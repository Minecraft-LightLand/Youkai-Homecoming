package dev.xkmc.youkaishomecoming.content.pot.cooking.large;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingRenderUtil;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class LargeCookingPotRenderer implements BlockEntityRenderer<LargeCookingPotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public LargeCookingPotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(LargeCookingPotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		CookingRenderUtil.render(level, itemRenderer, be, pTick, pose, buffer, light, overlay,
				14f / 16, 3f / 16, 1f / 8);
	}

}
