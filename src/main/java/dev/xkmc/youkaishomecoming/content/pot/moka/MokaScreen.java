package dev.xkmc.youkaishomecoming.content.pot.moka;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotScreen;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class MokaScreen extends BasePotScreen<MokaMenu> {

	private static final ResourceLocation BG = YoukaisHomecoming.loc("textures/gui/moka.png");
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
