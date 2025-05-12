package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy;

import com.github.tartaricacid.touhoulittlemaid.client.model.NewEntityFairyModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class SmallFairyRenderer extends MobRenderer<SmallFairy, SmallFairyModel> {

	private static final ResourceLocation[] TEXTURES;
	private static final ResourceLocation TEXTURE_RICK = new ResourceLocation("touhou_little_maid", "textures/entity/new_maid_fairy/maid_fairy_rick.png");

	static {
		TEXTURES = new ResourceLocation[18];
		for (int i = 0; i < 18; i++) {
			TEXTURES[i] = new ResourceLocation("touhou_little_maid", "textures/entity/new_maid_fairy/maid_fairy_" + i + ".png");
		}
	}

	public SmallFairyRenderer(EntityRendererProvider.Context context) {
		super(context, new SmallFairyModel(context.bakeLayer(NewEntityFairyModel.LAYER)), 0.5F);
	}

	protected void setupRotations(SmallFairy fairy, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(fairy, poseStack, ageInTicks, rotationYaw, partialTicks);
		if (!fairy.onGround()) {
			poseStack.mulPose(Axis.XN.rotation(0.13962634F));
		}
	}

	public ResourceLocation getTextureLocation(SmallFairy entity) {
		String name = entity.getName().getString().toLowerCase(Locale.ENGLISH);
		if ("rick".equals(name)) {
			return TEXTURE_RICK;
		}
		var model = entity.getModelId();
		if (model.startsWith("fairy:")) {
			try {
				return TEXTURES[Integer.parseUnsignedInt(model.substring(6))];
			} catch (Exception ignored) {
			}
		}
		return TEXTURES[0];
	}
}
