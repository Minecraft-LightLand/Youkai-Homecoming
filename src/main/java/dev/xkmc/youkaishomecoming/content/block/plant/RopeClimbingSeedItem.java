package dev.xkmc.youkaishomecoming.content.block.plant;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

public class RopeClimbingSeedItem extends ItemNameBlockItem {

	private static boolean isRope(BlockState state) {
		return Configuration.ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES.get() ? state.is(ModTags.ROPES) : state.is(ModBlocks.ROPE.get());
	}

	public RopeClimbingSeedItem(Block block, Properties prop) {
		super(block, prop);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		if (getBlock() instanceof RopeClimbingCropBlock block) {
			var result = block.defaultBlockState().setValue(RopeClimbingCropBlock.ROPELOGGED, true);
			var level = ctx.getLevel();
			var clickPos = ctx.getClickedPos();
			var clickState = level.getBlockState(clickPos);
			if (isRope(clickState)) {
				if (result.canSurvive(level, clickPos)) {
					level.setBlockAndUpdate(clickPos, result);
					return InteractionResult.SUCCESS;
				}
			} else if (result.canSurvive(level, clickPos.above())) {
				var upPos = clickPos.above();
				if (isRope(level.getBlockState(upPos))) {
					level.setBlockAndUpdate(upPos, result);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.useOn(ctx);
	}

}
