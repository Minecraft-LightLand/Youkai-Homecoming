package dev.xkmc.youkaishomecoming.content.pot.overlay;

import dev.xkmc.l2library.base.overlay.OverlayUtil;
import dev.xkmc.l2modularblock.DelegateBlockImpl;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HintOverlay implements IGuiOverlay {

	private int start = 0;
	private BlockPos pos = null;

	@Override
	public void render(ForgeGui gui, GuiGraphics g, float pTick, int w, int h) {
		int prev = start;
		start = 0;
		var player = Minecraft.getInstance().player;
		if (player == null || Minecraft.getInstance().screen != null) return;
		var hit = Minecraft.getInstance().hitResult;
		if (!(hit instanceof BlockHitResult bhit)) return;
		var level = player.level();
		IHintableBlock be = getBlock(level, bhit.getBlockPos());
		if (be == null) return;
		if (prev == 0 || prev > player.tickCount || pos == null || !pos.equals(bhit.getBlockPos()))
			prev = player.tickCount;
		pos = bhit.getBlockPos();
		start = prev;
		int time = player.tickCount - start;
		if (time < 15) return;
		var list = be.getHints(level, pos);
		if (list.isEmpty()) return;
		var stacks = compile(list);
		int total = stacks.size();
		int n = Math.min(total, 12);
		ItemStack[] display = new ItemStack[n];
		for (int i = 0; i < n; i++) {
			var arr = stacks.get(i);
			if (arr.length == 0) {
				display[i] = YHItems.EMPTY_HAND_ICON.asStack(1);
				continue;
			}
			display[i] = arr[time / 15 % arr.length];
		}
		new ImageBox(g, (int) (w * 0.7), (int) (h * 0.5), 0)
				.render(display, Math.min(4, n), Math.min(3, (n - 1) / 4 + 1), total - n);
	}

	@Nullable
	private IHintableBlock getBlock(Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof IHintableBlock be)
			return be;
		var state = level.getBlockState(pos);
		if (state.getBlock() instanceof IHintableBlock b) {
			return b;
		}
		if (state.getBlock() instanceof DelegateBlockImpl del) {
			var opt = del.getImpl().one(IHintableBlock.class);
			if (opt.isPresent()) return opt.get();
		}
		return null;
	}

	private List<ItemStack[]> compile(List<Ingredient> list) {
		Int2ObjectLinkedOpenHashMap<ItemStack[]> set = new Int2ObjectLinkedOpenHashMap<>();
		for (var e : list) {
			if (e.isEmpty()) {
				set.put(1, new ItemStack[0]);
				continue;
			}
			var stacks = e.getItems();
			int result = 1;
			for (var stack : stacks) {
				int hash;
				if (stack.isEmpty()) {
					hash = 0;
				} else {
					hash = BuiltInRegistries.ITEM.getId(stack.getItem());
					var tag = stack.getTag();
					if (tag != null) {
						hash += tag.hashCode() * 15;
					}
				}
				result = 31 * result + hash;
			}
			set.put(result, stacks);
		}
		return new ArrayList<>(set.values());
	}

	public static class ImageBox extends OverlayUtil {

		public ImageBox(GuiGraphics g, int x0, int y0, int maxW) {
			super(g, x0, y0, maxW);
		}

		public void render(ItemStack[] stacks, int w, int h, int extra) {
			List<ClientTooltipComponent> tooltip = new ArrayList<>();
			tooltip.add(new ClientTextTooltip(YHLangData.CUISINE_ALLOW.get().getVisualOrderText()));
			tooltip.add(new TileClientTooltip(List.of(stacks), List.of(), w, h));
			if (extra > 0) {
				tooltip.add(new ClientTextTooltip(YHLangData.CUISINE_EXTRA.get("" + extra).getVisualOrderText()));
			}
			renderTooltipInternal(Minecraft.getInstance().font, tooltip);

		}

	}
}
