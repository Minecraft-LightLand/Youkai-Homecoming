package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = EventBusSubscriber.Bus.GAME)
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
			}
		}
	}


	public static boolean preventPhantomSpawn(ServerPlayer player) {
		return player.hasEffect(YHEffects.SOBER);
	}

	public static boolean supressVibration(Entity self) {
		if (self instanceof TraceableEntity item) {
			if (item.getOwner() instanceof LivingEntity le) {
				self = le;
			}
		}
		if (self instanceof LivingEntity le) {
			return le.hasEffect(YHEffects.UDUMBARA);
		}
		return false;
	}

}
