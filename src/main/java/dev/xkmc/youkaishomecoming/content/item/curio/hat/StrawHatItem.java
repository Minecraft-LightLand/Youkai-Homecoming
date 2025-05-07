package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.client.HatModel;
import dev.xkmc.youkaishomecoming.content.client.SuwakoHatModel;
import dev.xkmc.youkaishomecoming.content.item.food.IFleshFoodItem;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class StrawHatItem extends TouhouHatItem {

	public StrawHatItem(Item.Properties properties) {
		super(properties, TouhouMat.STRAW_HAT);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new HatModel(SuwakoHatModel.STRAW));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/models/suwako_hat.png";
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!(target instanceof Frog frog))
			return InteractionResult.PASS;
		if (!EffectEventHandlers.isYoukai(player))
			return InteractionResult.FAIL;
		if (!FrogGodCapability.HOLDER.isProper(frog))
			return InteractionResult.FAIL;
		var cap = FrogGodCapability.HOLDER.get(frog);
		if (cap.hasHat) {
			return InteractionResult.FAIL;
		} else {
			if (player instanceof ServerPlayer sp) {
				cap.hasHat = true;
				cap.syncToClient(frog);
				YHCriteriaTriggers.SUWAKO_WEAR.trigger(sp);
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		Player player = IFleshFoodItem.getPlayer();
		if (player == null) return;
		boolean obtain = showTooltip();
		Component obt;
		if (obtain) {
			var fying = Component.translatable(YHEffects.YOUKAIFYING.get().getDescriptionId());
			var fied = Component.translatable(YHEffects.YOUKAIFIED.get().getDescriptionId());
			obt = YHLangData.USAGE_STRAW_HAT.get(fying, fied);
		} else {
			obt = YHLangData.UNKNOWN.get();
		}
		list.add(YHLangData.USAGE.get().append(obt));
	}

}
