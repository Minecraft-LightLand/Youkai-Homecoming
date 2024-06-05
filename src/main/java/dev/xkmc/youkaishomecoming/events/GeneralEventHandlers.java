package dev.xkmc.youkaishomecoming.events;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMCompat;
import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.capability.KoishiAttackCapability;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
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
		if (event.getSource().getDirectEntity() instanceof LivingEntity le) {
			if (!le.getMainHandItem().is(ForgeTags.TOOLS_KNIVES)) return;
			if (!le.getOffhandItem().is(Items.GLASS_BOTTLE)) return;
			if (le.hasEffect(YHEffects.YOUKAIFIED.get()) ||
					le.hasEffect(YHEffects.YOUKAIFYING.get())) {
				le.getOffhandItem().shrink(1);
				if (le instanceof Player player) {
					player.getInventory().add(YHItems.BLOOD_BOTTLE.asStack());
				} else {
					le.spawnAtLocation(YHItems.BLOOD_BOTTLE.asStack());
				}
			}
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
			if (event.getSource().getEntity() instanceof ServerPlayer sp) {
				generateYoukaiResponse(sp, 16, false);
			}
		}
	}

	public static void generateYoukaiResponse(ServerPlayer sp, int range, boolean requireSight) {
		AABB aabb = sp.getBoundingBox().inflate(range);
		var list = sp.serverLevel().getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && (!requireSight || e.hasLineOfSight(sp)));
		if (!list.isEmpty()) {
			if (GeneralEventHandlers.summonProtector(sp)) {
				list.forEach(GolemSensor::golemDetected);
			}
		}
		for (var e : list) {
			sp.serverLevel().broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
			sp.serverLevel().onReputationEvent(ReputationEventType.VILLAGER_KILLED, sp, e);
		}
	}

	private static boolean summonProtector(ServerPlayer sp) {
		if (sp.isCreative()) return false;
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			return TLMCompat.summonReimu(sp);
		}
		var opt = SpawnUtil.trySpawnMob(EntityType.IRON_GOLEM, MobSpawnType.MOB_SUMMONED, sp.serverLevel(), sp.blockPosition(),
				10, 8, 6, SpawnUtil.Strategy.LEGACY_IRON_GOLEM);
		if (opt.isPresent()) {
			int time = 3 * 60 * 20;
			opt.get().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, time, 4));
			opt.get().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, time, 2));
			return true;
		}
		return false;
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
}
