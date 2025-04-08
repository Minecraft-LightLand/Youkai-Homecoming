package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.util.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;

public class SteamerBlockRenderer implements BlockEntityRenderer<SteamerBlockEntity> {

	private static final RandomSource RANDOM = RandomSource.create(42);

	private final ItemRenderer itemRenderer;

	public SteamerBlockRenderer(BlockEntityRendererProvider.Context pContext) {
		this.itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(SteamerBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		RackInfo info = RackInfo.getRackInfo(be.getBlockState());
		if (info.pot() && be.getBlockState().getValue(SteamerStates.WATER)) {
			FluidRenderer.renderWaterBox(4 / 16f, 1 / 16f, 4 / 16f, 12 / 16f, 8.01f / 16f, 12 / 16f, buffer, pose, light, 0);
		}
		if (info.racks() == 0 || be.racks.isEmpty() || info.racks() > be.racks.size()) return;
		RackData rack = be.racks.get(info.racks() - 1);
		if (rack.list[0] != null && rack.list[0].stack.getItem() instanceof FoodSaucerItem item) {
			pose.pushPose();
			BlockState state = item.getBlock().defaultBlockState();
			state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
			BakedModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
			pose.translate(.5f, (info.height() * 4 - 3) / 16f, .5f);
			FoodSaucerBlock block = (FoodSaucerBlock) item.getBlock();
			var saucer = block.dish.saucer;
			int width = 16 - Math.min(saucer.x, saucer.z) * 2;
			float s = 8f / width;
			pose.scale(s, s, s);
			pose.translate(-.5f, 0, -.5f);
			ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
			PoseStack.Pose mat = pose.last();
			RANDOM.setSeed(42);
			for (RenderType rt : model.getRenderTypes(state, RANDOM, ModelData.EMPTY)) {
				renderer.renderModel(mat, buffer.getBuffer(ForgeHooksClient.getEntityRenderType(rt, false)),
						state, model, 1F, 1F, 1F, light, overlay, ModelData.EMPTY, rt);
			}
			pose.popPose();
			return;
		}
		int i = (int) be.getBlockPos().asLong();
		for (int j = 0; j < rack.list.length; ++j) {
			var data = rack.list[j];
			if (data == null) continue;
			ItemStack stack = data.stack;
			if (stack.isEmpty()) continue;
			pose.pushPose();
			pose.translate(0.5F, (info.height() * 4 - 2.8) / 16f, 0.5F);
			Direction rot = Direction.from2DDataValue(j % 4);
			pose.mulPose(Axis.YP.rotationDegrees(-rot.toYRot()));
			pose.mulPose(Axis.XP.rotationDegrees(90.0F));
			float dist = 2f / 16;
			float scale = 5f / 16;
			pose.translate(-dist, -dist, 0.0F);
			pose.scale(scale, scale, scale);
			this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
			pose.popPose();
		}
	}

}
