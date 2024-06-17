package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class CirnoEntity extends FairyEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return FairyEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public CirnoEntity(EntityType<? extends GeneralYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
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
		e.setTicksFrozen(Math.min(200, e.getTicksFrozen() + 120));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		TouhouSpellCards.setCirno(this);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public static boolean checkCirnoSpawnRules(EntityType<CirnoEntity> e, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
		if (!checkMobSpawnRules(e, level, type, pos, rand)) return false;
		if (!level.getEntitiesOfClass(CirnoEntity.class, AABB.ofSize(pos.getCenter(), 48, 24, 48)).isEmpty()) return false;
		var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false);
		if (player == null) return false;
		return player.hasEffect(YHEffects.YOUKAIFIED.get()) ||
				player.hasEffect(YHEffects.YOUKAIFYING.get());
	}

}
