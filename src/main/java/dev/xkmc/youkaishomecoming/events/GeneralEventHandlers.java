package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.capability.KoishiAttackCapability;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
		if (event.getSource().getEntity() instanceof LivingEntity le) {
			if (le.getMainHandItem().is(ForgeTags.TOOLS_KNIVES) &&
					EffectEventHandlers.isYoukai(le))
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
		if (event.getEntity() instanceof AbstractVillager e &&
				YHModConfig.COMMON.reimuSummonKill.get() &&
				event.getSource().getEntity() instanceof ServerPlayer sp &&
				EffectEventHandlers.isYoukai(sp)
		) {
			GeneralEventHandler.schedule(() -> {
				if (e.isAlive()) {
					ReimuEventHandlers.hurtWarn(sp);
				}
			});

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
		if (event.getEntity() instanceof Villager && YHModConfig.COMMON.reimuSummonKill.get()) {
			if (event.getSource().getEntity() instanceof LivingEntity le) {
				if (EffectEventHandlers.isYoukai(le))
					ReimuEventHandlers.triggerReimuResponse(le, 16, false);
			}
		}
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
