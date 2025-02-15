package dev.xkmc.youkaishomecoming.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
import dev.xkmc.youkaishomecoming.content.entity.youkai.CombatProgress;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandlers {

	private static float oTilt, tilt;

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		oTilt = tilt;
		float lv = drunkLevel();
		tilt = Mth.lerp(0.03f, tilt, lv);
	}

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof TouhouHatItem hat) {
			AttrTooltip.modifyTooltip(event.getToolTip(), hat.getAttributeModifiersForDisplay(), false);
		}
	}

	private static float drunkLevel() {
		var cam = Minecraft.getInstance().getCameraEntity();
		if (!(cam instanceof Player player)) return 0;
		var ins = player.getEffect(YHEffects.DRUNK.get());
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

	public static void setProgress(int id, CombatProgress progress) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		if (level.getEntity(id) instanceof YoukaiEntity e) {
			e.setCombatProgress(progress.getProgress());
		}
	}
}
