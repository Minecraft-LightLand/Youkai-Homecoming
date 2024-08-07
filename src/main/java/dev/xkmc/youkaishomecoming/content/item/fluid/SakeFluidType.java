package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class SakeFluidType extends FluidType {
	final ResourceLocation stillTexture;
	final ResourceLocation flowingTexture;
	final IYHSake type;

	public SakeFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, IYHSake type) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.type = type;
	}

	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new ClientFruitFluid(this));
	}
}
