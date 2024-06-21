package dev.xkmc.youkaishomecoming.content.client;

import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public record CirnoHairbandModel(ModelLayerLocation loc) implements IClientItemExtensions {

	public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		ModelPart root = models.bakeLayer(loc);
		root.getAllParts().forEach(e -> e.skipDraw = true);
		var head = root.getChild("head");
		head.getAllParts().forEach(e -> e.skipDraw = false);
		head.skipDraw = true;
		return new HumanoidModel<>(root);
	}

}
