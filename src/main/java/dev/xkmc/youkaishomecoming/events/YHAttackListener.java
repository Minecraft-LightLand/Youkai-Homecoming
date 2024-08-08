package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.youkaishomecoming.content.effect.UdumbaraEffect;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.DamageTypeTags;

public class YHAttackListener implements AttackListener {

	@Override
	public boolean onAttack(DamageData.Attack data) {
		if (data.getSource().is(DamageTypeTags.IS_FIRE)) {
			if (data.getTarget().hasEffect(YHEffects.REFRESHING)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDamage(DamageData.Defence data) {
		var source = data.getSource();
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS) ||
				source.is(DamageTypeTags.BYPASSES_RESISTANCE) ||
				source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return;
		int reduction = 0;
		var e = data.getTarget();
		if (e.hasEffect(YHEffects.THICK)) {
			reduction += 1;
		}
		var udu = e.getEffect(YHEffects.UDUMBARA);
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
			data.addDealtModifier(DamageModifier.add(-reduction, YoukaisHomecoming.loc("reduction")));
		}
	}

}
