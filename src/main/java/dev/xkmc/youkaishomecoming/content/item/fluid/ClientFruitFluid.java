package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public record ClientFruitFluid(SakeFluidType type) implements IClientFluidTypeExtensions {
	public ClientFruitFluid(SakeFluidType type) {
		this.type = type;
	}

	public ResourceLocation getStillTexture() {
		return this.type.stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return this.type.flowingTexture;
	}

	public int getTintColor() {
		return this.type.type.getColor();
	}

	public SakeFluidType type() {
		return this.type;
	}
}
