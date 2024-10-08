package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SerialClass
public class SimpleFermentationRecipe extends FermentationRecipe<SimpleFermentationRecipe> {

	@SerialField
	public ArrayList<Ingredient> ingredients = new ArrayList<>();

	@SerialField
	public ArrayList<ItemStack> results = new ArrayList<>();

	@SerialField
	public FluidStack outputFluid = FluidStack.EMPTY;

	@SerialField
	public FluidIngredient inputFluid = FluidIngredient.empty();

	@SerialField
	public int time;

	public SimpleFermentationRecipe() {
		super(YHBlocks.FERMENT_RS.get());
	}

	@Override
	public boolean matches(FermentationDummyContainer cont, Level level) {
		FluidStack fluid = cont.fluids().getFluidInTank(0);
		if (!inputFluid.isEmpty()) {
			if (inputFluid.test(fluid)) {
				return false;
			}
		}
		Set<ItemStack> available = new LinkedHashSet<>();
		for (var e : cont.items().getAsList()) {
			if (e.isEmpty()) continue;
			available.add(e);
		}
		for (var ing : ingredients) {
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
	public ItemStack assemble(FermentationDummyContainer cont, HolderLookup.Provider access) {
		List<ItemStack> remain = new ArrayList<>();
		for (var e : cont.items().getAsList()) {
			ItemStack rem = e.getCraftingRemainingItem();
			if (!rem.isEmpty()) {
				remain.add(rem);
			}
		}
		cont.items().clearContent();
		for (var e : results) {
			cont.items().addItem(e.copy());
		}
		for (var e : remain) {
			cont.items().addItem(e);
		}
		FluidStack ans = outputFluid.copy();
		ans.setAmount(cont.fluids().getFluidInTank(0).getAmount());
		cont.fluids().clear();
		cont.fluids().set(0, 0, ans);
		return ItemStack.EMPTY;
	}

	@Override
	public int getFermentationTime() {
		return time;
	}
}
