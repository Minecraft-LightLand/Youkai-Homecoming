package dev.xkmc.youkaishomecoming.content.pot.storage.bottle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottledDrinkSet;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;

public class SauceRackRenderer implements BlockEntityRenderer<SauceRackBlockEntity> {

	private static final RandomSource RANDOM = RandomSource.create(42);

	public SauceRackRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(SauceRackBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(Axis.YP.rotationDegrees(-be.getBlockState().getValue(BlockProxy.HORIZONTAL_FACING).toYRot()));
		for (int i = 0; i < 3; i++) {
			var stack = be.items.getItem(i);
			var bottle = stack.getItem();
			if (bottle instanceof SlipBottleItem) {
				var fluid = SlipBottleItem.getFluid(stack);
				if (fluid.getFluid() instanceof YHFluid type) {
					if (type.type.bottleSet() instanceof BottledDrinkSet set) {
						bottle = set.bottle.asItem();
					}
				}
			}
			if (bottle instanceof BlockItem item) {
				pose.pushPose();
				int x = i % 3 - 1;
				pose.translate(x * 5f / 16 , 1f / 16, -5f / 16);
				pose.mulPose(Axis.YP.rotationDegrees(180));
				pose.translate(- 8f / 16, 0, -8f / 16);
				var state = item.getBlock().defaultBlockState();
				BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
				ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
				PoseStack.Pose mat = pose.last();
				RANDOM.setSeed(42);
				for (RenderType rt : model.getRenderTypes(state, RANDOM, ModelData.EMPTY)) {
					renderer.renderModel(mat, buffer.getBuffer(ForgeHooksClient.getEntityRenderType(rt, false)),
							state, model, 1F, 1F, 1F, light, overlay, ModelData.EMPTY, rt);
				}
				pose.popPose();
			}
		}
		pose.popPose();
	}

}
