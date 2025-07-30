package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class UnorderedCookingRecipe extends PotCookingRecipe<UnorderedCookingRecipe> {

	@SerialClass.SerialField
	public final List<Ingredient> input = new ArrayList<>();

	public UnorderedCookingRecipe(ResourceLocation id) {
		super(id, YHBlocks.COOKING_UNORDER.get());
	}

	@Override
	public List<Ingredient> getInput() {
		return input;
	}

	@Override
	public boolean matches(CookingInv inv, Level level) {
		if (!super.matches(inv,level))return false;
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
	public List<Ingredient> getHints(Level level, CookingInv inv) {
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
