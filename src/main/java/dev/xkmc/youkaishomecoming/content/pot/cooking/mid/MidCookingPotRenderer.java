package dev.xkmc.youkaishomecoming.content.pot.cooking.mid;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingRenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class MidCookingPotRenderer implements BlockEntityRenderer<MidCookingPotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public MidCookingPotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(MidCookingPotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		CookingRenderUtil.render(level, itemRenderer, be, pTick, pose, buffer, light, overlay,
				5f / 16, 4f / 16, 1f / 16);
	}

}
