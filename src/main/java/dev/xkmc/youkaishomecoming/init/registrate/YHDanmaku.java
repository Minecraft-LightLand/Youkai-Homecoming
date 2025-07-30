package dev.xkmc.youkaishomecoming.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.fastprojectileapi.render.core.DisplayType;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuPoofParticleOptions;
import dev.xkmc.youkaishomecoming.content.item.danmaku.CustomSpellItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.SpellItem;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.HomingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.RingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.player.*;
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
			tag = YHTagGen.item("danmaku/" + name);
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
		LASER(1, 1, 4), PENCIL(1, 1.75f, 4);

		public final String name;
		public final TagKey<Item> tag;
		public final float size, visualLength;
		private final int damage;

		Laser(float size, float visualLength, int damage) {
			this.size = size;
			this.visualLength = visualLength;
			this.damage = damage;
			name = name().toLowerCase(Locale.ROOT);
			tag = YHTagGen.item("laser/" + name);
		}

		public ItemEntry<LaserItem> get(DyeColor color) {
			return YHDanmaku.LASER[ordinal()][color.ordinal()];
		}

		public int damage() {
			return damage;
		}

		public boolean setupLength() {
			return this != LASER;
		}

		public float visualLength() {
			return visualLength;
		}
	}

	public static final RegistryEntry<CreativeModeTab> TAB = YoukaisHomecoming.REGISTRATE
			.buildModCreativeTab("danmaku", "Youkai's Danmaku",
					e -> e.icon(YHDanmaku.DANMAKU[0][DyeColor.RED.ordinal()]::asStack));

	private static final ItemEntry<DanmakuItem>[][] DANMAKU;

	private static final ItemEntry<LaserItem>[][] LASER;

	public static final RegistryEntry<ParticleType<DanmakuPoofParticleOptions>> POOF;

	public static final ItemEntry<SpellItem> REIMU_SPELL;
	public static final ItemEntry<SpellItem> MARISA_SPELL;
	public static final ItemEntry<SpellItem> SANAE_SPELL;
	public static final ItemEntry<SpellItem> KOISHI_SPELL;
	public static final ItemEntry<SpellItem> MYSTIA_SPELL;
	public static final ItemEntry<SpellItem> REMILIA_SPELL;
	public static final ItemEntry<SpellItem> YUKARI_SPELL_LASER;
	public static final ItemEntry<SpellItem> YUKARI_SPELL_BUTTERFLY;
	public static final ItemEntry<SpellItem> CLOWNPIECE_SPELL;
	public static final ItemEntry<CustomSpellItem> CUSTOM_SPELL_RING;
	public static final ItemEntry<CustomSpellItem> CUSTOM_SPELL_HOMING;

	static {

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YHDanmaku.TAB.getKey());

		// spell
		{

			CUSTOM_SPELL_RING = YoukaisHomecoming.REGISTRATE
					.item("custom_spell_ring", p -> new CustomSpellItem(p.stacksTo(1), false, RingSpellFormData.FLOWER))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/custom_spell")))
					.tag(YHTagGen.CUSTOM_SPELL)
					.register();

			CUSTOM_SPELL_HOMING = YoukaisHomecoming.REGISTRATE
					.item("custom_spell_homing", p -> new CustomSpellItem(p.stacksTo(1), true, HomingSpellFormData.RING))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/custom_spell")))
					.tag(YHTagGen.CUSTOM_SPELL)
					.register();

			REIMU_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_reimu", p -> new SpellItem(
							p.stacksTo(1), ReimuItemSpell::new, true,
							() -> Bullet.CIRCLE.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Reimu's Spellcard \"Innate Dream\"")
					.register();

			MARISA_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_marisa", p -> new SpellItem(
							p.stacksTo(1), MarisaItemSpell::new, false,
							() -> Laser.LASER.get(DyeColor.WHITE).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Marisa's Spellcard \"Master Spark\"")
					.register();

			SANAE_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_sanae", p -> new SpellItem(
							p.stacksTo(1), SanaeItemSpell::new, false,
							() -> Bullet.SPARK.get(DyeColor.GREEN).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Sanae's Spellcard \"Inherited Ritual\"")
					.register();

			MYSTIA_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_mystia", p -> new SpellItem(
							p.stacksTo(1), MystiaItemSpell::new, false,
							() -> Bullet.MENTOS.get(DyeColor.GREEN).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Night Sparrow \"Midnight Chorus Master\"")
					.register();

			KOISHI_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_koishi", p -> new SpellItem(
							p.stacksTo(1), KoishiItemSpell::new, false,
							() -> Laser.LASER.get(DyeColor.BLUE).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Response \"Youkai Polygraph\"")
					.register();

			REMILIA_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_remilia", p -> new SpellItem(
							p.stacksTo(1), RemiliaItemSpell::new, false,
							() -> Bullet.BUBBLE.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName())))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Scarlet Sign \"Scarlet Meister\"")
					.register();

			YUKARI_SPELL_LASER = YoukaisHomecoming.REGISTRATE
					.item("spell_yukari_laser", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellLaser::new, false,
							() -> Laser.LASER.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/spell_yukari")))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Barrier \"Mesh of Light & Darkness\"")
					.register();

			YUKARI_SPELL_BUTTERFLY = YoukaisHomecoming.REGISTRATE
					.item("spell_yukari_butterfly", p -> new SpellItem(
							p.stacksTo(1), YukariItemSpellButterfly::new, false,
							() -> Bullet.BUTTERFLY.get(DyeColor.MAGENTA).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/spell_yukari")))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Barrier \"Double Black Death Butterfly\"")
					.register();

			CLOWNPIECE_SPELL = YoukaisHomecoming.REGISTRATE
					.item("spell_clownpiece", p -> new SpellItem(
							p.stacksTo(1), ClownItemSpell::new, true,
							() -> Laser.LASER.get(DyeColor.RED).get()))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/spell/spell_clownpiece")))
					.tag(YHTagGen.PRESET_SPELL)
					.lang("Hell Sign \"Star and Stripe\"")
					.register();
		}

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
