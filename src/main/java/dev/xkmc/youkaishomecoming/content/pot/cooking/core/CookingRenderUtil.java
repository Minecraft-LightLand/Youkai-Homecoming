package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CookingRenderUtil {

	public static void render(
			Level level, ItemRenderer itemRenderer, CookingBlockEntity be,
			float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay,
			float height, float margin, float radius) {
		{
			var s1 = be.getSoup().getNextLayer(pTick);
			if (s1 != null) {
				var t1 = s1.id().withPrefix("block/bowl/soup/");
				margin += 0.001f;
				height -= 0.001f;
				FluidRenderer.renderFluidBox(t1, margin, 2f / 16, margin,
						1 - margin, height, 1 - margin,
						buffer, pose, light, false, s1.color());
			}
		}
		{
			var s0 = be.getSoup().getCurrentLayer(pTick);
			var t0 = s0.id().withPrefix("block/bowl/soup/");
			margin -= 0.001f;
			height += 0.001f;
			FluidRenderer.renderFluidBox(t0, margin, 2f / 16, margin,
					1 - margin, height, 1 - margin,
					buffer, pose, light, false, s0.color());
		}

		float time = (pTick + (int) (level.getGameTime() % 80L)) / 80;
		float p = (float) Math.sin(time / 80 * 2 * Math.PI);
		float h = height + 1 / 16f + 1 / 32f * p;
		int seed = (int) be.getBlockPos().asLong();
		var rand = new Random(new Random(seed).nextLong());
		var toRender = be.getSoup().floatingItems;
		if (toRender.isEmpty()) return;
		float prog = be.getSoup().getProgress(pTick);
		float total = 0;
		for (var e : toRender)
			total += e.getAmount(prog);
		if (total < 0.01) return;
		float step = 0;
		for (int i = 0; i < toRender.size(); i++) {
			var entry = toRender.get(i);
			float amount = entry.getAmount(prog);
			float pr1 = (float) Math.sin(time * 2 * Math.PI + rand.nextFloat());
			float pr2 = (float) Math.cos(time * 2 * Math.PI + rand.nextFloat() * 2 * Math.PI);
			float angle = 360f * step / total + rand.nextFloat() * 10 * pr1;
			float dist = radius * (1f + rand.nextFloat() * 0.25f + 0.25f * pr2);
			step += amount;
			var item = entry.stack();
			if (!item.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5F, h - (1 - amount) * 2f / 16, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(angle));
				pose.mulPose(Axis.XP.rotationDegrees(70));
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), seed + i);
				pose.popPose();
			}
		}
	}
}
