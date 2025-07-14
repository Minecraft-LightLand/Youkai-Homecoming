package dev.xkmc.youkaishomecoming.content.pot.cooking.mid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class MidCookingPotRenderer implements BlockEntityRenderer<MidCookingPotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public MidCookingPotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(MidCookingPotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		int seed = (int) be.getBlockPos().asLong();
		var rand = new Random(new Random(seed).nextLong());
		float h = 0;

		var tex = be.getSoupCache().withPrefix("block/bowl/soup/");
		FluidRenderer.renderFluidBox(tex, 5f / 16, 2f / 16, 5f / 16,
				11f / 16, 3f / 16, 11f / 16,
				buffer, pose, light, false, -1);

		float time = (pTick + (int) (level.getGameTime() % 80L)) / 80;
		float p = (float) Math.sin(time / 80 * 2 * Math.PI);

		h = 4 / 16f + 1 / 32f * p;

		List<ItemStack> toRender = be.getFloatingItems();
		if (toRender.isEmpty()) return;
		float fan = 360f / toRender.size();
		for (int i = 0; i < toRender.size(); ++i) {
			ItemStack item = toRender.get(i);
			if (!item.isEmpty()) {
				float pr1 = (float) Math.sin(time * 2 * Math.PI + rand.nextFloat());
				float pr2 = (float) Math.cos(time * 2 * Math.PI + rand.nextFloat() * 2 * Math.PI);
				pose.pushPose();
				pose.translate(0.5F, h, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(i * fan + rand.nextFloat() * 10 * (h >= 0.25f ? pr1 : 1)));
				pose.mulPose(Axis.XP.rotationDegrees(70));
				float dist = 1f / 32f * (1f + rand.nextFloat() * 0.25f + 0.25f * pr2);
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), seed + i);
				pose.popPose();
			}
		}
	}

}
