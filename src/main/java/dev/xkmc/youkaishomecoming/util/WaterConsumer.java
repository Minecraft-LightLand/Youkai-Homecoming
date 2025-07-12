package dev.xkmc.youkaishomecoming.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WaterConsumer {

	public static boolean isWaterContainer(ItemStack stack, int minAmount) {
		if (stack.is(Items.WATER_BUCKET)) return minAmount <= 1000;
		if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) return minAmount <= 250;
		var opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
		if (opt.isEmpty()) return false;
		var cap = opt.get();
		var fluid = new FluidStack(Fluids.WATER, minAmount);
		return cap.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() >= minAmount;
	}

	public static ItemStack drainWater(ItemStack stack, int minAmount) {
		if (stack.is(Items.WATER_BUCKET))
			return Items.BUCKET.getDefaultInstance();
		if (stack.is(Items.POTION))
			return Items.GLASS_BOTTLE.getDefaultInstance();
		var opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
		if (opt.isEmpty()) return stack;
		var cap = opt.get();
		var fluid = new FluidStack(Fluids.WATER, minAmount);
		cap.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
		return cap.getContainer();
	}

}
