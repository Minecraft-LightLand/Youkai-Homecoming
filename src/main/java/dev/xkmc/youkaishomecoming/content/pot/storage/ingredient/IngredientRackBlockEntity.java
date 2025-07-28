package dev.xkmc.youkaishomecoming.content.pot.storage.ingredient;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.storage.bottle.SauceRackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@SerialClass
public class IngredientRackBlockEntity extends BaseBlockEntity implements BaseContainerListener, BlockContainer {

	public static boolean isSauce(ItemStack stack) {
		return SauceRackBlockEntity.isFlask(stack);
	}

	@SerialClass.SerialField
	public final IngredientRackContainer items = new IngredientRackContainer(6).setMax(64).add(this);

	public IngredientRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	@Override
	public void notifyTile() {
		sync();
		setChanged();
	}

	public boolean click(Player player, InteractionHand hand, int index) {
		if (level == null) return false;
		ItemStack stack = player.getItemInHand(hand);
		var ans = items.getItem(index);
		if (stack.isEmpty()) {
			if (ans.isEmpty()) return false;
			if (hand == InteractionHand.OFF_HAND) return false;
			if (!level.isClientSide()) {
				int amount = player.isShiftKeyDown() ? Math.min(ans.getCount(), ans.getMaxStackSize()) : 1;
				player.getInventory().placeItemBackInInventory(ans.split(amount));
				level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
				notifyTile();
			}
			return true;
		}
		if (isSauce(stack)) return false;
		boolean up = index < 3;
		int max = getBlockState().getValue(IngredientRackBlock.SUPPORT) == IngredientRackBlock.State.STACKED || up ? 32 : 64;
		if (ans.isEmpty()) {
			if (!level.isClientSide()) {
				int amount = Math.min(max, stack.getCount());
				items.setItem(index, stack.split(amount));
				level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
				notifyTile();
			}
			return true;
		} else if (ItemStack.isSameItemSameTags(ans, stack)) {
			int amount = Math.min(max - ans.getCount(), stack.getCount());
			if (amount <= 0) return false;
			if (!level.isClientSide()) {
				ans.grow(amount);
				stack.shrink(amount);
				level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
				notifyTile();
			}
			return true;
		}
		return false;
	}

}
