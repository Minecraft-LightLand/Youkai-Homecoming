package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class MixedCuisineRecipe extends BaseCuisineRecipe<MixedCuisineRecipe> {

	@SerialClass.SerialField
	public final List<Ingredient> first = new ArrayList<>();
	@SerialClass.SerialField
	public final List<Ingredient> second = new ArrayList<>();

	public MixedCuisineRecipe(ResourceLocation id) {
		super(id, YHBlocks.CUISINE_MIXED.get());
	}

	@Override
	public List<Ingredient> getCustomIngredients() {
		List<Ingredient> ans = new ArrayList<>();
		ans.addAll(first);
		ans.addAll(second);
		return ans;
	}

	@Override
	public boolean matches(CuisineInv inv, Level level) {
		if (!inv.base().equals(base)) return false;
		if (inv.getContainerSize() > first.size() + second.size()) return false;

		int n = Math.min(inv.getContainerSize(), first.size());
		for (int i = 0; i < n; i++) {
			if (!first.get(i).test(inv.getItem(i))) {
				return false;
			}
		}
		if (n < first.size()) return !inv.isComplete();

		List<Ingredient> remain = new ArrayList<>(second);
		for (int i = n; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			var itr = remain.iterator();
			boolean match = false;
			while (itr.hasNext()) {
				var ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					match = true;
					break;
				}
			}
			if (!match) return false;
		}
		return !inv.isComplete() || remain.isEmpty();
	}

}
