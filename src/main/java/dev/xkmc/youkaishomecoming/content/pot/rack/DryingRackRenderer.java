package dev.xkmc.youkaishomecoming.content.pot.rack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	private static final float SIZE = 0.375F;
	private final ItemRenderer itemRenderer;

	public DryingRackRenderer(BlockEntityRendererProvider.Context pContext) {
		this.itemRenderer = pContext.getItemRenderer();
	}

	public void render(DryingRackBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		Direction dir = be.getBlockState().getValue(DryingRackBlock.FACING);
		NonNullList<ItemStack> list = be.getItems();
		int i = (int) be.getBlockPos().asLong();

		for (int j = 0; j < list.size(); ++j) {
			ItemStack stack = list.get(j);
			if (!stack.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5F, 0.125F, 0.5F);
				Direction rot = Direction.from2DDataValue((j + dir.get2DDataValue()) % 4);
				float f = -rot.toYRot();
				pose.mulPose(Axis.YP.rotationDegrees(f));
				pose.mulPose(Axis.XP.rotationDegrees(90.0F));
				pose.translate(-0.3125F, -0.3125F, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
				pose.popPose();
			}
		}

	}
}