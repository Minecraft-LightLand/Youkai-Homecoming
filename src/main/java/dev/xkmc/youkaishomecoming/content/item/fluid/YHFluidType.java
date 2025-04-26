package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class YHFluidType extends FluidType {
	final ResourceLocation stillTexture;
	final ResourceLocation flowingTexture;
	final IYHFluidHolder type;

	public YHFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, IYHFluidHolder type) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.type = type;
	}

	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new ClientYHFluid(this));
	}
}
