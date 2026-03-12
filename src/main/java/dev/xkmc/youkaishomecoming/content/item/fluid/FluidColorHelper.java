package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidColorHelper {

	public static int getColor(FluidStack fluidStack) {
		if (YHFluidHandler.of(fluidStack) instanceof IYHFluidItem item) {
			return item.getColor();
		}
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
		FluidType fluidAttributes = fluid.getFluidType();
		TextureAtlasSprite fluidTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(clientFluid.getStillTexture(fluidStack));
		return clientFluid.getTintColor(fluidStack);
	}

}
