package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.util.JEIFluidStackRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record TileClientTooltip(List<ItemStack> items, List<FluidStack> fluids) implements ClientTooltipComponent {

	public TileClientTooltip(TileTooltip inv) {
		this(inv.items(), inv.fluids());
	}

	private int size() {
		return items().size() + fluids().size();
	}

	public int getHeight() {
		return 3 * 18 + 2;
	}

	public int getWidth(Font font) {
		return 3 * 18 + 2;
	}

	public void renderImage(Font font, int mx, int my, GuiGraphics g) {
		int w = (int) Math.ceil(Math.sqrt(size()));
		int n = 0;
		for (ItemStack stack : items) {
			if (stack.isEmpty()) continue;
			int y = my + n / w * 18 + 1;
			int x = mx + n % w * 18 + 1;
			renderSlot(font, x, y, g, stack);
			n++;
		}
		for (FluidStack stack : fluids) {
			if (stack.isEmpty()) continue;
			int y = my + n / w * 18 + 1;
			int x = mx + n % w * 18 + 1;
			if (stack.getFluid() instanceof SakeFluid sake) {
				renderSlot(font, x, y, g, sake.type.asStack(stack.getAmount() / sake.type.amount()));
			} else {
				renderSlot(font, x, y, g, stack);
			}
			n++;
		}

	}

	private void renderSlot(Font font, int x, int y, GuiGraphics g, ItemStack stack) {
		if (!stack.isEmpty()) {
			g.renderItem(stack, x + 1, y + 1, 0);
			g.renderItemDecorations(font, stack, x + 1, y + 1);
		}
	}

	private void renderSlot(Font font, int x, int y, GuiGraphics g, FluidStack stack) {
		if (!stack.isEmpty()) {
			g.pose().pushPose();
			g.pose().translate(x + 1, y + 1, 0);
			new JEIFluidStackRenderer().render(g, stack);
			g.pose().popPose();
		}
	}

}
