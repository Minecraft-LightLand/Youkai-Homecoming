package dev.xkmc.youkaihomecoming.content.pot.kettle;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotScreen;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class KettleScreen extends BasePotScreen<KettleMenu> {

	private static final ResourceLocation BG = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/kettle.png");
	private static final Rectangle HEAT_ICON = new Rectangle(44, 64, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(74, 28, 35, 17);
	private static final Rectangle WATER_LINE = new Rectangle(36, 56, 32, 5);

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

	protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(gui, partialTicks, mouseX, mouseY);
		if (minecraft != null) {
			int l = menu.getWater() * WATER_LINE.width / KettleBlockEntity.WATER_BUCKET;
			gui.blit(getBackgroundTexture(),
					leftPos + WATER_LINE.x,
					topPos + WATER_LINE.y,
					176 + (WATER_LINE.width - l),
					getHeatIcon().height + getProgressArrow().height,
					l + 1,
					WATER_LINE.height
			);
		}
	}

}
