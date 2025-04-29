package dev.xkmc.youkaishomecoming.content.pot.tank;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;

public class CopperFaucetRenderer implements BlockEntityRenderer<CopperFaucetBlockEntity> {

	public CopperFaucetRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(CopperFaucetBlockEntity entity, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		FluidStack fluid = entity.cache;
		if (!fluid.isEmpty()) {
			FluidRenderer.renderFluidBox(fluid, 6f / 16, -8f / 16, 6f / 16,
					10f / 16, 4f / 16, 10f / 16,
					buffer, pose, light, false, 0);
		}
	}

}
