package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.table.food.FoodModelHelper;
import dev.xkmc.youkaishomecoming.content.pot.table.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SerialClass
public abstract class CuisineRecipe<T extends CuisineRecipe<T>> extends BaseRecipe<T, CuisineRecipe<?>, CuisineInv> {

	public CuisineRecipe(ResourceLocation id, RecType<T, CuisineRecipe<?>, CuisineInv> fac) {
		super(id, fac);
	}

	public abstract ResourceLocation base();

	public abstract List<Ingredient> getCustomIngredients();

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	public abstract ItemStack getResult();

	public Optional<TableItem> recreate() {
		List<ItemStack> list = new ArrayList<>();
		for (var e : getCustomIngredients()) {
			list.add(e.getItems()[0]);
		}
		var base = VariantTableItemBase.MAP.get(base());
		if (base != null) {
			return Optional.of(new VariantTableItem(base, list, getResult().getCount()));
		}
		var fix = IngredientTableItem.FIXED.get(base());
		if (fix != null) {
			return Optional.of(fix);
		}
		var item = ForgeRegistries.ITEMS.getValue(base());
		if (item == null || item == Items.AIR) return Optional.empty();
		var init = item.getDefaultInstance();
		var holder = FoodModelHelper.find(init);
		if (holder == null) return Optional.empty();
		return Optional.of(new FoodTableItem(holder.base(), item.getDefaultInstance(), holder, list));

	}

	public abstract List<Ingredient> getHints(Level level, CuisineInv cont);

}
