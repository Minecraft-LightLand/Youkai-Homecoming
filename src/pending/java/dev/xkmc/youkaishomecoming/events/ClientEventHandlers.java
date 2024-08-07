package dev.xkmc.youkaishomecoming.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
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


	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() instanceof TouhouHatItem hat) {
			AttrTooltip.modifyTooltip(event.getToolTip(), hat.getAttributeModifiersForDisplay(), false);
		}
	}

}
