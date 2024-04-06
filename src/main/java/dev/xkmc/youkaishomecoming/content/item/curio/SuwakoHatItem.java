
package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.client.SuwakoHatModel;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SuwakoHatItem extends TouhouHatItem {

	public SuwakoHatItem(Item.Properties properties) {
		super(properties, TouhouMat.SUWAKO_HAT);
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
	protected void tick(ItemStack stack, Level level, Player player) {
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.NATIVE.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		Player player = FleshFoodItem.getPlayer();
		if (player == null) return;
		boolean obtain = false;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			obtain = true;
		} else if (player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			obtain = true;
		}
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_SUWAKO_HAT.get(Component.literal("" + FrogGodCapability.COUNT))));
			list.add(YHLangData.USAGE.get().append(YHLangData.USAGE_SUWAKO_HAT.get(Component.translatable(YHEffects.NATIVE.get().getDescriptionId()))));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

}
