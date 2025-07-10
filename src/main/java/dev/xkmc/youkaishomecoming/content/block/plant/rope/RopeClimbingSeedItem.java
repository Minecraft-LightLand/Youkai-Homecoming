package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class RopeClimbingSeedItem extends ItemNameBlockItem {

	public RopeClimbingSeedItem(Block block, Properties prop) {
		super(block, prop);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		if (getBlock() instanceof RopeLoggedCropBlock block) {
			var result = block.defaultBlockState().setValue(RopeLoggedCropBlock.ROPELOGGED, true);
			var level = ctx.getLevel();
			var clickPos = ctx.getClickedPos();
			var clickState = level.getBlockState(clickPos);
			boolean creative = ctx.getPlayer() != null && ctx.getPlayer().getAbilities().instabuild;
			if (RopeLoggedCropBlock.isRope(clickState)) {
				if (result.canSurvive(level, clickPos)) {
					if (!level.isClientSide()) {
						level.setBlockAndUpdate(clickPos, result);
						if (!creative) ctx.getItemInHand().shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			} else if (result.canSurvive(level, clickPos.above())) {
				var upPos = clickPos.above();
				if (RopeLoggedCropBlock.isRope(level.getBlockState(upPos))) {
					if (!level.isClientSide()) {
						level.setBlockAndUpdate(upPos, result);
						if (!creative) ctx.getItemInHand().shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.useOn(ctx);
	}

}
