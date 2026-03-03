package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.pot.overlay.IHintableBlock;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.util.WaterConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class IronBowlBlock extends BowlBlock implements IHintableBlock {

	public IronBowlBlock(Properties prop, Vec3 saucer) {
		super(prop, saucer);
	}

	public boolean startCooking(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, Block block, Block target, int water) {
		if (state.is(block) && WaterConsumer.isWaterContainer(stack, water)) {
			if (!level.isClientSide()) {
				var pot = target.defaultBlockState().setValue(FACING, state.getValue(FACING));
				level.setBlockAndUpdate(pos, pot);
				if (!player.getAbilities().instabuild) {
					if (stack.getCount() > 1) {
						var copy = stack.copyWithCount(1);
						var remain = WaterConsumer.drainWater(copy, water);
						player.getInventory().placeItemBackInInventory(remain);
					} else {
						player.setItemInHand(hand, WaterConsumer.drainWater(stack, water));
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public List<Ingredient> getHints(Level level, BlockPos pos) {
		return List.of(Ingredient.of(Items.WATER_BUCKET));
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (startCooking(stack, state, level, pos, player, hand, YHBlocks.IRON_BOWL.get(), YHBlocks.SMALL_POT.get(), 250))
			return ItemInteractionResult.SUCCESS;
		if (startCooking(stack, state, level, pos, player, hand, YHBlocks.IRON_POT.get(), YHBlocks.SHORT_POT.get(), 250))
			return ItemInteractionResult.SUCCESS;
		if (startCooking(stack, state, level, pos, player, hand, YHBlocks.STOCKPOT.get(), YHBlocks.LARGE_POT.get(), 1000))
			return ItemInteractionResult.SUCCESS;
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
