package dev.xkmc.youkaishomecoming.content.entity.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class MaidenEntity extends BossYoukaiEntity {

	public MaidenEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		bossEvent.setVisible(false);
	}

	protected boolean wouldAttack(LivingEntity entity) {
		return entity instanceof Mob mob && (
				mob.getTarget() instanceof Villager ||
						mob.getLastHurtMob() instanceof Villager
		) || entity.hasEffect(YHEffects.YOUKAIFYING.get());
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
