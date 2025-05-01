package dev.xkmc.youkaishomecoming.content.pot.tank;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.util.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class CopperFaucetBlock implements
		CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
		UseWithoutItemBlockMethod, ShapeBlockMethod {

	public static final BlockMethod INS = new CopperFaucetBlock();
	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.FAUCET_BE, CopperFaucetBlockEntity.class);

	private static final VoxelShape[] SHAPES;

	static {
		SHAPES = new VoxelShape[4];
		for (int i = 0; i < 4; i++) {
			Direction dir = Direction.from2DDataValue(i);
			SHAPES[i] = Shapes.or(
					new VoxelBuilder(5, 5, 15, 11, 11, 16).rotateFromNorth(dir),
					new VoxelBuilder(6, 6, 6, 10, 10, 16).rotateFromNorth(dir),
					new VoxelBuilder(6, 4, 6, 10, 6, 10).rotateFromNorth(dir),
					new VoxelBuilder(5, 2, 5, 11, 4, 11).rotateFromNorth(dir),
					new VoxelBuilder(5, 11, 7, 11, 12, 13).rotateFromNorth(dir),
					new VoxelBuilder(7, 10, 9, 9, 11, 11).rotateFromNorth(dir)
			);
		}
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.OPEN);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.OPEN, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		return ctx.getClickedFace().getAxis() == Direction.Axis.Y ? state :
				state.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getClickedFace());
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue()];
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof CopperFaucetBlockEntity be) {
			return be.activate() ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}
		return InteractionResult.PASS;
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/faucet")))
				.texture("base", pvd.modLoc("block/utensil/" + ctx.getName()))
				.texture("valve", pvd.modLoc("block/utensil/" + ctx.getName() + "_valve"))
				.texture("particle", pvd.modLoc("block/utensil/" + ctx.getName())));
	}

}
