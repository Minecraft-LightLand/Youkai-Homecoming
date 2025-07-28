package dev.xkmc.youkaishomecoming.content.pot.cooking.soup;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SimpleSoupBaseRecipe extends SoupBaseRecipe<SimpleSoupBaseRecipe> {

	@SerialClass.SerialField
	public final List<Ingredient> input = new ArrayList<>();

	public SimpleSoupBaseRecipe(ResourceLocation id) {
		super(id, YHBlocks.IMMEDIATE_SOUP.get());
	}

	@Override
	public int getIngredientCount() {
		return input.size();
	}

	@Override
	public boolean matches(CookingInv inv, Level level) {
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
		return remain.isEmpty();
	}


	@Override
	public void removeConsumed(List<ItemStack> list) {
		List<Ingredient> remain = new ArrayList<>(input);
		for (int i = 0; i < list.size(); i++) {
			ItemStack stack = list.get(i);
			var itr = remain.iterator();
			while (itr.hasNext()) {
				var ing = itr.next();
				if (ing.test(stack)) {
					itr.remove();
					list.set(i, ItemStack.EMPTY);
					break;
				}
			}
		}
	}

}
