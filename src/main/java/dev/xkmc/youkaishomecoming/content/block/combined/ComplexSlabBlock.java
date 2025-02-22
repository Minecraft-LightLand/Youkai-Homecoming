package dev.xkmc.youkaishomecoming.content.block.combined;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComplexSlabBlock extends CombinedSlabBlock implements EntityBlock {

	public ComplexSlabBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CombinedBlockEntity(YHBlocks.BE_COMBINED.get(), pos, state);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		return List.of();
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
			if (level.getBlockEntity(pos) instanceof CombinedBlockEntity be) {
				be.set(null, null);
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (level.getBlockEntity(pos) instanceof CombinedBlockEntity be) {
			boolean slab = state.getValue(FACING).getAxis() == Direction.Axis.Y;
			var a = be.getA();
			if (a != null)
				Block.popResource(level, pos, (slab ? a.slab() : a.vertical())
						.value().asItem().getDefaultInstance());
			var b = be.getA();
			if (b != null)
				Block.popResource(level, pos, (slab ? b.slab() : b.vertical())
						.value().asItem().getDefaultInstance());
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

}
