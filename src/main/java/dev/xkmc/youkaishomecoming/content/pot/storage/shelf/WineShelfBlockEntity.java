package dev.xkmc.youkaishomecoming.content.pot.storage.shelf;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@SerialClass
public class WineShelfBlockEntity extends BaseBlockEntity implements BaseContainerListener, BlockContainer {

	@SerialClass.SerialField
	public final ShelfContainer items = new ShelfContainer(9).setMax(1).add(this);

	public WineShelfBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
		ItemStack stack = player.getItemInHand(hand);
		if (items.getItem(index).isEmpty()) {
			if (stack.is(YHTagGen.BOTTLED)) {
				if (!player.level().isClientSide())
					items.setItem(index, stack.split(1));
				return true;
			}
		} else {
			if (!player.level().isClientSide()) {
				var ans = items.getItem(index);
				items.setItem(index, ItemStack.EMPTY);
				if (stack.isEmpty()) {
					player.setItemInHand(hand, ans);
				} else {
					player.getInventory().placeItemBackInInventory(ans);
				}
			}
			return true;
		}
		return false;
	}

}
