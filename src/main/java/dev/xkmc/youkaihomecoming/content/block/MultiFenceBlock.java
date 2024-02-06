package dev.xkmc.youkaihomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class MultiFenceBlock extends Block {

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

	}

	private static class FlatModelSet {

		private enum Face {
			INNER, OUTER;
		}

		private enum Side {
			LEFT, RIGHT
		}

		private enum Type {
			CORNER, CONNECT, EXTEND
		}


		private final DataGenContext<Block, MultiFenceBlock> ctx;
		private final RegistrateBlockstateProvider pvd;
		private final ModelFile[][][] modelSet;

		private FlatModelSet(DataGenContext<Block, MultiFenceBlock> ctx, RegistrateBlockstateProvider pvd) {
			this.ctx = ctx;
			this.pvd = pvd;
			modelSet = new ModelFile[2][2][3];
			for (Face f : Face.values()) {
				for (Side s : Side.values()) {
					for (Type t : Type.values()) {
						if (f == Face.OUTER && t == Type.CORNER) continue;
						modelSet[f.ordinal()][s.ordinal()][t.ordinal()] = genModel(f, s, t);
					}
				}
			}
		}

		private ModelFile genModel(Face f, Side s, Type t) {
			String name = (f.name() + "_" + s.name() + "_" + t.name()).toLowerCase(Locale.ROOT);
			var builder = pvd.models().withExistingParent("block/" + ctx.getName() + "_" + name, "block/block")
					.texture("all", pvd.modLoc("block/" + ctx.getName()));
			int x = 0;
			int z = 0;
			return builder;
		}

		private ModelFile getModel(Face f, Side s, Type t) {
			return modelSet[f.ordinal()][s.ordinal()][t.ordinal()];
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
					.condition(adj, State.OPEN)
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
					.condition(adj, State.OPEN)
					.end();
		}

		private void buildFace(MultiPartBlockStateBuilder builder, int rot,
							   EnumProperty<State> self, EnumProperty<State> left, EnumProperty<State> right) {

			buildInnerSide(builder, rot, self, Side.LEFT, left);
			buildInnerSide(builder, rot, self, Side.RIGHT, right);
			buildOuterSide(builder, rot, self, Side.LEFT, left);
			buildOuterSide(builder, rot, self, Side.RIGHT, right);
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

	public static final EnumProperty<State>[] STATES;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

	static {
		STATES = new EnumProperty[4];
		for (int i = 0; i < 4; i++) {
			STATES[i] = EnumProperty.create(Direction.from2DDataValue(i).getName(), State.class);
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
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STATES);
		builder.add(WATERLOGGED, INVERTED);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return super.use(state, level, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx);
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

}
