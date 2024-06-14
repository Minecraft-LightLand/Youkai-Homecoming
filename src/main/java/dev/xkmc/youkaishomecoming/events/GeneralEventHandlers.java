package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.capability.KoishiAttackCapability;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ForgeTags;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GeneralEventHandlers {

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getItemStack().is(Items.DEBUG_STICK)) return;
		var level = event.getLevel();
		var pos = event.getPos();
		var state = level.getBlockState(pos);
		if (state.getBlock() instanceof LeftClickBlock block) {
			if (block.leftClick(state, level, pos, event.getEntity())) {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.CONSUME);
			}
		}
	}

	@SubscribeEvent
	public static void onShieldBlock(ShieldBlockEvent event) {
		if (event.getDamageSource().is(YHDamageTypes.KOISHI)) {
			if (event.getEntity() instanceof Player player) {
				KoishiAttackCapability.HOLDER.get(player).onBlock();
			}
		}
		if (event.getDamageSource().getDirectEntity() instanceof RumiaEntity rumia) {
			rumia.state.onBlocked();
		}
	}

	@SubscribeEvent
	public static void collectBlood(LivingDeathEvent event) {
		if (!event.getEntity().getType().is(YHTagGen.FLESH_SOURCE)) return;
		if (event.getSource().getEntity() instanceof LivingEntity le) {
			if (le.getMainHandItem().is(ForgeTags.TOOLS_KNIVES) &&
					(le.hasEffect(YHEffects.YOUKAIFIED.get()) ||
							le.hasEffect(YHEffects.YOUKAIFYING.get())))
				spawnBlood(le);
			if (le.getItemBySlot(EquipmentSlot.HEAD).is(YHItems.RUMIA_HAIRBAND.get()))
				spawnBlood(le);
		}
	}

	private static void spawnBlood(LivingEntity le) {
		if (!le.getOffhandItem().is(Items.GLASS_BOTTLE)) return;
		le.getOffhandItem().shrink(1);
		if (le instanceof Player player) {
			player.getInventory().add(YHItems.BLOOD_BOTTLE.asStack());
		} else {
			le.spawnAtLocation(YHItems.BLOOD_BOTTLE.asStack());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onDamage(LivingDamageEvent event) {
		if (event.getSource().is(YHDamageTypes.DANMAKU) && event.getSource().getDirectEntity() instanceof YoukaiEntity) {
			double min = YHModConfig.COMMON.danmakuMinPHPDamage.get();
			LivingEntity le = event.getEntity();
			if (le instanceof Player) min = YHModConfig.COMMON.danmakuPlayerPHPDamage.get();
			if (event.getAmount() < le.getMaxHealth() * min) {
				event.setAmount(le.getMaxHealth() * (float) min);
			}
		}
	}

	@SubscribeEvent
	public static void startTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof Frog frog) {
			FrogGodCapability.startTracking(frog, event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) {
		if (event.getEntity() instanceof Villager) {
			if (event.getSource().getEntity() instanceof LivingEntity le) {
				if (le.hasEffect(YHEffects.YOUKAIFIED.get()) || le.hasEffect(YHEffects.YOUKAIFYING.get()))
					generateYoukaiResponse(le, 16, false);
			}
		}
	}

	public static void generateYoukaiResponse(LivingEntity le, int range, boolean requireSight) {
		if (!(le.level() instanceof ServerLevel sl)) return;
		AABB aabb = le.getBoundingBox().inflate(range);
		var list = sl.getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && (!requireSight || e.hasLineOfSight(le)));
		if (!list.isEmpty()) {
			if (GeneralEventHandlers.summonProtector(sl, le)) {
				list.forEach(GolemSensor::golemDetected);
			}
		}
		for (var e : list) {
			sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
			sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
		}
	}

	private static boolean summonProtector(ServerLevel sl, LivingEntity le) {
		if (le instanceof ServerPlayer sp && sp.isCreative()) return false;
		var list = sl.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
				le.getBoundingBox().inflate(32), LivingEntity::isAlive);
		if (!list.isEmpty()) {
			for (var e : list) {
				e.setTarget(le);
				e.refreshIdle();
			}
			return true;
		}
		BlockPos center = BlockPos.containing(le.position().add(le.getForward().scale(8)).add(0, 5, 0));
		MaidenEntity e = YHEntities.REIMU.create(sl);
		if (e == null) return false;
		BlockPos pos = getPos(le, e, center, 16, 8, 5);
		if (pos == null) {
			center = le.blockPosition().above(5);
			pos = getPos(le, e, center, 16, 16, 5);
		}
		if (pos == null) return false;
		e.moveTo(pos, 0, 0);
		EffectEventHandlers.removeKoishi(le);
		e.setTarget(le);
		TouhouSpellCards.setReimu(e);
		sl.addFreshEntity(e);
		return true;
	}

	@Nullable
	private static BlockPos getPos(LivingEntity sp, Entity e, BlockPos center, int trial, int range, int dy) {
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

	public static boolean preventPhantomSpawn(ServerPlayer player) {
		return player.hasEffect(YHEffects.SOBER.get());
	}

	public static boolean supressVibration(Entity self) {
		if (self instanceof TraceableEntity item) {
			if (item.getOwner() instanceof LivingEntity le) {
				self = le;
			}
		}
		if (self instanceof LivingEntity le) {
			return le.hasEffect(YHEffects.UDUMBARA.get());
		}
		return false;
	}

	public static DamageSource modifyDamageType(LivingEntity le, DamageSource type, IYHDanmaku danmaku) {
		ItemStack stack = le.getItemBySlot(EquipmentSlot.HEAD);
		if (stack.getItem() instanceof TouhouHatItem hat) {
			return hat.modifyDamageType(stack, le, danmaku, type);
		}
		return type;
	}

}
