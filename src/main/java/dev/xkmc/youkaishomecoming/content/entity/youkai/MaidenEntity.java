package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class MaidenEntity extends BossYoukaiEntity {

	public MaidenEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected boolean wouldAttack(LivingEntity entity) {
		return entity instanceof Mob mob && (
				mob.getTarget() instanceof Villager ||
						mob.getLastHurtMob() instanceof Villager
		) || entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		return le instanceof Enemy || super.shouldHurt(le) || wouldAttack(le);
	}

	@Override
	protected void dropEquipment() {
		super.dropEquipment();
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		TouhouSpellCards.setReimu(this);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

}
