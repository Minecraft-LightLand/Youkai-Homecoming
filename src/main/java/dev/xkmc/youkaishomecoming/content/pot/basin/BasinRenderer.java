package dev.xkmc.youkaishomecoming.content.pot.basin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

	private final ItemRenderer itemRenderer;

	public BasinRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(BasinBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = be.items.getItem(0);
		int i = (int) be.getBlockPos().asLong();
		int n = Math.min(8, stack.getCount());
		for (int j = 0; j < n; ++j) {
			ItemStack item = stack.copyWithCount(stack.getCount() / n);
			if (!item.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5F, 3f / 16, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(j * 45));
				pose.mulPose(Axis.XP.rotationDegrees(70.0F));
				float dist = 3f / 16;
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
				pose.popPose();
			}
		}
		FluidStack fluid = be.fluids.getFluidInTank(0);
		if (!fluid.isEmpty()) {
			float h = 8f / 16 * fluid.getAmount() / be.fluids.getTankCapacity(0);
			FluidRenderer.renderFluidBox(fluid, 1f / 16, 1f / 16, 1f / 16,
					15f / 16, 1f / 16 + h, 15f / 16,
					buffer, pose, light, false, 0);
		}
	}

}
