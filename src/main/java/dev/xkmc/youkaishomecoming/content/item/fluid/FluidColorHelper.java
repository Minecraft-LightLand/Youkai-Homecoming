package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public class FluidColorHelper {

	public static int getColor(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
		FluidType fluidAttributes = fluid.getFluidType();
		TextureAtlasSprite fluidTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(clientFluid.getStillTexture(fluidStack));
		return clientFluid.getTintColor(fluidStack);
	}

}
