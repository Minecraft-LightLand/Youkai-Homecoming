package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.l2library.base.overlay.OverlayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class TileInfoDisplay implements IGuiOverlay {

	@Override
	public void render(ForgeGui gui, GuiGraphics g, float pTick, int sw, int sh) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;
		var hit = Minecraft.getInstance().hitResult;
		if (!(hit instanceof BlockHitResult bhit)) return;
		if (!(level.getBlockEntity(bhit.getBlockPos()) instanceof InfoTile tile)) return;
		new ImageBox(g, (int) (sw * 0.7), (int) (sh * 0.5), 0, new TileClientTooltip(tile.getTooltip()))
				.render();
	}

	public static class ImageBox extends OverlayUtil {

		private final TileClientTooltip tooltip;

		public ImageBox(GuiGraphics g, int x0, int y0, int maxW, TileClientTooltip tooltip) {
			super(g, x0, y0, maxW);
			this.tooltip = tooltip;
		}

		public void render() {
			renderTooltipInternal(Minecraft.getInstance().font, List.of(tooltip));
		}

	}

}
