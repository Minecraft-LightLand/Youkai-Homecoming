
package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.content.client.SuwakoHatModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class SuwakoHatItem extends ArmorItem {

	public static class Mat implements ArmorMaterial {
		private static final int[] DURABILITY = {13, 15, 16, 11};
		private static final int[] DEFENSE = {2, 5, 6, 3};

		private static final Mat INS = new Mat();

		public int getDurabilityForType(ArmorItem.Type type) {
			return DURABILITY[type.getSlot().getIndex()] * 30;
		}

		public int getDefenseForType(ArmorItem.Type type) {
			return DEFENSE[type.getSlot().getIndex()];
		}

		public int getEnchantmentValue() {
			return 10;
		}

		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_LEATHER;
		}

		public Ingredient getRepairIngredient() {
			return Ingredient.of(Items.WHEAT.getDefaultInstance());
		}

		public String getName() {
			return "suwako_hat";
		}

		public float getToughness() {
			return 0.0F;
		}

		public float getKnockbackResistance() {
			return 0.0F;
		}

	}

	public SuwakoHatItem(Item.Properties properties) {
		super(Mat.INS, Type.HELMET, properties);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
				EntityModelSet models = Minecraft.getInstance().getEntityModels();
				ModelPart root = models.bakeLayer(SuwakoHatModel.LAYER_LOCATION);
				return new HumanoidModel<>(root);
			}
		});
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/models/suwako_hat.png";
	}

	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.NATIVE.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}
}
