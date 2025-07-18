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
public class UnorderedCuisineRecipe extends BaseCuisineRecipe<UnorderedCuisineRecipe> {

	@SerialClass.SerialField
	public final List<Ingredient> input = new ArrayList<>();

	public UnorderedCuisineRecipe(ResourceLocation id) {
		super(id, YHBlocks.CUISINE_UNORDER.get());
	}

	@Override
	public List<Ingredient> getCustomIngredients() {
		return input;
	}

	@Override
	public boolean matches(CuisineInv inv, Level level) {
		if (!inv.base().equals(base)) return false;
		if (inv.getContainerSize() > input.size()) return false;
		List<Ingredient> remain = new ArrayList<>(input);
		for (int i = 0; i < inv.getContainerSize(); i++) {
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

	@Override
	public List<Ingredient> getHints(Level level, CuisineInv inv) {
		List<Ingredient> remain = new ArrayList<>(input);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			var itr = remain.iterator();
			while (itr.hasNext()) {
				var ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					break;
				}
			}
		}
		return remain;
	}

}
