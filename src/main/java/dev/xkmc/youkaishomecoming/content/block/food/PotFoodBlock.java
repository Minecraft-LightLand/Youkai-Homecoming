package dev.xkmc.youkaishomecoming.content.block.food;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PotFoodBlock extends BowlBlock {

	public static final IntegerProperty SERVE_2 = IntegerProperty.create("serve", 1, 2);

	public PotFoodBlock(Properties prop, Vec3 saucer, ItemLike food) {
		super(prop, saucer, food);
		registerDefaultState(defaultBlockState().setValue(SERVE_2, 2));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(SERVE_2);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);
		var item = food.asItem().getDefaultInstance();
		if (food.asItem() != asItem()) {
			if (item.hasCraftingRemainingItem()) {
				if (stack.is(item.getCraftingRemainingItem().getItem())) {
					if (!level.isClientSide()) {
						if (!player.isCreative())
							stack.shrink(1);
						player.getInventory().placeItemBackInInventory(item);
						consume(state, level, pos);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	protected void consume(BlockState state, Level level, BlockPos pos) {
		int serve = state.getValue(SERVE_2);
		if (serve > 1) {
			level.setBlockAndUpdate(pos, state.setValue(SERVE_2, serve - 1));
			return;
		}
		super.consume(state, level, pos);
	}

	public ItemStack asBowls() {
		return food.asItem().getDefaultInstance().copyWithCount(2);
	}

}
