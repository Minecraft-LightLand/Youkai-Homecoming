package dev.xkmc.youkaishomecoming.content.pot.storage.ingredient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.youkaishomecoming.util.BatchedItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IngredientRackRenderer implements BlockEntityRenderer<IngredientRackBlockEntity> {

	private static final int[] COUNT_HI = {0, 1, 2, 4, 6, 9, 13, 18, 24, 32};
	private static final int[] REV_HI = new int[32];
	private static final int[] COUNT_LO = {0, 1, 2, 3, 4, 6, 8, 10, 12, 16, 20, 24, 32, 40, 48, 56, 64};
	private static final int[] REV_LO = new int[64];

	static {
		int k = 0;
		for (int i = 0; i < 32; i++) {
			if (i > COUNT_HI[k]) k++;
			REV_HI[i] = k;
		}
		k = 0;
		for (int i = 0; i < 64; i++) {
			if (i > COUNT_LO[k]) k++;
			REV_LO[i] = k;
		}
	}

	private static final RandomSource RANDOM = RandomSource.create(42);

	public IngredientRackRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(IngredientRackBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(Axis.YP.rotationDegrees(-be.getBlockState().getValue(BlockTemplates.HORIZONTAL_FACING).toYRot()));
		List<BatchedItemRenderer> batches = new ArrayList<>(6);
		for (int i = 0; i < 6; i++) {
			ItemStack stack = be.items.getItem(i);
			boolean half = be.getBlockState().getValue(IngredientRackBlock.SUPPORT) == IngredientRackBlock.State.STACKED || i < 3;
			int count = getModelCount(stack, half);
			if (count <= 0) continue;
			pose.pushPose();
			int x = i % 3;
			int y = i / 3;
			pose.translate(x * 4f / 16 - 4f / 16, -y * 8f / 16 + 12f / 16, -5.5f / 16);
			int seed = stack.getItem().hashCode() + stack.getCount() + i * 64;
			RANDOM.setSeed(seed);
			BatchedItemRenderer batch = BatchedItemRenderer.create(itemRenderer, stack, be.getLevel(), ItemDisplayContext.FIXED, i, batches);
			batches.add(batch);
			for (int j = 0; j < count; ++j) {
				float r = (RANDOM.nextFloat() * 2.0F - 1.0F) * 0.03F;
				pose.pushPose();
				pose.translate(r, -0.05 - j * 0.003, -0.05 + j * (half ? 0.04 : 0.05));
				pose.mulPose(Axis.XP.rotationDegrees(-20 - j * 2f));
				pose.scale(0.25f, 0.25f, 0.25f);
				batch.render(pose, buffer, light, overlay);
				pose.popPose();
			}

			pose.popPose();

		}
		pose.popPose();
	}

	protected int getModelCount(ItemStack stack, boolean half) {
		int count = stack.getCount();
		return half ?
				count < REV_HI.length ? REV_HI[count] : 9 :
				count < REV_LO.length ? REV_LO[count] : 16;
	}

}
