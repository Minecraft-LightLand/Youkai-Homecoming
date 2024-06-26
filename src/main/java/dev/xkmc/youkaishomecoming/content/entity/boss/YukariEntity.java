package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class YukariEntity extends BossYoukaiEntity {

	public YukariEntity(EntityType<? extends BossYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected boolean wouldAttack(LivingEntity entity) {
		return PlayerStatusData.Kind.WORTHY.is(entity);
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		return le instanceof Enemy || super.shouldHurt(le) || wouldAttack(le);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		TouhouSpellCards.setYukari(this);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

}
