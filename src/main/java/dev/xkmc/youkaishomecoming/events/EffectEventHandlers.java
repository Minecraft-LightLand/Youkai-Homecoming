package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.content.effect.StatusTokenEffect;
import dev.xkmc.youkaishomecoming.content.effect.UdumbaraEffect;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandlers {

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
		if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
			if (event.getEntity().hasEffect(YHEffects.REFRESHING.get())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onDamage(LivingDamageEvent event) {
		var source = event.getSource();
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS) ||
				source.is(DamageTypeTags.BYPASSES_RESISTANCE) ||
				source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return;
		int reduction = 0;
		var e = event.getEntity();
		if (e.hasEffect(YHEffects.THICK.get())) {
			reduction += 1;
		}
		var udu = e.getEffect(YHEffects.UDUMBARA.get());
		if (udu != null) {
			var level = e.level();
			if (level.isNight()) {
				if (level.canSeeSky(e.blockPosition().above()) &&
						level.getMoonBrightness() > 0.8f ||
						UdumbaraEffect.hasLantern(e))
					reduction += YHModConfig.COMMON.udumbaraFullMoonReduction.get() << udu.getAmplifier();
			}
		}
		if (reduction > 0) {
			event.setAmount(Math.max(0, event.getAmount() - reduction));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHeal(LivingHealEvent event) {
		float amount = event.getAmount();
		if (event.getEntity().hasEffect(YHEffects.SMOOTHING.get())) {
			amount *= YHModConfig.COMMON.smoothingHealingFactor.get();
		}
		if (PlayerStatusData.Status.FAIRY.is(event.getEntity())) {
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
		if (event.getEffectInstance().getEffect() instanceof StatusTokenEffect) {
			event.setResult(Event.Result.DENY);
		}
	}

}
