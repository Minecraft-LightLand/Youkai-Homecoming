package dev.xkmc.youkaishomecoming.events;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHAdvGen;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReimuEventHandlers {

	private static List<Villager> getWitness(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return List.of();
		AABB aabb = le.getBoundingBox().inflate(range);
		return sl.getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && !e.hasEffect(YHEffects.HYPNOSIS.get())
						&& (!eatFlesh || e.hasLineOfSight(le)));
	}

	public static void triggerReimuResponse(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return;
		if (!le.level().dimension().equals(Level.OVERWORLD)) return;
		var list = getWitness(le, range, eatFlesh);
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
			if (trySummonMaidenAttack(sl, le)) {
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
		if (!sp.level().dimension().equals(Level.OVERWORLD)) return;
		if (getWitness(sp, 16, false).isEmpty()) return;
		if (koishiBlockReimu(sp)) return;
		var adv = sp.server.getAdvancements().getAdvancement(YHAdvGen.HURT_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return;
		YHCriteriaTriggers.HURT_WARN.trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(YHLangData.REIMU_WARN.get(), true);
	}

	@Nullable
	public static BossYoukaiEntity trySummonMaiden(ServerLevel sl, LivingEntity le) {
		EntityEntry<? extends BossYoukaiEntity> type = YHEntities.REIMU;
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			if (le.getRandom().nextFloat() > 0.5) {
				if (le.getRandom().nextFloat() > 0.5) {
					type = YHEntities.SANAE;
				} else {
					type = YHEntities.MARISA;
				}
			}
		}
		var e = trySummon(type, sl, le, 5, 5);
		if (e == null) return null;
		e.initSpellCard();
		sl.addFreshEntity(e);
		return e;
	}

	@Nullable
	public static <T extends BossYoukaiEntity> T trySummon(EntityEntry<T> type, ServerLevel sl, LivingEntity le, int y0, int dy) {
		BlockPos center = BlockPos.containing(le.position().add(le.getForward().scale(8)).add(0, y0, 0));
		T e = type.create(sl);
		if (e == null) return null;
		if (!setRandomizedPos(le, e, center, dy))
			return null;
		return e;
	}

	private static boolean trySummonMaidenAttack(ServerLevel sl, LivingEntity le) {
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
		var maiden = trySummonMaiden(sl, le);
		if (maiden == null) return false;
		EffectEventHandlers.removeKoishi(le);
		maiden.setTarget(le);
		return true;
	}

	public static boolean setRandomizedPos(LivingEntity target, YoukaiEntity youkai, BlockPos center, int dy) {
		BlockPos pos = getPosForSpawn(target, youkai, center, 16, 8, dy);
		if (pos == null) {
			center = target.blockPosition().above(5);
			pos = getPosForSpawn(target, youkai, center, 16, 16, dy);
		}
		if (pos == null) return false;
		youkai.moveTo(pos, 0, 0);
		return true;
	}

	@Nullable
	public static BlockPos getPosForSpawn(LivingEntity target, YoukaiEntity youkai, BlockPos center, int trial, int range, int dy) {
		for (int i = 0; i < trial; i++) {
			BlockPos pos = center.offset(
					target.getRandom().nextInt(-range, range),
					target.getRandom().nextInt(-dy, dy),
					target.getRandom().nextInt(-range, range)
			);
			youkai.moveTo(pos, 0, 0);
			if (target.level().noCollision(youkai) && youkai.hasLineOfSight(target)) {
				return pos;
			}
		}
		return null;
	}

}
