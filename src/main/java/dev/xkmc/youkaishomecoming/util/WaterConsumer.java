package dev.xkmc.youkaishomecoming.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class WaterConsumer {

	public static boolean isWaterContainer(ItemStack stack, int minAmount) {
		if (stack.is(Items.WATER_BUCKET)) return minAmount <= 1000;
		if (stack.is(Items.POTION)) {
			var potion = stack.get(DataComponents.POTION_CONTENTS);
			if (potion != null && potion.is(Potions.WATER))
				return minAmount <= 250;
		}
		var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (cap == null) return false;
		var fluid = new FluidStack(Fluids.WATER, minAmount);
		return cap.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() >= minAmount;
	}

	public static ItemStack drainWater(ItemStack stack, int minAmount) {
		if (stack.is(Items.WATER_BUCKET))
			return Items.BUCKET.getDefaultInstance();
		if (stack.is(Items.POTION))
			return Items.GLASS_BOTTLE.getDefaultInstance();
		var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (cap == null) return stack;
		var fluid = new FluidStack(Fluids.WATER, minAmount);
		cap.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
		return cap.getContainer();
	}

}
