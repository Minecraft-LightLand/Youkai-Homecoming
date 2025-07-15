package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.overlay.IHintableBlock;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.util.WaterConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

	public boolean startCooking(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, Block block, Block target, int water) {
		var stack = player.getItemInHand(hand);
		if (state.is(block) &&
				CookingBlockEntity.isHeatedPos(level, pos) &&
				WaterConsumer.isWaterContainer(stack, water)) {
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
		if (CookingBlockEntity.isHeatedPos(level, pos)) {
			return List.of(Ingredient.of(Items.WATER_BUCKET));
		}
		return List.of();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (startCooking(state, level, pos, player, hand, YHBlocks.IRON_BOWL.get(), YHBlocks.SMALL_POT.get(), 250))
			return InteractionResult.SUCCESS;
		if (startCooking(state, level, pos, player, hand, YHBlocks.IRON_POT.get(), YHBlocks.SHORT_POT.get(), 250))
			return InteractionResult.SUCCESS;
		if (startCooking(state, level, pos, player, hand, YHBlocks.STOCKPOT.get(), YHBlocks.LARGE_POT.get(), 1000))
			return InteractionResult.SUCCESS;
		return InteractionResult.PASS;
	}

}
