package dev.xkmc.youkaihomecoming.content.pot.kettle;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotScreen;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class KettleScreen extends BasePotScreen<KettleMenu> {

	private static final ResourceLocation BG = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/kettle.png");
	private static final Rectangle HEAT_ICON = new Rectangle(44, 64, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(74, 28, 35, 17);

	public KettleScreen(KettleMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public ResourceLocation getBackgroundTexture() {
		return BG;
	}

	@Override
	public Rectangle getHeatIcon() {
		return HEAT_ICON;
	}

	@Override
	public Rectangle getProgressArrow() {
		return PROGRESS_ARROW;
	}

}
