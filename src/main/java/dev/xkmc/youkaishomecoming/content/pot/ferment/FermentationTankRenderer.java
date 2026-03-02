package dev.xkmc.youkaishomecoming.content.pot.ferment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class FermentationTankRenderer implements BlockEntityRenderer<FermentationTankBlockEntity> {

	private final ItemRenderer itemRenderer;

	public FermentationTankRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(FermentationTankBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		List<ItemStack> list = be.items.getAsList();
		int i = (int) be.getBlockPos().asLong();

		for (int j = 0; j < list.size(); ++j) {
			ItemStack stack = list.get(j);
			if (!stack.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5F, 3f / 16, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(j * 45));
				pose.mulPose(Axis.XP.rotationDegrees(70.0F));
				float dist = 2f / 16;
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
				pose.popPose();
			}
		}
		FluidStack fluid = be.fluids.getFluidInTank(0);
		if (!fluid.isEmpty()) {
			float h = 12f / 16 * fluid.getAmount() / 1000;
			FluidRenderer.renderFluidBox(fluid, 3f / 16, 2f / 16, 3f / 16,
					13f / 16, 2f / 16 + h, 13f / 16,
					buffer, pose, light, false, 0);
		}
	}

}
