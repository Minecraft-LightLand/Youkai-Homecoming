package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class SmallFairyModel extends EntityModel<SmallFairy> {
	private final ModelPart head;
	private final ModelPart blink;
	private final ModelPart armLeft;
	private final ModelPart body;
	private final ModelPart legLeft;
	private final ModelPart legRight;
	private final ModelPart wingLeft;
	private final ModelPart wingRight;
	private final ModelPart armRight;

	public SmallFairyModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.head = root.getChild("head");
		this.armRight = root.getChild("armRight");
		this.armLeft = root.getChild("armLeft");
		this.body = root.getChild("body");
		this.legLeft = root.getChild("legLeft");
		this.legRight = root.getChild("legRight");
		this.wingLeft = root.getChild("wingLeft");
		this.wingRight = root.getChild("wingRight");
		this.blink = this.head.getChild("blink");
	}

	public void setupAnim(SmallFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.armLeft.zRot = Mth.cos(ageInTicks * 0.05F) * 0.05F - 0.4F;
		this.armRight.zRot = -Mth.cos(ageInTicks * 0.05F) * 0.05F + 0.4F;
		if (entityIn.onGround()) {
			this.legLeft.xRot = Mth.cos(limbSwing * 0.67F) * 0.3F * limbSwingAmount;
			this.legRight.xRot = -Mth.cos(limbSwing * 0.67F) * 0.3F * limbSwingAmount;
			this.armLeft.xRot = -Mth.cos(limbSwing * 0.67F) * 0.7F * limbSwingAmount;
			this.armRight.xRot = Mth.cos(limbSwing * 0.67F) * 0.7F * limbSwingAmount;
			this.wingLeft.yRot = -Mth.cos(ageInTicks * 0.3F) * 0.2F + 1.0F;
			this.wingRight.yRot = Mth.cos(ageInTicks * 0.3F) * 0.2F - 1.0F;
		} else {
			this.legLeft.xRot = 0.0F;
			this.legRight.xRot = 0.0F;
			this.armLeft.xRot = -0.17453292F;
			this.armRight.xRot = -0.17453292F;
			this.head.xRot -= 0.13962634F;
			this.wingLeft.yRot = -Mth.cos(ageInTicks * 0.5F) * 0.4F + 1.2F;
			this.wingRight.yRot = Mth.cos(ageInTicks * 0.5F) * 0.4F - 1.2F;
		}

		float remainder = ageInTicks % 60.0F;
		this.blink.visible = 55.0F < remainder && remainder < 60.0F;
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.wingLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.wingRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
