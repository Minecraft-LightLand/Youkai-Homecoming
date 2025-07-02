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

import java.util.Random;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {

	private final ItemRenderer itemRenderer;

	public BasinRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(BasinBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		ItemStack stack = be.items.getItem(0);
		int i = (int) be.getBlockPos().asLong();
		int n = Math.min(8, stack.getCount());
		var rand = new Random(new Random(i).nextLong());
		float h = 0;
		FluidStack fluid = be.fluids.getFluidInTank(0);
		if (!fluid.isEmpty()) {
			h = 6f / 16 * fluid.getAmount() / be.fluids.getTankCapacity(0);
			FluidRenderer.renderFluidBox(fluid, 1f / 16, 1f / 16, 1f / 16,
					15f / 16, 1f / 16 + h, 15f / 16,
					buffer, pose, light, false, 0);
		}
		h = Math.max(h, 1f / 16) + 2f / 16;
		float time = (pTick + (int) (level.getGameTime() % 80L)) / 80;
		float p = (float) Math.sin(time / 80 * 2 * Math.PI);
		if (h >= 0.25f) {
			h = h + 1 / 32f * p;
		}
		for (int j = 0; j < n; ++j) {
			ItemStack item = stack.copyWithCount(stack.getCount() / n);
			if (!item.isEmpty()) {
				float pr1 = (float) Math.sin(time * 2 * Math.PI + rand.nextFloat());
				float pr2 = (float) Math.cos(time * 2 * Math.PI + rand.nextFloat() * 2 * Math.PI);
				pose.pushPose();
				pose.translate(0.5F, h, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(j * 45 + rand.nextFloat() * 10 * (h >= 0.25f ? pr1 : 1)));
				pose.mulPose(Axis.XP.rotationDegrees(70));
				float dist = 1f / 16f * (2f + rand.nextFloat() * 0.75f + 0.25f * (h >= 0.25f ? pr2 : rand.nextFloat()));
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
				pose.popPose();
			}
		}
	}

}
