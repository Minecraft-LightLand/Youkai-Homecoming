package dev.xkmc.youkaishomecoming.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientEventHandlers {

	private static float oTilt, tilt;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		oTilt = tilt;
		float lv = drunkLevel();
		tilt = Mth.lerp(0.03f, tilt, lv);
	}

	private static float drunkLevel() {
		var cam = Minecraft.getInstance().getCameraEntity();
		if (!(cam instanceof Player player)) return 0;
		var ins = player.getEffect(YHEffects.DRUNK);
		if (ins == null) return 0;
		return Mth.clamp((ins.getAmplifier() + 1) * 0.2f, 0, 1);
	}

	public static void drunkView(PoseStack pose, float pTick) {
		var cam = Minecraft.getInstance().getCameraEntity();
		if (!(cam instanceof Player player)) return;
		float t = Mth.lerp(pTick, oTilt, tilt);
		if (t < 0.01) return;
		float time = pTick + player.tickCount;
		pose.mulPose(Axis.ZP.rotationDegrees(t * t * 45 * Mth.sin((float) (time / 60 * Math.PI * 2))));
	}

}
