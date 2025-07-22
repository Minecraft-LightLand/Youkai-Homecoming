package dev.xkmc.youkaishomecoming.content.pot.cooking.small;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingRenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class SmallCookingPotRenderer implements BlockEntityRenderer<SmallCookingPotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public SmallCookingPotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(SmallCookingPotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		CookingRenderUtil.render(level, itemRenderer, be, pTick, pose, buffer, light, overlay,
				3f / 16, 5f / 16, 1f / 32);
	}

}
