package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHDish implements IYHDish {
	BAMBOO_MIZUYOKAN(Saucer.CERAMIC, Type.COOKED, 6, 0.6f, 4, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.SUGARS.tag),
	DRIED_FISH(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	IMITATION_BEAR_PAW(Saucer.CERAMIC, Type.STEAMED, 12, 0.8f, 3, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	PASTITSIO(Saucer.CERAMIC, Type.COOKED, 12, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	SAUCE_GRILLED_FISH(Saucer.PORCELAIN, Type.COOKED, 12, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	STINKY_TOFU(Saucer.CERAMIC, Type.COOKED, 8, 0.6f, 5, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	TOFU_BURGER(Saucer.CERAMIC, Type.COOKED, 8, 0.6f, 3, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SEVEN_COLORED_YOKAN(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 3600, 1, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag),

	COLD_TOFU(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	CUMBERLAND_LOIN(Saucer.CERAMIC, Type.COOKED, 10, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SCHOLAR_GINKGO(Saucer.CERAMIC, Type.STEAMED, 6, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag),
	TOMATO_SAUCE_COD(Saucer.CERAMIC, Type.COOKED, 10, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	;

	public final Saucer saucer;
	public final Type base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> raw, block;

	YHDish(Saucer saucer, Type type, int nutrition, float sat, int height, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.saucer = saucer;
		this.base = type;
		this.height = height;
		if (type == Type.STEAMED) {
			raw = buildBlock(YoukaisHomecoming.REGISTRATE, type, true, false, nutrition, sat, effs, tags);
		} else raw = null;
		block = buildBlock(YoukaisHomecoming.REGISTRATE, type, false, false, nutrition, sat, effs, tags);
	}

	@Override
	public BlockEntry<FoodSaucerBlock> block() {
		return block;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public Saucer base() {
		return saucer;
	}

	@Override
	public int height() {
		return height;
	}

	public static void register() {
	}

	public enum Type {
		COOKED, STEAMED, FLESH;

		public Item create(FoodSaucerBlock block, Item.Properties properties) {
			return new FoodSaucerItem(block, properties);
		}
	}

}
