package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.Locale;

public class YHDanmaku {

	public enum Bullet {
		CIRCLE(1), BALL(1), MENTOS(2), BUBBLE(4);

		public final String name;
		public final TagKey<Item> tag;
		public final float size;

		Bullet(float size) {
			this.size = size;
			name = name().toLowerCase(Locale.ROOT);
			tag = YHTagGen.item(name + "_danmaku");
		}

		public ItemEntry<DanmakuItem> get(DyeColor color) {
			return DANMAKU[ordinal()][color.ordinal()];
		}
	}

	public static final RegistryEntry<CreativeModeTab> TAB = YoukaisHomecoming.REGISTRATE
			.buildModCreativeTab("danmaku", "Youkai's Danmaku",
					e -> e.icon(YHDanmaku.DANMAKU[0][DyeColor.RED.ordinal()]::asStack));

	private static final ItemEntry<DanmakuItem>[][] DANMAKU;

	private static final ItemEntry<LaserItem>[] LASER;

	static {
		DANMAKU = new ItemEntry[Bullet.values().length][DyeColor.values().length];
		for (var t : Bullet.values()) {
			for (var e : DyeColor.values()) {
				var ent = YoukaisHomecoming.REGISTRATE
						.item(e.getName() + "_" + t.name + "_danmaku", p -> new DanmakuItem(p.rarity(Rarity.RARE), t, e, t.size))
						.model((ctx, pvd) -> pvd.generated(ctx,
								pvd.modLoc("item/danmaku/" + t.name),
								pvd.modLoc("item/danmaku/" + t.name + "_overlay")))
						.color(() -> () -> (stack, i) -> ((DanmakuItem) stack.getItem()).getDanmakuColor(stack, i))
						.tag(t.tag)
						.register();
				DANMAKU[t.ordinal()][e.ordinal()] = ent;
			}
		}

		LASER = new ItemEntry[DyeColor.values().length];

		for (var e : DyeColor.values()) {
			var ent = YoukaisHomecoming.REGISTRATE
					.item(e.getName() + "_laser", p -> new LaserItem(p.rarity(Rarity.RARE), e, 1))
					.model((ctx, pvd) -> pvd.generated(ctx,
							pvd.modLoc("item/danmaku/laser"),
							pvd.modLoc("item/danmaku/laser_overlay")))
					.color(() -> () -> (stack, i) -> ((LaserItem) stack.getItem()).getDanmakuColor(stack, i))
					//TODO tag
					.register();
			LASER[e.ordinal()] = ent;
		}
	}

	public static void register() {
	}

}
