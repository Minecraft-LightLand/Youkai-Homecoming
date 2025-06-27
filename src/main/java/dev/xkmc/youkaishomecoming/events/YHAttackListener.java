package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.youkaishomecoming.content.capability.GrazeCapability;
import dev.xkmc.youkaishomecoming.content.entity.animal.tuna.TunaEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class YHAttackListener implements AttackListener {

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
	public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
		var attacker = cache.getAttacker();
		if (attacker == null) return;
		var damageEvent = cache.getLivingDamageEvent();
		assert damageEvent != null;
		ItemStack head = attacker.getItemBySlot(EquipmentSlot.HEAD);
		if (head.getItem() instanceof TouhouHatItem hat) {
			hat.onHurtTarget(head, damageEvent.getSource(), cache.getAttackTarget());
		}
	}
}
