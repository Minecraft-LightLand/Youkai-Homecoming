package dev.xkmc.youkaishomecoming.content.pot.table.board;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2modularblock.BlockProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;

public class CuisineBoardRenderer implements BlockEntityRenderer<CuisineBoardBlockEntity> {

	public CuisineBoardRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(CuisineBoardBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		var list = be.getModel().getModels();
		if (list.isEmpty()) return;
		var manager = Minecraft.getInstance().getModelManager();
		var vc = buffer.getBuffer(RenderType.cutout());
		var renderer = Minecraft.getInstance().getItemRenderer();
		var dummy = be.getBlockState().getBlock().asItem().getDefaultInstance();
		var dir = be.getBlockState().getValue(BlockProxy.HORIZONTAL_FACING);
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(Axis.YP.rotation(dir.toYRot() * Mth.DEG_TO_RAD));
		pose.translate(-0.5, 0, -0.5);
		for (var id : list) {
			var model = manager.getModel(id);
			renderer.renderModelLists(model, dummy, light, overlay, pose, vc);
		}
		pose.popPose();
	}

}
