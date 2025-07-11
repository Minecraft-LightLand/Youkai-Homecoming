package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.block.LeftClickBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GeneralEventHandlers {

	@SubscribeEvent
	public static void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntity().hasEffect(CoffeeEffects.CAFFEINATED.get())) {
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

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

	public static boolean preventPhantomSpawn(ServerPlayer player) {
		return player.hasEffect(CoffeeEffects.CAFFEINATED.get());
	}

}
