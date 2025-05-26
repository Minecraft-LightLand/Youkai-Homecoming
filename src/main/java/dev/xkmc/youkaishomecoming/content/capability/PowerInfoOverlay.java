package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class PowerInfoOverlay implements IGuiOverlay {

	@Override
	public void render(ForgeGui gui, GuiGraphics g, float pTick, int w, int h) {
		if (Minecraft.getInstance().screen != null) return;
		var pl = Minecraft.getInstance().player;
		if (pl == null) return;
		var graze = GrazeCapability.HOLDER.get(pl);
		List<GrazeCapability.InfoLine> info = graze.getInfoLines();
		if (info.isEmpty()) return;
		var font = gui.getFont();
		int lh = font.lineHeight + 2;
		int th = lh * info.size();
		int tw = 0;
		for (var e : info) {
			tw = Math.max(tw, font.width(e.text()));
		}
		tw += 14;

		int xa = YHModConfig.CLIENT.powerInfoXAnchor.get();
		int xo = YHModConfig.CLIENT.powerInfoXOffset.get();
		int ya = YHModConfig.CLIENT.powerInfoYAnchor.get();
		int yo = YHModConfig.CLIENT.powerInfoYOffset.get();

		int x = xo + (xa + 1) * (w - tw) / 2;
		int y = yo + (ya + 1) * (h - th) / 2;

		for (var e : info) {
			g.blit(e.icon().loc(), x, y, e.x(), e.y(), 10, 10, e.icon().w(), e.icon().h());
			g.drawString(font, e.text(), x + 14, y, 0xffffffff, false);
			y += lh;
		}


	}

}

