package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2library.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandlers {

	public static boolean isYoukai(LivingEntity e) {
		return
				e.hasEffect(YHEffects.YOUKAIFYING.get()) ||
						e.hasEffect(YHEffects.YOUKAIFIED.get());
	}

	public static boolean isCharacter(LivingEntity e) {
		return e instanceof YoukaiEntity ||
				e.hasEffect(YHEffects.YOUKAIFYING.get()) ||
				e.hasEffect(YHEffects.YOUKAIFIED.get()) ||
				e.hasEffect(YHEffects.FAIRY.get());
	}

	public static boolean isFullCharacter(LivingEntity e) {
		return e instanceof YoukaiEntity ||
				e.hasEffect(YHEffects.YOUKAIFIED.get()) ||
				e.hasEffect(YHEffects.FAIRY.get());
	}

	@SubscribeEvent
	public static void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SOBER.get())) {
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

	public static void disableKoishi(Player player) {
		boolean flag = false;
		var hat = YHItems.KOISHI_HAT.get();
		if (player.hasEffect(YHEffects.UNCONSCIOUS.get())) {
			player.removeEffect(YHEffects.UNCONSCIOUS.get());
			flag = true;
		}
		if (player.getCooldowns().isOnCooldown(hat)) {
			flag = true;
		}
		if (flag) {
			player.getCooldowns().addCooldown(hat, 200);
		}
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity le) {
			if (le instanceof Player player) {
				disableKoishi(player);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHeal(LivingHealEvent event) {
		float amount = event.getAmount();
		if (event.getEntity().hasEffect(YHEffects.SMOOTHING.get())) {
			amount *= YHModConfig.COMMON.smoothingHealingFactor.get();
		}
		if (event.getEntity().hasEffect(YHEffects.FAIRY.get())) {
			amount *= YHModConfig.COMMON.fairyHealingFactor.get();
		}
		event.setAmount(amount);
	}

	@SubscribeEvent
	public static void onTick(LivingEvent.LivingTickEvent event) {
		var e = event.getEntity();
		if (e.hasEffect(YHEffects.THICK.get()) && e.hasEffect(MobEffects.WITHER)) {
			e.removeEffect(MobEffects.WITHER);
		}
		if (e.hasEffect(YHEffects.SMOOTHING.get()) && e.hasEffect(MobEffects.POISON)) {
			e.removeEffect(MobEffects.POISON);
		}
		if (e.hasEffect(YHEffects.REFRESHING.get()) && e.isOnFire()) {
			e.clearFire();
		}
		if (e.getLastHurtMob() instanceof MaidenEntity ||
				e.getLastHurtByMob() instanceof MaidenEntity) {
			removeKoishi(e);
		}
	}

	public static void removeKoishi(LivingEntity le) {
		if (le instanceof Player player) {
			if (player.hasEffect(YHEffects.UNCONSCIOUS.get())) {
				player.removeEffect(YHEffects.UNCONSCIOUS.get());
			}
			var hat = YHItems.KOISHI_HAT.get();
			if (player.getItemBySlot(EquipmentSlot.HEAD).is(hat)) {
				if (player.getCooldowns().getCooldownPercent(hat, 0) < 0.5)
					player.getCooldowns().addCooldown(hat, 200);
			}
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect() == MobEffects.WITHER) {
			if (event.getEntity().hasEffect(YHEffects.THICK.get())) {
				event.setResult(Event.Result.DENY);
			}
		}
		if (event.getEffectInstance().getEffect() == MobEffects.POISON) {
			if (event.getEntity().hasEffect(YHEffects.SMOOTHING.get())) {
				event.setResult(Event.Result.DENY);
			}
		}
		if (event.getEffectInstance().getEffect() == YHEffects.YOUKAIFYING.get()) {
			if (event.getEntity().hasEffect(YHEffects.SOBER.get()) ||
					event.getEntity().hasEffect(YHEffects.FAIRY.get()) ||
					event.getEntity().hasEffect(YHEffects.YOUKAIFIED.get())) {
				event.setResult(Event.Result.DENY);
			}
		}
		if (event.getEffectInstance().getEffect() == YHEffects.YOUKAIFIED.get()) {
			if (event.getEntity().hasEffect(YHEffects.SOBER.get()) ||
					event.getEntity().hasEffect(YHEffects.FAIRY.get())) {
				event.setResult(Event.Result.DENY);
			}
		}
		if (event.getEffectInstance().getEffect() == YHEffects.FAIRY.get()) {
			if (event.getEntity().hasEffect(YHEffects.YOUKAIFYING.get()) ||
					event.getEntity().hasEffect(YHEffects.YOUKAIFIED.get())) {
				event.setResult(Event.Result.DENY);
			}
		}
	}

	public static MobEffectInstance onEat(LivingEntity user, MobEffectInstance ins) {
		var builder = new EffectBuilder(ins);
		int dur = ins.getDuration();
		var enjoy = user.getEffect(YHEffects.ENJOYABLE.get());
		if (enjoy != null && ins.getEffect().isBeneficial()) {
			int lv = enjoy.getAmplifier() + 1;
			builder.setDuration((int) (dur * (1 + 0.2 * lv)));
		}
		return ins;
	}

}
