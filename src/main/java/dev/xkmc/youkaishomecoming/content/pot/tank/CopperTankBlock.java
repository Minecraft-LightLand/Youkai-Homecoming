package dev.xkmc.youkaishomecoming.content.pot.tank;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class CopperTankBlock extends DelegateEntityBlockImpl {

	public static final BlockMethod INS = new CopperTankBlockVertical();
	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.TANK_BE, CopperTankBlockEntity.class);

	public CopperTankBlock(Properties p, BlockMethod... impl) {
		super(p, impl);
	}

	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && player.isCreative()) {
			preventCreativeDropFromBottomPart(level, pos, state, player);
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	protected static void preventCreativeDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		DoubleBlockHalf half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
		BlockPos alt = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
		BlockState altState = level.getBlockState(alt);
		if (altState.is(state.getBlock()) && altState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != half) {
			BlockState ans = altState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
			level.setBlock(alt, ans, 35);
			level.levelEvent(player, 2001, alt, Block.getId(altState));
		}
	}

	public static void buildStates(DataGenContext<Block, CopperTankBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			boolean up = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
			boolean open = state.getValue(BlockStateProperties.OPEN);
			String id = up ? "block/copper_tank" : "block/copper_tank_lower";
			if (open) id += "_open";
			String side = up ? "copper_tank_side_up" : "copper_tank_side_down";
			String top = open ? "copper_tank_top_open" : "copper_tank_top";
			return ConfiguredModel.builder().modelFile(pvd.models().cubeBottomTop(id,
					pvd.modLoc("block/utensil/" + side),
					pvd.modLoc("block/utensil/copper_tank_bottom"),
					pvd.modLoc("block/utensil/" + top)
			)).build();
		});
	}

}
