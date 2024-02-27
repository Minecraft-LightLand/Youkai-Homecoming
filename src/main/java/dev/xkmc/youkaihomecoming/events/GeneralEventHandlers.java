package dev.xkmc.youkaihomecoming.events;

import dev.xkmc.youkaihomecoming.content.block.LeftClickBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.data.YHTagGen;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaihomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.tag.ForgeTags;

@Mod.EventBusSubscriber(modid = YoukaiHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

}
