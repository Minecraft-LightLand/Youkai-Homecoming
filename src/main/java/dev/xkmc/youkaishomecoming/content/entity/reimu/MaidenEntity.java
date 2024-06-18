package dev.xkmc.youkaishomecoming.content.entity.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

@SerialClass
public class MaidenEntity extends BossYoukaiEntity {

	public MaidenEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		bossEvent.setVisible(false);
		if (walkNav instanceof GroundPathNavigation nav) {
			nav.setCanPassDoors(true);
			nav.setCanOpenDoors(true);
			nav.setCanFloat(true);
		}
	}

	protected boolean wouldAttack(LivingEntity entity) {
		return entity instanceof Mob mob && (
				mob.getTarget() instanceof Villager ||
						mob.getLastHurtMob() instanceof Villager
		) || entity.getType().is(EntityTypeTags.RAIDERS) ||
				entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		return le instanceof Enemy || super.shouldHurt(le);
	}

	@SerialClass.SerialField
	private int noPlayerTime = 0;

	public void refreshIdle() {
		noPlayerTime = 0;
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (noTargetTime == 0) {
			bossEvent.setVisible(true);
		}
		if (noTargetTime > 40) {
			bossEvent.setVisible(false);
		}
		if (noTargetTime > 100 && tickCount % 20 == 0 && !level().isClientSide()) {
			var e = level().getNearestPlayer(this, 32);
			if (e == null || e.isSpectator()) {
				noPlayerTime++;
				if (noPlayerTime > 30) {
					discard();
				}
				return;
			}
		}
		noPlayerTime = 0;
	}

}
