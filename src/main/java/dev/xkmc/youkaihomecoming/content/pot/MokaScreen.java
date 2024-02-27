package dev.xkmc.youkaihomecoming.content.pot;

import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MokaScreen extends BasePotScreen<MokaMenu> {

	private static final ResourceLocation BG = new ResourceLocation(YoukaiHomecoming.MODID, "textures/gui/moka_maker.png");

	public MokaScreen(MokaMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public ResourceLocation getBackgroundTexture() {
		return BG;
	}
}
