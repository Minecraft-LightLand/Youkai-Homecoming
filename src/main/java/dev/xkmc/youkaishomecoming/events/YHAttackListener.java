package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.capability.GrazeCapability;
import dev.xkmc.youkaishomecoming.content.effect.UdumbaraEffect;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class YHAttackListener implements AttackListener {

	@Override
	public void onAttack(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingAttackEvent();
		assert event != null;
		var source = event.getSource();
		if (source.is(DamageTypeTags.IS_FIRE)) {
			if (event.getEntity().hasEffect(YHEffects.REFRESHING.get())) {
				event.setCanceled(true);
			}
		}
	}

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingHurtEvent();
		assert event != null;
		var source = event.getSource();
		if (source.is(YHDamageTypes.DANMAKU_TYPE) && source.getEntity() instanceof Player player) {
			var graze = GrazeCapability.HOLDER.get(player);
			cache.addHurtModifier(DamageModifier.multTotal(1 + graze.powerBonus()));
		}
		if (source.getEntity() instanceof TunaEntity && cache.getAttackTarget() instanceof WaterAnimal) {
			cache.addHurtModifier(DamageModifier.multTotal(2));
		}
	}

	@Override
	public void onDamage(AttackCache cache, ItemStack weapon) {
		var event = cache.getLivingDamageEvent();
		assert event != null;
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
			cache.addDealtModifier(DamageModifier.add(-reduction));
		}
		var mature = e.getEffect(YHEffects.MATURE.get());
		var nourishment = e.getEffect(ModEffects.NOURISHMENT.get());
		if (mature != null && nourishment != null) {
			cache.addDealtModifier(DamageModifier.nonlinearMiddle(182, f -> {
				int max = Math.min((int) (f * 0.2f * (mature.getAmplifier() + 1)), nourishment.getDuration() / 100);
				e.removeEffect(ModEffects.NOURISHMENT.get());
				new EffectBuilder(nourishment).setDuration(nourishment.getDuration() - max * 100);
				e.addEffect(nourishment);
				return f - max;
			}));
		}
	}

	@Override
	public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
		var attacker = cache.getAttacker();
		if (attacker == null) return;
		var damageEvent = cache.getLivingDamageEvent();
		assert damageEvent != null;
		ItemStack head = attacker.getItemBySlot(EquipmentSlot.HEAD);
		if (head.getItem() instanceof TouhouHatItem hat) {
			hat.onHurtTarget(head, damageEvent.getSource(), cache.getAttackTarget());
		}
		var target = cache.getAttackTarget();
		var ins = target.getEffect(YHEffects.CONFIDENT.get());
		if (ins != null) {
			target.invulnerableTime += (ins.getAmplifier() + 1) * 6;
		}
	}

}
