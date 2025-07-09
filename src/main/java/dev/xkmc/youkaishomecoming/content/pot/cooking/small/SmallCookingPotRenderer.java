package dev.xkmc.youkaishomecoming.content.pot.cooking.small;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmallCookingPotRenderer implements BlockEntityRenderer<SmallCookingPotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public SmallCookingPotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(SmallCookingPotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		//TODO
		var level = be.getLevel();
		if (level == null) return;
		int seed = (int) be.getBlockPos().asLong();
		var rand = new Random(new Random(seed).nextLong());
		float h = 0;
		/*FluidStack fluid = be.fluids.getFluidInTank(0);
		if (!fluid.isEmpty()) {
			h = 6f / 16 * fluid.getAmount() / be.fluids.getTankCapacity(0);
			FluidRenderer.renderFluidBox(fluid, 1f / 16, 1f / 16, 1f / 16,
					15f / 16, 1f / 16 + h, 15f / 16,
					buffer, pose, light, false, 0);
		}

		 */
		h = Math.max(h, 1f / 16) + 2f / 16;
		float time = (pTick + (int) (level.getGameTime() % 80L)) / 80;
		float p = (float) Math.sin(time / 80 * 2 * Math.PI);
		if (h >= 0.25f) {
			h = h + 1 / 32f * p;
		}
		List<ItemStack> toRender = new ArrayList<>();
		for (var e : be.items.getAsList()) {
			if (!e.isEmpty()) toRender.add(e);
		}
		for (int i = 0; i < toRender.size(); ++i) {
			ItemStack item = toRender.get(i);
			if (!item.isEmpty()) {
				float pr1 = (float) Math.sin(time * 2 * Math.PI + rand.nextFloat());
				float pr2 = (float) Math.cos(time * 2 * Math.PI + rand.nextFloat() * 2 * Math.PI);
				pose.pushPose();
				pose.translate(0.5F, h, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(i * 45 + rand.nextFloat() * 10 * (h >= 0.25f ? pr1 : 1)));
				pose.mulPose(Axis.XP.rotationDegrees(70));
				float dist = 1f / 16f * (1f + rand.nextFloat() * 0.5f + 0.25f * (h >= 0.25f ? pr2 : rand.nextFloat()));
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), seed + i);
				pose.popPose();
			}
		}
	}

}
