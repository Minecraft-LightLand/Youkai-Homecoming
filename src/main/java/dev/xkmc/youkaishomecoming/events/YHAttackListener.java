package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.youkaishomecoming.content.effect.UdumbaraEffect;
import dev.xkmc.youkaishomecoming.content.entity.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.animal.WaterAnimal;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class YHAttackListener implements AttackListener {

	@Override
	public boolean onAttack(DamageData.Attack cache) {
		var source = cache.getSource();
		if (source.is(DamageTypeTags.IS_FIRE)) {
			if (cache.getTarget().hasEffect(YHEffects.REFRESHING)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onHurt(DamageData.Offence cache) {
		var source = cache.getSource();
		if (source.getEntity() instanceof TunaEntity && cache.getTarget() instanceof WaterAnimal) {
			cache.addHurtModifier(DamageModifier.multTotal(2, YHEntities.TUNA.getId()));
		}
	}

	@Override
	public void onDamage(DamageData.Defence cache) {
		var source = cache.getSource();
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS) ||
				source.is(DamageTypeTags.BYPASSES_RESISTANCE) ||
				source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return;
		var e = cache.getTarget();
		if (e.hasEffect(YHEffects.THICK)) {
			cache.addDealtModifier(DamageModifier.add(-1, YHEffects.THICK.val().getId()));
		}
		var udu = e.getEffect(YHEffects.UDUMBARA);
		if (udu != null) {
			var level = e.level();
			if (level.isNight()) {
				if (level.canSeeSky(e.blockPosition().above()) &&
						level.getMoonBrightness() > 0.8f ||
						UdumbaraEffect.hasLantern(e)) {
					int val = YHModConfig.COMMON.udumbaraFullMoonReduction.get() << udu.getAmplifier();
					cache.addDealtModifier(DamageModifier.add(-val, YHEffects.UDUMBARA.val().getId()));
				}
			}
		}
		var mature = e.getEffect(YHEffects.MATURE);
		var nourishment = e.getEffect(ModEffects.NOURISHMENT);
		if (mature != null && nourishment != null) {
			cache.addDealtModifier(DamageModifier.nonlinearMiddle(182, f -> {
				int max = Math.min((int) (f * 0.2f * (mature.getAmplifier() + 1)), nourishment.getDuration() / 100);
				e.removeEffect(ModEffects.NOURISHMENT);
				new EffectBuilder(nourishment).setDuration(nourishment.getDuration() - max * 100);
				e.addEffect(nourishment);
				return f - max;
			}, YHEffects.MATURE.val().getId()));
		}
	}

	@Override
	public void onDamageFinalized(DamageData.DefenceMax cache) {
		var attacker = cache.getAttacker();
		if (attacker == null) return;
		var target = cache.getTarget();
		var ins = target.getEffect(YHEffects.CONFIDENT);
		if (ins != null) {
			target.invulnerableTime += (ins.getAmplifier() + 1) * 6;
		}
	}

}
