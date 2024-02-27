package dev.xkmc.youkaihomecoming.content.pot.moka;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotScreen;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class MokaScreen extends BasePotScreen<MokaMenu> {

	private static final ResourceLocation BG = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/moka.png");
	private static final Rectangle HEAT_ICON = new Rectangle(44, 57, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(74, 28, 38, 17);

	public MokaScreen(MokaMenu screenContainer, Inventory inv, Component titleIn) {
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
