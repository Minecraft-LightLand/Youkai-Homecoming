package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record TileTooltip(List<ItemStack> items, List<FluidStack> fluids) implements TooltipComponent {

}
