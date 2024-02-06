package dev.xkmc.youkaihomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.youkaihomecoming.util.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class MultiFenceBlock extends Block implements SimpleWaterloggedBlock, LeftClickBlock {

	// states
	public enum State implements StringRepresentable {
		OPEN, CONNECT, FLAT, UP, CW, CCW;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public State flip() {
			return switch (this) {
				case CW -> CCW;
				case CCW -> CW;
				default -> this;
			};
		}

		public int type() {
			return switch (this) {
				case OPEN, CONNECT -> 0;
				case UP -> 1;
				case FLAT -> 2;
				case CW, CCW -> 3;
			};
		}

		public int collide() {
			return switch (this) {
				case OPEN, CONNECT -> 0;
				case FLAT, UP -> 1;
				case CW -> 2;
				case CCW -> 3;
			};
		}

	}

	public static final EnumProperty<State>[] STATES;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

	static {
		STATES = new EnumProperty[4];
		for (int i = 0; i < 4; i++) {
			STATES[i] = EnumProperty.create(Direction.from2DDataValue(i).getName(), State.class);
		}
	}

	public static final VoxelShape[] SHAPES;

	private int indexOf(BlockState state) {
		int total = 0;
		for (int i = 0; i < 4; i++) {
			var dir = Direction.from2DDataValue(i);
			total |= of(state, dir).collide() << (i * 2);

		}
		return total;
	}

	static {
		SHAPES = new VoxelShape[256];
		var box = new VoxelBuilder(0, 0, 0, 16, 16, 2);
		var left = new VoxelBuilder(0, 8, 0, 8, 16, 2);
		var right = new VoxelBuilder(8, 8, 0, 16, 16, 2);
		VoxelShape[][] parts = new VoxelShape[4][4];
		for (int i = 0; i < 4; i++) {
			Direction dir = Direction.from2DDataValue(i);
			parts[i][0] = Shapes.empty();
			var rbox = box.rotateFromNorth(dir);
			var ls = left.rotateFromNorth(dir);
			var rs = right.rotateFromNorth(dir);
			parts[i][1] = rbox;
			parts[i][2] = Shapes.join(rbox, rs, BooleanOp.ONLY_FIRST);
			parts[i][3] = Shapes.join(rbox, ls, BooleanOp.ONLY_FIRST);
		}
		for (int i = 0; i < 256; i++) {
			List<VoxelShape> list = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				int index = i >> j * 2 & 3;
				list.add(parts[j][index]);
			}
			SHAPES[i] = Shapes.or(Shapes.empty(), list.toArray(VoxelShape[]::new));
		}
	}

	public static State of(BlockState state, Direction dire) {
		return state.getValue(STATES[dire.get2DDataValue()]);
	}

	public static BlockState with(BlockState state, Direction dire, State val) {
		return state.setValue(STATES[dire.get2DDataValue()], val);
	}

	public static BlockState with(BlockState state, State north, State south, State west, State east) {
		state = with(state, Direction.NORTH, north);
		state = with(state, Direction.SOUTH, south);
		state = with(state, Direction.WEST, west);
		state = with(state, Direction.EAST, east);
		return state;
	}

	public MultiFenceBlock(Properties prop) {
		super(prop);
		registerDefaultState(with(defaultBlockState(), State.FLAT, State.OPEN, State.OPEN, State.OPEN)
				.setValue(INVERTED, false)
				.setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STATES);
		builder.add(WATERLOGGED, INVERTED);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(Items.DEBUG_STICK)) return InteractionResult.PASS;
		if (canBeReplaced(state, new BlockPlaceContext(player, hand, stack, hit)))
			return InteractionResult.PASS;
		if (!level.isClientSide()) {
			while (level.getBlockState(pos.above()).is(this)) pos = pos.above();
			state = level.getBlockState(pos);
			level.setBlockAndUpdate(pos, state.setValue(INVERTED, !state.getValue(INVERTED)));
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		var result = RayTraceUtil.rayTraceBlock(level, player, player.getBlockReach());
		var hit = result.getLocation();
		if (!new AABB(pos).inflate(0.1).contains(hit)) {
			return true;
		}
		var rel = hit.subtract(Vec3.atCenterOf(pos));
		Direction candidate = null;
		double minSqr = 10;
		int count = 0;
		for (int i = 0; i < 4; i++) {
			Direction dir = Direction.from2DDataValue(i);
			if (of(state, dir).type() == 0) {
				continue;
			}
			count++;
			double distSqr = rel.x * dir.getStepX() + rel.z * dir.getStepZ();
			if (candidate == null || distSqr > minSqr) {
				minSqr = distSqr;
				candidate = dir;
			}
		}
		if (count > 1) {
			level.setBlockAndUpdate(pos, with(state, candidate, State.OPEN));
		} else {
			level.removeBlock(pos, false);
		}
		popResource(level, pos, asItem().getDefaultInstance());
		return true;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
		if (!ctx.getItemInHand().is(asItem())) return false;
		Direction dir = ctx.getClickedFace().getOpposite();
		BlockPos pos = ctx.getClickedPos();
		var hit = ctx.getClickLocation();
		if (dir.getAxis().isHorizontal()) {
			if (new AABB(pos).contains(hit)) {
				var center = Vec3.atCenterOf(pos).relative(dir, 0.5);
				var axis = dir.getCounterClockWise().getAxis();
				double val = hit.subtract(center).get(axis);
				Direction req;
				if (Math.abs(val) > 3d / 16) {
					var ad = val > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
					req = Direction.get(ad, axis);
				} else {
					req = dir.getOpposite();
				}
				State st = of(state, req);
				return st.type() == 0;
			}
			return false;
		}
		var rel = hit.subtract(Vec3.atCenterOf(pos));
		var dx = Direction.getNearest(rel.x, 0, rel.z);
		if (dx.getAxis().isVertical()) dx = Direction.NORTH;
		return of(state, dx).type() == 0;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Direction dir = ctx.getClickedFace().getOpposite();
		var hit = ctx.getClickLocation();
		BlockState state;
		BlockState old = ctx.getLevel().getBlockState(pos);
		if (old.is(this)) {
			state = old;
			if (dir.getAxis().isHorizontal()) {
				if (new AABB(pos).contains(hit)) {
					var center = Vec3.atCenterOf(pos).relative(dir, 0.5);
					var axis = dir.getCounterClockWise().getAxis();
					double val = hit.subtract(center).get(axis);
					Direction req;
					if (Math.abs(val) > 3d / 16) {
						var ad = val > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
						req = Direction.get(ad, axis);
					} else {
						req = dir.getOpposite();
					}
					State st = of(state, req);
					if (st.type() == 0) {
						return with(state, req, State.FLAT);
					}
				}
			}
		} else {
			state = defaultBlockState();
			FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
			boolean flag = fluidstate.getType() == Fluids.WATER;
			state = with(state, State.OPEN, State.OPEN, State.OPEN, State.OPEN);
			state = state.setValue(WATERLOGGED, flag).setValue(INVERTED, false);
		}
		if (dir.getAxis().isHorizontal()) {
			return with(state, dir, State.FLAT).setValue(INVERTED, true);
		}
		var rel = hit.subtract(Vec3.atCenterOf(pos));
		var dx = Direction.getNearest(rel.x, 0, rel.z);
		if (dx.getAxis().isVertical()) dx = Direction.NORTH;
		return with(state, dx, State.FLAT);
	}

	public BlockState updateShape(BlockState state, Direction dire, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		if (dire == Direction.DOWN && !state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		if (dire == Direction.UP) {
			var ans = state;
			boolean valid = neighborState.is(this);
			if (valid) {
				ans = ans.setValue(INVERTED, neighborState.getValue(INVERTED));
			}
			for (int i = 0; i < 4; i++) {
				Direction d = Direction.from2DDataValue(i);
				State old = of(state, d);
				if (old.type() == 0) continue;
				boolean up = valid && of(neighborState, d).type() > 0;
				if (old == State.UP) old = State.FLAT;
				ans = with(ans, d, up ? State.UP : old);
			}
			return ans;
		}
		if (dire.getAxis().isHorizontal()) {
			BlockState ans = state;
			var left = dire.getClockWise();
			var right = dire.getCounterClockWise();
			boolean flag = neighborState.is(this);
			if (of(state, dire).type() == 0) {
				if (flag) {
					if (of(state, left).type() > 1) {
						flag = of(neighborState, left).type() > 1;
					}
					if (of(state, right).type() > 1) {
						flag &= of(neighborState, right).type() > 1;
					}
				}
				ans = with(ans, dire, flag ? State.CONNECT : State.OPEN);
			}
			if (neighborState.is(this)) {
				if (of(state, left).type() > 1 && of(neighborState, left).type() > 0) {
					BlockPos rev = pos.relative(dire.getOpposite()).below();
					BlockState low = level.getBlockState(rev);
					if (low.is(this) && of(low, left).type() > 1)
						ans = with(ans, left, State.CW);
				}
				if (of(state, right).type() > 1 && of(neighborState, right).type() > 0) {
					BlockPos rev = pos.relative(dire.getOpposite()).below();
					BlockState low = level.getBlockState(rev);
					if (low.is(this) && of(low, right).type() > 1)
						ans = with(ans, right, State.CCW);
				}
			}
			return ans;
		}
		return super.updateShape(state, dire, neighborState, level, pos, neighborPos);
	}

	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[indexOf(state)];
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> with(state,
					of(state, Direction.SOUTH).flip(),
					of(state, Direction.NORTH).flip(),
					of(state, Direction.WEST).flip(),
					of(state, Direction.EAST).flip());
			case FRONT_BACK -> with(state,
					of(state, Direction.NORTH).flip(),
					of(state, Direction.SOUTH).flip(),
					of(state, Direction.EAST).flip(),
					of(state, Direction.WEST).flip());
			case NONE -> state;
		};
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotate) {
		BlockState ans = state;
		ans = with(ans, rotate.rotate(Direction.NORTH), of(state, Direction.NORTH));
		ans = with(ans, rotate.rotate(Direction.SOUTH), of(state, Direction.SOUTH));
		ans = with(ans, rotate.rotate(Direction.WEST), of(state, Direction.WEST));
		ans = with(ans, rotate.rotate(Direction.EAST), of(state, Direction.EAST));
		return ans;
	}

	private static class FlatModelSet {

		private static ModelFile[][][] FLAT = null, DIAG = null;

		private static void genRow(ModelBuilder<?> builder, int x, int y, int z, int w, int h, int t, int u) {
			builder.element()
					.from(x, y, z).to(x + w, y + h, z + t)
					.face(Direction.NORTH).uvs(u, 0, u + w, h).texture("#all").end()
					.face(Direction.SOUTH).uvs(u + w, 0, u, h).texture("#all").end()
					.face(Direction.UP).uvs(u + w, t, u, 0).texture("#all").end()
					.face(Direction.DOWN).uvs(u + w, h - t, u, h).texture("#all").end();
		}

		private static void genDiagonal(ModelBuilder<?> builder, float x, float y, float z, float w, float h, float t, float u, Rotate r, Side s) {
			int angle = r == Rotate.CW ? -45 : 45;
			int px = r == Rotate.CW ? 0 : 16;
			var elem = builder.element()
					.from(x, y, z).to(x + w, y + h, z + t)
					.rotation().angle(angle).axis(Direction.Axis.Z).origin(px, 0, 0).end()
					.face(Direction.NORTH).uvs(u, 0, u + w, h).texture("#all").end()
					.face(Direction.SOUTH).uvs(u + w, 0, u, h).texture("#all").end()
					.face(Direction.UP).uvs(u + w, t, u, 0).texture("#all").end()
					.face(Direction.DOWN).uvs(u + w, h - t, u, h).texture("#all").end();
			if (s == Side.LEFT) {
				elem.face(Direction.WEST).uvs(u + w - t, 0, u + w, h).texture("#all").end();
			} else {
				elem.face(Direction.EAST).uvs(u, 0, u + t, h).texture("#all").end();
			}
		}

		private static void genExt(ModelBuilder<?> builder, int x, int y, int z, int w, int h, int t, int u, Side s) {
			var elem = builder.element()
					.from(x, y, z).to(x + w, y + h, z + t)
					.face(Direction.NORTH).uvs(u, 0, u + w, h).texture("#all").end()
					.face(Direction.SOUTH).uvs(u + w, 0, u, h).texture("#all").end()
					.face(Direction.UP).uvs(u + w, 0, u, t).texture("#all").end()
					.face(Direction.DOWN).uvs(u + w, h - t, u, h).texture("#all").end();
			if (s == Side.LEFT) {
				elem.face(Direction.WEST).uvs(u + w - t, 0, u + w, h).texture("#all").end();
			} else {
				elem.face(Direction.EAST).uvs(u, 0, u + t, h).texture("#all").end();
			}
		}

		private static void genColumn(ModelBuilder<?> builder, int x, int y, int z, int w, int h, int t, int u, int v) {
			builder.element()
					.from(x, y, z).to(x + w, y + h, z + t)
					.face(Direction.NORTH).uvs(16 - h - u, v + w, 16 - u, v).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end()
					.face(Direction.EAST).uvs(16 - h - u, v + t, 16 - u, v).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end()
					.face(Direction.SOUTH).uvs(16 - h - u, v, 16 - u, v + w).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end()
					.face(Direction.WEST).uvs(16 - h - u, v + w, 16 - u, v + w - t).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end()
					.face(Direction.UP).uvs(16 - h - u, v, 16 - h - u + t, v + w).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end()
					.face(Direction.DOWN).uvs(16 - t, v, 16, v + w).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all").end();
		}

		private static ModelFile genFlat(RegistrateBlockstateProvider pvd, Face f, Side s, Type t) {
			String name = (f.name() + "_" + s.name() + "_" + t.name()).toLowerCase(Locale.ROOT);
			var builder = pvd.models().withExistingParent("custom/handrail_" + name, "block/block");
			int off = t == Type.CORNER ? 1 : 0;
			int z = f == Face.INNER ? 1 : 0;
			int x = s == Side.RIGHT ? 8 : off;
			int u = s == Side.RIGHT ? off : 8;
			int y = 13;
			int h = 3;
			genRow(builder, x, y, z, 8 - off, h, 1, u);
			if (t == Type.EXTEND) {
				int e = f == Face.INNER ? 2 : 1;
				x = s == Side.RIGHT ? 16 : -e;
				u = s == Side.RIGHT ? 16 - e : 0;
				genExt(builder, x, y, z, e, h, 1, u, s);
			}
			x = s == Side.RIGHT ? 11 : 3;
			genColumn(builder, x, 0, 1 - z, 2, 16, 1, 0, 3);
			builder.texture("particle", "#all");
			return builder;
		}

		private static ModelFile genDiag(RegistrateBlockstateProvider pvd, Face f, Side s, Rotate r) {
			String name = (f.name() + "_" + s.name() + "_" + r.name()).toLowerCase(Locale.ROOT);
			var builder = pvd.models().withExistingParent("custom/handrail_" + name, "block/block");
			int z = f == Face.INNER ? 1 : 0;
			float x = (s == Side.RIGHT ? 12.5f : 0) + (r == Rotate.CW ? -11.25f : 2.25f);
			float u = s == Side.RIGHT ? 0 : 3.5f;
			float y = 8.25f;
			genDiagonal(builder, x, y, z, 12.5f, 3, 1, u, r, s);
			int x0 = s == Side.RIGHT ? 11 : 3;
			int h = (s == Side.RIGHT) == (r == Rotate.CW) ? 2 : 10;
			genColumn(builder, x0, 0, 1 - z, 2, h, 1, 0, 3);
			if (h < 8) {
				genColumn(builder, x0, h - 10, 1 - z, 2, 10 - h, 1, 10 - h, 3);
			}
			return builder;
		}

		private static void genPost(RegistrateBlockstateProvider pvd, Face f) {
			String name = "post_" + f.name().toLowerCase(Locale.ROOT);
			var builder = pvd.models().withExistingParent("custom/handrail_" + name, "block/block");
			int z = f == Face.INNER ? 1 : 0;
			genColumn(builder, 3, 0, 1 - z, 2, 16, 1, 0, 3);
			genColumn(builder, 11, 0, 1 - z, 2, 16, 1, 0, 3);
			builder.texture("particle", "#all");
		}

		private static void init(RegistrateBlockstateProvider pvd) {
			if (FLAT != null) return;
			FLAT = new ModelFile[2][2][3];
			DIAG = new ModelFile[2][2][2];
			for (Face f : Face.values()) {
				for (Side s : Side.values()) {
					for (Type t : Type.values()) {
						if (f == Face.OUTER && t == Type.CORNER) continue;
						FLAT[f.ordinal()][s.ordinal()][t.ordinal()] = genFlat(pvd, f, s, t);
					}
					for (Rotate r : Rotate.values()) {
						DIAG[f.ordinal()][s.ordinal()][r.ordinal()] = genDiag(pvd, f, s, r);
					}
				}
			}
			genPost(pvd, Face.INNER);
			genPost(pvd, Face.OUTER);

		}

		private enum Face {INNER, OUTER}

		private enum Side {LEFT, RIGHT}

		private enum Type {CORNER, CONNECT, EXTEND}

		private enum Rotate {CW, CCW}


		private final DataGenContext<Block, MultiFenceBlock> ctx;
		private final RegistrateBlockstateProvider pvd;
		private final ModelFile[][][] flat, diag;

		private FlatModelSet(DataGenContext<Block, MultiFenceBlock> ctx, RegistrateBlockstateProvider pvd) {
			this.ctx = ctx;
			this.pvd = pvd;
			init(pvd);
			flat = new ModelFile[2][2][3];
			diag = new ModelFile[2][2][2];
			for (Face f : Face.values()) {
				for (Side s : Side.values()) {
					for (Type t : Type.values()) {
						if (f == Face.OUTER && t == Type.CORNER) continue;
						flat[f.ordinal()][s.ordinal()][t.ordinal()] = genFlatModel(f, s, t);
					}
					for (Rotate r : Rotate.values()) {
						diag[f.ordinal()][s.ordinal()][r.ordinal()] = genDiagModel(f, s, r);
					}
				}
			}
		}

		private ModelFile genFlatModel(Face f, Side s, Type t) {
			String name = (f.name() + "_" + s.name() + "_" + t.name()).toLowerCase(Locale.ROOT);
			return pvd.models().getBuilder("block/" + ctx.getName() + "_" + name)
					.parent(FLAT[f.ordinal()][s.ordinal()][t.ordinal()])
					.texture("all", pvd.modLoc("block/" + ctx.getName()));
		}

		private ModelFile genDiagModel(Face f, Side s, Rotate r) {
			String name = (f.name() + "_" + s.name() + "_" + r.name()).toLowerCase(Locale.ROOT);
			return pvd.models().getBuilder("block/" + ctx.getName() + "_" + name)
					.parent(DIAG[f.ordinal()][s.ordinal()][r.ordinal()])
					.texture("all", pvd.modLoc("block/" + ctx.getName()));
		}

		private ModelFile getModel(Face f, Side s, Type t) {
			return flat[f.ordinal()][s.ordinal()][t.ordinal()];
		}

		private ModelFile getModel(Face f, Side s, Rotate r) {
			return diag[f.ordinal()][s.ordinal()][r.ordinal()];
		}

		private void buildInnerSide(MultiPartBlockStateBuilder builder, int rot,
									EnumProperty<State> self, Side side, EnumProperty<State> adj) {

			builder.part().modelFile(getModel(Face.INNER, side, Type.CORNER)).rotationY(rot).addModel()
					.condition(INVERTED, true)
					.condition(self, State.FLAT)
					.condition(adj, State.FLAT)
					.end();

			builder.part().modelFile(getModel(Face.INNER, side, Type.CONNECT)).rotationY(rot).addModel()
					.condition(INVERTED, true)
					.condition(self, State.FLAT)
					.condition(adj, State.CONNECT)
					.end();

			builder.part().modelFile(getModel(Face.INNER, side, Type.EXTEND)).rotationY(rot).addModel()
					.condition(INVERTED, true)
					.condition(self, State.FLAT)
					.condition(adj, State.OPEN, State.UP, State.CW, State.CCW)
					.end();
		}

		private void buildOuterSide(MultiPartBlockStateBuilder builder, int rot,
									EnumProperty<State> self, Side side, EnumProperty<State> adj) {

			builder.part().modelFile(getModel(Face.OUTER, side, Type.CONNECT)).rotationY(rot).addModel()
					.condition(INVERTED, false)
					.condition(self, State.FLAT)
					.condition(adj, State.CONNECT, State.FLAT)
					.end();

			builder.part().modelFile(getModel(Face.OUTER, side, Type.EXTEND)).rotationY(rot).addModel()
					.condition(INVERTED, false)
					.condition(self, State.FLAT)
					.condition(adj, State.OPEN, State.UP, State.CW, State.CCW)
					.end();
		}

		private void buildDiag(MultiPartBlockStateBuilder builder, int rot, EnumProperty<State> self,
							   Face face, Side side) {

			builder.part().modelFile(getModel(face, side, Rotate.CW)).rotationY(rot).addModel()
					.condition(INVERTED, face == Face.INNER)
					.condition(self, State.CW)
					.end();

			builder.part().modelFile(getModel(face, side, Rotate.CCW)).rotationY(rot).addModel()
					.condition(INVERTED, face == Face.INNER)
					.condition(self, State.CCW)
					.end();

		}

		private void buildPost(MultiPartBlockStateBuilder builder, int rot, EnumProperty<State> self, Face face) {
			String name = "post_" + face.name().toLowerCase(Locale.ROOT);
			builder.part().modelFile(pvd.models().getBuilder("block/" + ctx.getName() + "_" + name)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/handrail_" + name)))
							.texture("all", pvd.modLoc("block/" + ctx.getName()))
					).rotationY(rot).addModel()
					.condition(INVERTED, face == Face.INNER)
					.condition(self, State.UP)
					.end();
		}

		private void buildFace(MultiPartBlockStateBuilder builder, int rot,
							   EnumProperty<State> self, EnumProperty<State> left, EnumProperty<State> right) {
			buildInnerSide(builder, rot, self, Side.LEFT, left);
			buildInnerSide(builder, rot, self, Side.RIGHT, right);
			buildOuterSide(builder, rot, self, Side.LEFT, left);
			buildOuterSide(builder, rot, self, Side.RIGHT, right);
			buildPost(builder, rot, self, Face.INNER);
			buildPost(builder, rot, self, Face.OUTER);
			buildDiag(builder, rot, self, Face.INNER, Side.LEFT);
			buildDiag(builder, rot, self, Face.INNER, Side.RIGHT);
			buildDiag(builder, rot, self, Face.OUTER, Side.LEFT);
			buildDiag(builder, rot, self, Face.OUTER, Side.RIGHT);
		}

	}

	public static void genModel(DataGenContext<Block, MultiFenceBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		FlatModelSet set = new FlatModelSet(ctx, pvd);
		for (int i = 0; i < 4; i++) {
			Direction dir = Direction.from2DDataValue(i);
			int rot = (((int) dir.toYRot()) + 180) % 360;
			var left = STATES[dir.getCounterClockWise().get2DDataValue()];
			var right = STATES[dir.getClockWise().get2DDataValue()];
			set.buildFace(builder, rot, STATES[i], left, right);
		}
	}

}
