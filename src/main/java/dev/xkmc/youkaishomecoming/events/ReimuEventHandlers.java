package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHAdvGen;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class ReimuEventHandlers {

	public static void triggerReimuResponse(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return;
		AABB aabb = le.getBoundingBox().inflate(range);
		var list = sl.getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && (!eatFlesh || e.hasLineOfSight(le)));
		if (!list.isEmpty()) {
			if (le instanceof ServerPlayer sp && eatFlesh) {
				if (!YHModConfig.COMMON.reimuSummonFlesh.get() || fleshWarn(sp)) {
					for (var e : list) {
						sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
						sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
					}
					return;
				}
			}
			if (trySummonReimuAttack(sl, le)) {
				list.forEach(GolemSensor::golemDetected);
			}
		}
		for (var e : list) {
			sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
			sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
		}
	}

	public static boolean koishiBlockReimu(LivingEntity le) {
		var hat = YHItems.KOISHI_HAT.get();
		if (le instanceof ServerPlayer sp &&
				le.getItemBySlot(EquipmentSlot.HEAD).is(hat) &&
				!sp.getCooldowns().isOnCooldown(hat)) {
			EffectEventHandlers.removeKoishi(le);
			sp.getCooldowns().addCooldown(hat, 1200);
			sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
			sp.sendSystemMessage(YHLangData.KOISHI_REIMU.get(), true);
			return true;
		}
		return false;
	}

	public static boolean fleshWarn(ServerPlayer sp) {
		if (koishiBlockReimu(sp)) return true;
		var adv = sp.server.getAdvancements().getAdvancement(YHAdvGen.FLESH_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return false;
		YHCriteriaTriggers.FLESH_WARN.trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(YHLangData.REIMU_FLESH.get(), true);
		return true;
	}

	public static void hurtWarn(ServerPlayer sp) {
		if (koishiBlockReimu(sp)) return;
		var adv = sp.server.getAdvancements().getAdvancement(YHAdvGen.HURT_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return;
		YHCriteriaTriggers.HURT_WARN.trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(YHLangData.REIMU_WARN.get(), true);
	}

	@Nullable
	public static MaidenEntity trySummonReimu(ServerLevel sl, LivingEntity le) {
		BlockPos center = BlockPos.containing(le.position().add(le.getForward().scale(8)).add(0, 5, 0));
		MaidenEntity e = YHEntities.REIMU.create(sl);
		if (e == null) return null;
		BlockPos pos = getPosForReimuSpawn(le, e, center, 16, 8, 5);
		if (pos == null) {
			center = le.blockPosition().above(5);
			pos = getPosForReimuSpawn(le, e, center, 16, 16, 5);
		}
		if (pos == null) return null;
		e.moveTo(pos, 0, 0);
		TouhouSpellCards.setReimu(e);
		sl.addFreshEntity(e);
		return e;
	}

	private static boolean trySummonReimuAttack(ServerLevel sl, LivingEntity le) {
		if (le instanceof ServerPlayer sp && sp.isCreative()) return false;
		if (koishiBlockReimu(le)) return false;
		EffectEventHandlers.removeKoishi(le);
		var list = sl.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
				le.getBoundingBox().inflate(32), LivingEntity::isAlive);
		if (!list.isEmpty()) {
			for (var e : list) {
				e.setTarget(le);
				e.refreshIdle();
			}
			return false;
		}
		var maiden = trySummonReimu(sl, le);
		if (maiden == null) return false;
		EffectEventHandlers.removeKoishi(le);
		maiden.setTarget(le);
		return true;
	}

	@Nullable
	private static BlockPos getPosForReimuSpawn(LivingEntity sp, Entity e, BlockPos center, int trial, int range, int dy) {
		for (int i = 0; i < trial; i++) {
			BlockPos pos = center.offset(
					sp.getRandom().nextInt(-range, range),
					sp.getRandom().nextInt(-dy, dy),
					sp.getRandom().nextInt(-range, range)
			);
			e.moveTo(pos, 0, 0);
			if (sp.level().noCollision(e)) {
				return pos;
			}
		}
		return null;
	}

}
