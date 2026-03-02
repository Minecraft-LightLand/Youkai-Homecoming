package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = YoukaisHomecoming.MODID)
public class EffectEventHandlers {

	@SubscribeEvent
	public static void onSleep(CanPlayerSleepEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SOBER)) {
			event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onHeal(LivingHealEvent event) {
		float amount = event.getAmount();
		if (event.getEntity().hasEffect(YHEffects.BREATHING)) {
			amount *= YHModConfig.COMMON.breathingHealingFactor.get();
		}
		event.setAmount(amount);
	}

	@SubscribeEvent
	public static void onTick(EntityTickEvent.Pre event) {
		if (!(event.getEntity() instanceof LivingEntity e)) return;
		if (e.hasEffect(YHEffects.THICK) && e.hasEffect(MobEffects.WITHER)) {
			e.removeEffect(MobEffects.WITHER);
		}
		if (e.hasEffect(YHEffects.SMOOTHING) && e.hasEffect(MobEffects.POISON)) {
			e.removeEffect(MobEffects.POISON);
		}
		if (e.hasEffect(YHEffects.REFRESHING) && e.isOnFire()) {
			e.clearFire();
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().value() == MobEffects.WITHER.value()) {
			if (event.getEntity().hasEffect(YHEffects.SMOOTHING)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
		if (event.getEffectInstance().getEffect().value() == MobEffects.POISON.value()) {
			if (event.getEntity().hasEffect(YHEffects.SMOOTHING)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
	}

	public static MobEffectInstance onEat(LivingEntity user, MobEffectInstance ins) {
		var builder = new EffectBuilder(ins);
		int dur = ins.getDuration();
		var enjoy = user.getEffect(YHEffects.ENJOYABLE);
		if (enjoy != null && ins.getEffect().value().isBeneficial()) {
			int lv = enjoy.getAmplifier() + 1;
			builder.setDuration((int) (dur * (1 + 0.2 * lv)));
		}
		return ins;
	}

}
