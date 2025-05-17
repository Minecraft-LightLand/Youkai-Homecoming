package dev.xkmc.youkaishomecoming.content.pot.table.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class CuisineTableRenderer implements BlockEntityRenderer<CuisineTableBlockEntity> {

	@Override
	public void render(CuisineTableBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		var model = be.getModel();
		var manager = Minecraft.getInstance().getModelManager();
		for (var m : model.getModels()) {

		}

	}

}
