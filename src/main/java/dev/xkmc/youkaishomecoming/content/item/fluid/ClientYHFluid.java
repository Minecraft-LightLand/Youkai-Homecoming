package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public record ClientYHFluid(YHFluidType type) implements IClientFluidTypeExtensions {

	public ClientYHFluid(YHFluidType type) {
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

	public YHFluidType type() {
		return this.type;
	}
}
