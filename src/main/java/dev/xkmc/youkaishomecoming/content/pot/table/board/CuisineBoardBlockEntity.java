package dev.xkmc.youkaishomecoming.content.pot.table.board;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.overlay.IHintableBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItem;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class CuisineBoardBlockEntity extends BaseBlockEntity implements IHintableBlockEntity {

	@SerialClass.SerialField
	private final List<ItemStack> contents = new ArrayList<>();

	private TableItem model = null;

	public CuisineBoardBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean performToolAction(ItemStack stack) {
		if (level == null) return false;
		var current = getModel().complete(level);
		if (current.isPresent()) {
			var cont = new SimpleContainer(1);
			cont.setItem(0, current.get());
			var wrapper = new RecipeWrapper(new InvWrapper(cont));
			List<CuttingBoardRecipe> list = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CUTTING.get(), wrapper, level);
			for (var recipe : list) {
				if (recipe.getTool().test(stack)) {
					if (!level.isClientSide()) {
						int fortune = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
						for (ItemStack drop : recipe.rollResults(level.random, fortune)) {
							ItemUtils.spawnItemEntity(level, drop.copy(),
									worldPosition.getX() + 0.5F,
									worldPosition.getY() + 0.2,
									worldPosition.getZ() + 0.5F,
									0, 0, 0);
						}
						model = null;
						contents.clear();
						notifyTile();
					}
					return true;
				}
			}
		}
		return false;
	}

	public int addItem(ItemStack stack) {
		if (level == null) return 0;
		var prev = getModel();
		var ans = prev.find(level, stack);
		if (ans.isPresent()) {
			int count = ans.get().getCost(prev);
			if (!stack.isEmpty() && count > stack.getCount()) return 0;
			if (level.isClientSide()) return count;
			contents.add(stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(count));
			model = ans.get();
			var transform = model.doTransform();
			if (transform.isPresent()) {
				contents.clear();
				contents.add(transform.get());
			}
			notifyTile();
			return count;
		}
		return 0;
	}

	public boolean addToPlayer(Player player) {
		if (level == null) return false;
		var opt = getModel().complete(level);
		if (opt.isPresent()) {
			if (level.isClientSide()) return true;
			var ans = opt.get();
			player.getInventory().placeItemBackInInventory(ans);
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

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		model = null;
	}

	public TableItem getModel() {
		if (model != null) return model;
		var ans = TableItemManager.TABLE.find(level, contents);
		if (ans.right().isPresent()) {
			var pair = ans.right().get();
			contents.clear();
			contents.addAll(pair.getSecond());
			setChanged();
		}
		model = ans.map(l -> l, Pair::getFirst);
		return model;
	}

	public void clear() {
		model = null;
		contents.clear();
		notifyTile();
	}

	@Override
	public List<Ingredient> getHints(Level level) {
		return model.getHints(level);
	}

}
