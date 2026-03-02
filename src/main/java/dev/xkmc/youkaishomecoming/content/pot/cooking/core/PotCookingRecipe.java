package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

@SerialClass
public abstract class PotCookingRecipe<T extends PotCookingRecipe<T>> extends BaseRecipe<T, PotCookingRecipe<?>, CookingInv>
		implements TimedRecipe {

	@SerialField
	protected ItemStack result = ItemStack.EMPTY;
	@SerialField
	protected int time;

	public PotCookingRecipe(RecType<T, PotCookingRecipe<?>, CookingInv> fac) {
		super(fac);
	}

	public abstract List<Ingredient> getInput();

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public boolean matches(CookingInv inv, Level level) {
		return matchContainer(inv.container());
	}

	public boolean matchContainer(Item item) {
		return result.hasCraftingRemainingItem() && result.getCraftingRemainingItem().is(item);
	}

	@Override
	public ItemStack assemble(CookingInv cookingInv, HolderLookup.Provider registryAccess) {
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return result;
	}

	public ItemStack getResult() {
		return result;
	}

	public abstract List<Ingredient> getHints(Level level, CookingInv cont);

	public int getProcessTime() {
		return time;
	}

}
