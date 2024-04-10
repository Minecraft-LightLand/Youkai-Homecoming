package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Rarity;

import java.util.LinkedHashMap;
import java.util.Map;

public class YHDanmaku {

	public enum Type {
		CIRCLE, BALL, MENTOS, BUBBLE
	}

	public static final RegistryEntry<CreativeModeTab> TAB = YoukaisHomecoming.REGISTRATE
			.buildModCreativeTab("danmaku", "Youkai's Danmaku",
					e -> e.icon(YHDanmaku.SIMPLE.get(DyeColor.RED)::asStack));

	public static final Map<DyeColor, ItemEntry<DanmakuItem>> SIMPLE = new LinkedHashMap<>();

	static {
		for (var e : DyeColor.values()) {
			var ent = YoukaisHomecoming.REGISTRATE
					.item(e.getName() + "_circle_danmaku", p -> new DanmakuItem(p.rarity(Rarity.RARE), e, 1))
					.model((ctx, pvd) -> pvd.generated(ctx,
							pvd.modLoc("item/danmaku/circle"),
							pvd.modLoc("item/danmaku/circle_overlay")))
					.color(() -> () -> (stack, i) -> ((DanmakuItem) stack.getItem()).getDanmakuColor(stack, i))
					.tag(YHTagGen.CIRCLE_DANMAKU)
					.register();
			SIMPLE.put(e, ent);
		}
	}

	public static void register() {
	}

}
