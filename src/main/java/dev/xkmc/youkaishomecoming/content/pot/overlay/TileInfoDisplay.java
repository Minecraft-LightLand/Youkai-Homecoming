package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.l2library.base.overlay.OverlayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

public class TileInfoDisplay implements IGuiOverlay {

	@Override
	public void render(ForgeGui gui, GuiGraphics g, float pTick, int sw, int sh) {
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
			var img = tile.getImage(shift, hit);
			if (img != null)
				tooltip.add(new TileClientTooltip(img));
			for (var e : tile.lines(shift, hit)) {
				tooltip.add(new ClientTextTooltip(e.getVisualOrderText()));
			}
			if (tooltip.isEmpty()) return;
			renderTooltipInternal(Minecraft.getInstance().font, tooltip);
		}

	}

}
