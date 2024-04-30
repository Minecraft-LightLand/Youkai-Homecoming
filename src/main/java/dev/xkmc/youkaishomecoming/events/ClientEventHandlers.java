package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandlers {

	@SubscribeEvent
	public static void playerTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		MobEffectInstance ins = player.getEffect(YHEffects.DRUNK.get());
		if (ins == null) return;
		if (player.spinningEffectIntensity < ins.getAmplifier() * 0.2) {
			player.spinningEffectIntensity += 0.01f;
		}
	}

}
