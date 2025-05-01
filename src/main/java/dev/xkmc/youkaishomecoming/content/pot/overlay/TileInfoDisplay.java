package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class TileInfoDisplay implements LayeredDraw.Layer {

	@Override
	public void render(GuiGraphics g, DeltaTracker delta) {
		int sw = g.guiWidth(), sh = g.guiHeight();
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;
		var hit = Minecraft.getInstance().hitResult;
		if (!(hit instanceof BlockHitResult bhit)) return;
		if (!(level.getBlockEntity(bhit.getBlockPos()) instanceof InfoTile tile)) return;
		new ImageBox(g, (int) (sw * 0.7), (int) (sh * 0.5), 0)
				.render(tile, Screen.hasShiftDown(), bhit);
	}

	public static class ImageBox extends OverlayUtil {

		public ImageBox(GuiGraphics g, int x0, int y0, int maxW) {
			super(g, x0, y0, maxW);
		}

		public void render(InfoTile tile, boolean shift, BlockHitResult hit) {
			List<ClientTooltipComponent> tooltip = new ArrayList<>();
			tooltip.add(new TileClientTooltip(tile.getImage(shift, hit)));
			for (var e : tile.lines(shift, hit)) {
				tooltip.add(new ClientTextTooltip(e.getVisualOrderText()));
			}
			renderTooltipInternal(Minecraft.getInstance().font, tooltip);
		}

	}

}
