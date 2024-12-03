package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class YHAttackListener implements AttackListener {

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
