package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@SerialClass
public class CirnoEntity extends FairyEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return FairyEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public CirnoEntity(EntityType<? extends CirnoEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source) || source.is(DamageTypeTags.IS_FREEZING);
	}

	@Override
	protected boolean wouldAttack(LivingEntity entity) {
		return super.wouldAttack(entity) || entity instanceof Frog;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (e.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET) &&
				e.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE) &&
				e.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS) &&
				e.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) {
			var ice = YHItems.FAIRY_ICE_CRYSTAL.asStack();
			double chance = YHModConfig.COMMON.cirnoFairyDrop.get();
			if (e.getRandom().nextDouble() < chance) {
				if (e instanceof Player pl) {
					pl.getInventory().placeItemBackInInventory(ice);
				} else {
					e.spawnAtLocation(ice);
				}
			}
			return;
		}
		if (e instanceof YoukaiEntity || e.hasEffect(YHEffects.YOUKAIFIED.get())) return;
		e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
		if (e.canFreeze()) {
			e.setTicksFrozen(Math.min(200, e.getTicksFrozen() + 120));
		}
	}

	public void initSpellCard() {
		TouhouSpellCards.setCirno(this);
	}

}
