package dev.xkmc.youkaishomecoming.content.pot.table.recipe;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class OrderedCuisineRecipe extends BaseCuisineRecipe<OrderedCuisineRecipe> {

	@SerialField
	public final List<Ingredient> input = new ArrayList<>();

	public OrderedCuisineRecipe() {
		super(YHBlocks.CUISINE_ORDER.get());
	}

	@Override
	public List<Ingredient> getCustomIngredients() {
		return input;
	}

	@Override
	public boolean matches(CuisineInv inv, Level level) {
		if (!inv.base().equals(base)) return false;
		if (inv.size() > input.size()) return false;
		int n = Math.min(inv.size(), input.size());
		for (int i = 0; i < n; i++) {
			if (!input.get(i).test(inv.getItem(i))) {
				return false;
			}
		}
		return input.size() == inv.size() || !inv.isComplete();
	}

	@Override
	public List<Ingredient> getHints(Level level, CuisineInv inv) {
		int n = Math.min(inv.size(), input.size());
		for (int i = 0; i < n; i++) {
			if (!input.get(i).test(inv.getItem(i))) {
				return List.of(input.get(i));
			}
		}
		if (n < input.size())
			return List.of(input.get(n));
		return List.of();
	}

}
