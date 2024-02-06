package dev.xkmc.youkaihomecoming.events;

import dev.xkmc.youkaihomecoming.content.block.LeftClickBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

}
