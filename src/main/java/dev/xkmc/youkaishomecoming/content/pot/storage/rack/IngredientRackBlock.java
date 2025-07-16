package dev.xkmc.youkaishomecoming.content.pot.storage.rack;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.util.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class IngredientRackBlock implements
		CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
		ShapeUpdateBlockMethod, ShapeBlockMethod, OnClickBlockMethod {

	public enum State implements StringRepresentable {
		GROUND, SUPPORTED, STACKED;


		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.INGREDIENT_RACK_BE, IngredientRackBlockEntity.class);

	public static final EnumProperty<State> SUPPORT = EnumProperty.create("support", State.class);

	public static final VoxelShape[] SHAPES;

	static {
		SHAPES = new VoxelShape[8];

		var bottom = Block.box(0, 0, 0, 16, 1, 16);

		for (int i = 0; i < 4; i++) {
			var dir = Direction.from2DDataValue(i);
			var flat = new VoxelBuilder(0, 8, 8, 16, 9, 19).rotateFromNorth(dir);
			var low = new VoxelBuilder(0, 1, 15, 16, 5, 16).rotateFromNorth(dir);
			var high = new VoxelBuilder(0, 9, 15, 16, 13, 16).rotateFromNorth(dir);
			var stacked = new VoxelBuilder(0, 0, 8, 16, 1, 16).rotateFromNorth(dir);
			SHAPES[i] = Shapes.or(bottom, flat, low, high);
			SHAPES[i + 4] = Shapes.or(stacked, flat, low, high);
		}

	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SUPPORT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(SUPPORT, State.GROUND);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		var level = ctx.getLevel();
		var lowPos = ctx.getClickedPos().below();
		var low = level.getBlockState(lowPos);
		var ans = low.is(state.getBlock()) ? State.STACKED :
				low.getShape(level, lowPos).max(Direction.Axis.Y) < 1 ? State.SUPPORTED : State.GROUND;
		return state.setValue(SUPPORT, ans);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int dir = state.getValue(BlockProxy.HORIZONTAL_FACING).get2DDataValue();
		boolean stack = state.getValue(SUPPORT) == State.STACKED;
		return SHAPES[stack ? dir + 4 : dir];
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction facing, BlockState ns, LevelAccessor level, BlockPos pos, BlockPos np) {
		if (facing != Direction.DOWN) return current;
		var ans = ns.is(state.getBlock()) ? State.STACKED :
				ns.getShape(level, np).max(Direction.Axis.Y) < 1 ? State.SUPPORTED : State.GROUND;
		return state.setValue(SUPPORT, ans);
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var dir = state.getValue(BlockProxy.HORIZONTAL_FACING);
		if (level.getBlockEntity(pos) instanceof IngredientRackBlockEntity be) {
			var vec = hit.getLocation().subtract(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
					.yRot(-(float) ((180 - dir.toYRot()) / 180 * Math.PI));
			int x = Mth.clamp((int) ((-vec.x + 0.5) * 3), 0, 2);
			int y = Mth.clamp((int) ((-vec.y + 0.5) * 2), 0, 1);
			if (be.click(player, hand, x + y * 3)) {
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	public static void buildModels(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> {
			var support = state.getValue(SUPPORT);
			String suffix = support == State.SUPPORTED ? "_supported" : support == State.STACKED ? "_stacked" : "";
			return pvd.models().getBuilder("block/" + ctx.getName() + suffix)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/ingredient_rack" + suffix)))
					.texture("base", pvd.modLoc("block/ingredient_rack/" + ctx.getName() + "_base"))
					.texture("top", pvd.modLoc("block/ingredient_rack/" + ctx.getName() + "_top"))
					.texture("bottom", pvd.modLoc("block/ingredient_rack/" + ctx.getName() + "_bottom"))
					.texture("side", pvd.modLoc("block/ingredient_rack/" + ctx.getName() + "_side"))
					.texture("support", pvd.modLoc("block/ingredient_rack/" + ctx.getName() + "_support"))
					.renderType("cutout");
		});
	}

}
