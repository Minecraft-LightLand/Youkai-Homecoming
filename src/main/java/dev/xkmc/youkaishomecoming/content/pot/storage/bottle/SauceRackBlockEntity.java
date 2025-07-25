package dev.xkmc.youkaishomecoming.content.pot.storage.bottle;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.item.fluid.BucketBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
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
public class SauceRackBlockEntity extends BaseBlockEntity implements BaseContainerListener, BlockContainer {

	@SerialClass.SerialField
	public final SauceRackContainer items = new SauceRackContainer(3).setMax(1).add(this);

	public SauceRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
				player.getInventory().placeItemBackInInventory(ans.split(1));
				level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
				notifyTile();
			}
			return true;
		}
		if (ans.isEmpty() && (stack.getItem() instanceof SlipBottleItem || stack.getItem() instanceof BucketBottleItem)) {
			if (!level.isClientSide()) {
				items.setItem(index, stack.split(1));
				level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1, 1);
				notifyTile();
			}
			return true;
		}
		return false;
	}

}
