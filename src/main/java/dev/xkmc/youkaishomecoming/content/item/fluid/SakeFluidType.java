package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.youkaishomecoming.init.food.YHSake;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class SakeFluidType extends FluidType {
    final ResourceLocation stillTexture;
    final ResourceLocation flowingTexture;
    final YHSake type;

    public SakeFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, YHSake type) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.type = type;
    }

    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new ClientFruitFluid(this));
    }
}
