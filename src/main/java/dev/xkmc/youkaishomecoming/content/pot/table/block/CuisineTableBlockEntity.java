package dev.xkmc.youkaishomecoming.content.pot.table.block;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItem;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class CuisineTableBlockEntity extends BaseBlockEntity {

	@SerialClass.SerialField
	private final List<ItemStack> contents = new ArrayList<>();

	private TableItem model = null;

	public CuisineTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean addItem(ItemStack stack) {
		if (level == null) return false;
		var prev = getModel();
		var ans = prev.find(level, stack);
		if (ans.isPresent()) {
			if (level.isClientSide()) return true;
			contents.add(stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
			model = ans.get();
			notifyTile();
			return true;
		}
		return false;
	}

	public boolean addToPlayer(Player player) {
		if (level == null) return false;
		var opt = getModel().complete(level);
		if (opt.isPresent()) {
			if (level.isClientSide()) return true;
			model = null;
			contents.clear();

			notifyTile();
			return true;
		}
		return false;
	}

	public void notifyTile() {
		sync();
		setChanged();
	}

	public TableItem getModel() {
		if (model != null) return model;
		var ans = TableModelManager.find(level, contents);
		if (ans.right().isPresent()) {
			var pair = ans.right().get();
			contents.clear();
			contents.addAll(pair.getSecond());
			setChanged();
		}
		model = ans.map(l -> l, Pair::getFirst);
		return model;
	}

}
