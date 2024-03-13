package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.capability.KoishiAttackCapability;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
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
	public static void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SOBER.get())) {
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity le) {
			if (le.hasEffect(YHEffects.UNCONSCIOUS.get())) {
				le.removeEffect(YHEffects.UNCONSCIOUS.get());
				if (le instanceof Player player) {
					player.getCooldowns().addCooldown(YHItems.KOISHI_HAT.get(), 200);//TODO config
				}
			}
		}
		if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
			if (event.getEntity().hasEffect(YHEffects.REFRESHING.get())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onDamage(LivingDamageEvent event) {
		var source = event.getSource();
		if (source.is(DamageTypeTags.BYPASSES_EFFECTS) ||
				source.is(DamageTypeTags.BYPASSES_RESISTANCE) ||
				source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return;
		int reduction = 0;
		var e = event.getEntity();
		if (e.hasEffect(YHEffects.THICK.get())) {
			reduction += 1;
		}
		var udu = e.getEffect(YHEffects.UDUMBARA.get());
		if (udu != null) {
			var level = e.level();
			if (level.isNight() && level.canSeeSky(e.blockPosition().above()) && level.getMoonBrightness() > 0.8f) {
				reduction += YHModConfig.COMMON.udumbaraFullMoonReduction.get() << udu.getAmplifier();
			}
		}
		if (reduction > 0) {
			event.setAmount(Math.max(0, event.getAmount() - reduction));
		}
	}

	@SubscribeEvent
	public static void onHeal(LivingHealEvent event) {
		if (event.getEntity().hasEffect(YHEffects.SMOOTHING.get())) {
			event.setAmount((float) (event.getAmount() * YHModConfig.COMMON.smoothingHealingFactor.get()));
		}
	}

	@SubscribeEvent
	public static void onTick(LivingEvent.LivingTickEvent event) {
		var e = event.getEntity();
		if (e.hasEffect(YHEffects.THICK.get()) && e.hasEffect(MobEffects.WITHER)) {
			e.removeEffect(MobEffects.WITHER);
		}
		if (e.hasEffect(YHEffects.SMOOTHING.get()) && e.hasEffect(MobEffects.POISON)) {
			e.removeEffect(MobEffects.POISON);
		}
		if (e.hasEffect(YHEffects.REFRESHING.get()) && e.isOnFire()) {
			e.clearFire();
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect() == MobEffects.WITHER) {
			if (event.getEntity().hasEffect(YHEffects.THICK.get())) {
				event.setCanceled(true);
			}
		}
		if (event.getEffectInstance().getEffect() == MobEffects.POISON) {
			if (event.getEntity().hasEffect(YHEffects.SMOOTHING.get())) {
				event.setCanceled(true);
			}
		}
		if (event.getEffectInstance().getEffect() == YHEffects.YOUKAIFYING.get()) {
			if (event.getEntity().hasEffect(YHEffects.SOBER.get()) ||
					event.getEntity().hasEffect(YHEffects.YOUKAIFIED.get())) {
				event.setCanceled(true);
			}
		}
		if (event.getEffectInstance().getEffect() == YHEffects.YOUKAIFIED.get()) {
			if (event.getEntity().hasEffect(YHEffects.SOBER.get())) {
				event.setCanceled(true);
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
	}

	@SubscribeEvent
	public static void collectBlood(LivingDeathEvent event) {
		if (!event.getEntity().getType().is(YHTagGen.FLESH_SOURCE)) return;
		if (event.getSource().getDirectEntity() instanceof LivingEntity le) {
			if (!le.getMainHandItem().is(ForgeTags.TOOLS_KNIVES)) return;
			if (!le.getOffhandItem().is(Items.GLASS_BOTTLE)) return;
			if (le.hasEffect(YHEffects.YOUKAIFIED.get()) || le.hasEffect(YHEffects.YOUKAIFYING.get())) {
				le.getOffhandItem().shrink(1);
				if (le instanceof Player player) {
					player.getInventory().add(YHItems.BLOOD_BOTTLE.asStack());
				} else {
					le.spawnAtLocation(YHItems.BLOOD_BOTTLE.asStack());
				}
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
			if (le.hasEffect(YHEffects.UDUMBARA.get())) {
				return true;
			}
		}
		return false;
	}
}
