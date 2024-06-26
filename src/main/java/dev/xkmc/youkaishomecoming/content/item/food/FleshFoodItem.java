package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.events.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshFoodItem extends YHFoodItem {

	public FleshFoodItem(Properties props) {
		super(props);
	}

	@Nullable
	public static Player getPlayer() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return FleshHelper.getPlayerOnClient();
		}
		return null;
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		FoodProperties old = super.getFoodProperties(stack, entity);
		if (old == null) return null;
		int factor = 1;
		if (entity != null) {
			if (PlayerStatusData.Kind.YOUKAI.is(entity))
				factor++;
			if (PlayerStatusData.Status.YOUKAIFIED.is(entity))
				factor++;
		}
		var builder = new FoodProperties.Builder();
		builder.nutrition(old.getNutrition() * factor);
		builder.saturationMod(old.getSaturationModifier());
		if (old.canAlwaysEat()) builder.alwaysEat();
		if (old.isFastFood()) builder.fast();
		if (old.isMeat()) builder.meat();
		for (var ent : old.getEffects()) {
			if (!ent.getFirst().getEffect().isBeneficial() || factor > 1) {
				builder.effect(ent::getFirst, ent.getSecond());
			}
		}
		return builder.build();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		Player player = getPlayer();
		if (player == null) return;
		if (PlayerStatusData.Status.YOUKAIFIED.is(player))
			list.add(YHLangData.FLESH_TASTE_YOUKAI.get());
		else if (PlayerStatusData.Kind.YOUKAI.is(player))
			list.add(YHLangData.FLESH_TASTE_HALF_YOUKAI.get());
		else if (getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD)) {
			list.add(YHLangData.FLESH_TASTE_HUMAN.get());
		}
		if (this == YHFood.FLESH.item.get()) {
			boolean obtain = TouhouHatItem.showTooltip();
			Component obt;
			if (obtain) {
				obt = YHLangData.OBTAIN_FLESH.get();
			} else {
				obt = YHLangData.UNKNOWN.get();
			}
			list.add(YHLangData.OBTAIN.get().append(obt));
		}
	}

	@Override
	public Component getName(ItemStack pStack) {
		Player player = getPlayer();
		Component name;
		if (player != null && PlayerStatusData.Status.YOUKAIFIED.is(player)) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(this.getDescriptionId(pStack), name);
	}

	public void consume(Player consumer) {
		if (consumer.level().isClientSide() || !PlayerStatusData.HOLDER.isProper(consumer)) return;
		PlayerStatusData.HOLDER.get(consumer).triggerFlesh();
		if (getDefaultInstance().is(YHTagGen.APPARENT_FLESH_FOOD) && consumer instanceof ServerPlayer sp) {
			ReimuEventHandlers.triggerReimuResponse(sp, 24, true);
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, worldIn, consumer);
		if (!(consumer instanceof Player player)) return ans;
		consume(player);
		return ans;
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
	}

}
