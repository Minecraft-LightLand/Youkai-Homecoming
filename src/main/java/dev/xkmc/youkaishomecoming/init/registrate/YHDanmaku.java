package dev.xkmc.youkaishomecoming.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.fastprojectileapi.render.core.DisplayType;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuPoofParticleOptions;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class YHDanmaku {

	public interface IDanmakuType {
		int damage();
	}

	public enum Bullet implements IDanmakuType {
		CIRCLE(1, 4, DisplayType.SOLID),
		BALL(1, 4, DisplayType.SOLID),
		MENTOS(2, 6, DisplayType.SOLID),
		BUBBLE(4, 8, DisplayType.ADDITIVE),
		BUTTERFLY(1, 4, DisplayType.TRANSPARENT),
		SPARK(1, 4, DisplayType.SOLID),
		STAR(2, 6, DisplayType.TRANSPARENT),
		;

		public final String name;
		public final TagKey<Item> tag;
		public final float size;
		private final int damage;
		private final DisplayType display;

		Bullet(float size, int damage, DisplayType display) {
			this.size = size;
			this.damage = damage;
			this.display = display;
			name = name().toLowerCase(Locale.ROOT);
			tag = YHTagGen.item(name + "_danmaku");
		}


		public ItemEntry<DanmakuItem> get(DyeColor color) {
			return YHDanmaku.DANMAKU[ordinal()][color.ordinal()];
		}

		public int damage() {
			return damage;
		}

		public boolean bypass() {
			return size > 1;
		}

		public String getName() {
			return name;
		}

		public DisplayType display() {
			return display;
		}

	}

	public enum Laser implements IDanmakuType {
		LASER(1, 4);

		public final String name;
		public final TagKey<Item> tag;
		public final float size;
		private final int damage;

		Laser(float size, int damage) {
			this.size = size;
			this.damage = damage;
			name = name().toLowerCase(Locale.ROOT);
			tag = YHTagGen.item(name);
		}

		public ItemEntry<LaserItem> get(DyeColor color) {
			return YHDanmaku.LASER[ordinal()][color.ordinal()];
		}

		public int damage() {
			return damage;
		}

	}

	public static final RegistryEntry<CreativeModeTab> TAB = YoukaisHomecoming.REGISTRATE
			.buildModCreativeTab("danmaku", "Youkai's Danmaku",
					e -> e.icon(YHDanmaku.DANMAKU[0][DyeColor.RED.ordinal()]::asStack));

	private static final ItemEntry<DanmakuItem>[][] DANMAKU;

	private static final ItemEntry<LaserItem>[][] LASER;

	public static final RegistryEntry<ParticleType<DanmakuPoofParticleOptions>> POOF;

	static {
		DANMAKU = new ItemEntry[Bullet.values().length][DyeColor.values().length];
		for (var t : Bullet.values()) {
			for (var e : DyeColor.values()) {
				var ent = YoukaisHomecoming.REGISTRATE
						.item(e.getName() + "_" + t.name + "_danmaku", p -> new DanmakuItem(p.rarity(Rarity.RARE), t, e, t.size))
						.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/bullet/" + t.name + "/" + e.getName())))
						.tag(t.tag)
						.register();
				DANMAKU[t.ordinal()][e.ordinal()] = ent;
			}
		}

		LASER = new ItemEntry[Laser.values().length][DyeColor.values().length];
		for (var t : Laser.values()) {
			for (var e : DyeColor.values()) {
				var ent = YoukaisHomecoming.REGISTRATE
						.item(e.getName() + "_" + t.name, p -> new LaserItem(p.rarity(Rarity.RARE), t, e, 1))
						.model((ctx, pvd) -> pvd.generated(ctx,
								pvd.modLoc("item/danmaku/" + t.name),
								pvd.modLoc("item/danmaku/" + t.name + "_overlay")))
						.color(() -> () -> (stack, i) -> ((LaserItem) stack.getItem()).getDanmakuColor(stack, i))
						.tag(t.tag)
						.register();
				LASER[t.ordinal()][e.ordinal()] = ent;
			}
		}

		POOF = YoukaisHomecoming.REGISTRATE.simple("danmaku_poof",
				ForgeRegistries.Keys.PARTICLE_TYPES,
				() -> new ParticleType<>(false, DanmakuPoofParticleOptions.DESERIALIZER) {
					@Override
					public Codec<DanmakuPoofParticleOptions> codec() {
						return DanmakuPoofParticleOptions.CODEC;
					}
				});

	}

	public static void register() {
	}

}
