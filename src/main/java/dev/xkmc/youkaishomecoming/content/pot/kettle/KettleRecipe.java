package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SerialClass
public class KettleRecipe extends BaseRecipe<KettleRecipe, KettleRecipe, SimpleContainer> implements TimedRecipe {

	@SerialClass.SerialField
	public final List<Ingredient> input = new ArrayList<>();
	@SerialClass.SerialField
	public int time;
	@SerialClass.SerialField
	public FluidStack result;

	public KettleRecipe(ResourceLocation id) {
		super(id, YHBlocks.KETTLE_RS.get());
	}

	@Override
	public ItemStack getToastSymbol() {
		return YHBlocks.KETTLE.asStack();
	}

	@Override
	public boolean matches(SimpleContainer cont, Level level) {
		Set<ItemStack> available = new LinkedHashSet<>();
		for (int i = 0; i < cont.getContainerSize(); i++) {
			var e = cont.getItem(i);
			if (e.isEmpty()) continue;
			available.add(e);
		}
		for (var ing : input) {
			ItemStack match = null;
			for (var e : available) {
				if (ing.test(e)) {
					match = e;
					break;
				}
			}
			if (match == null) {
				return false;
			}
			available.remove(match);
		}
		return available.isEmpty();
	}

	@Override
	public ItemStack assemble(SimpleContainer cont, RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getProcessTime() {
		return time;
	}

}
