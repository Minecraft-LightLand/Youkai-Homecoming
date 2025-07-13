package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodBlockItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHDish {
	BAMBOO_MIZUYOKAN(Saucer.CERAMIC, DishType.COOKED, 6, 0.6f, 4, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.SUGARS.tag),
	DRIED_FISH(Saucer.CERAMIC, DishType.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	IMITATION_BEAR_PAW(Saucer.CERAMIC, DishType.STEAMED, 12, 0.8f, 3, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
			YHTagGen.STEAM_BLOCKER, DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	PASTITSIO(Saucer.CERAMIC, DishType.COOKED, 12, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	SAUCE_GRILLED_FISH(Saucer.PORCELAIN, DishType.COOKED, 12, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	STINKY_TOFU(Saucer.CERAMIC, DishType.COOKED, 8, 0.6f, 5, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	TOFU_BURGER(Saucer.CERAMIC, DishType.COOKED, 8, 0.6f, 3, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SEVEN_COLORED_YOKAN(Saucer.CERAMIC, DishType.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 3600, 1, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag),

	COLD_TOFU(Saucer.CERAMIC, DishType.COOKED, 8, 0.8f, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	CUMBERLAND_LOIN(Saucer.CERAMIC, DishType.COOKED, 10, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SCHOLAR_GINKGO(Saucer.CERAMIC, DishType.STEAMED, 6, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			YHTagGen.STEAM_BLOCKER, DietTagGen.VEGETABLES.tag),
	TOMATO_SAUCE_COD(Saucer.CERAMIC, DishType.COOKED, 10, 0.8f, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),

	;

	public final Saucer saucer;
	public final DishType base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> raw, block;

	YHDish(Saucer saucer, DishType type, int nutrition, float sat, int height, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.saucer = saucer;
		this.base = type;
		this.height = height;
		if (type == DishType.STEAMED) {
			raw = buildBlock("raw_" + getName(), true, nutrition, sat, effs, tags);
		} else raw = null;
		block = buildBlock(getName(), false, nutrition, sat, effs, tags);
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private BlockEntry<FoodSaucerBlock> buildBlock(String name, boolean raw, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		return YoukaisHomecoming.REGISTRATE
				.block(name, p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), buildModel(pvd, !raw)))
				.item((block, p) -> base.create(block, p, raw, nutrition, sat, effs))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName())))
				.tag(tags).build()
				.register();
	}

	private BlockModelBuilder buildModel(RegistrateBlockstateProvider pvd, boolean extra) {
		String name = getName();
		var builder = pvd.models().getBuilder("block/" + name)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/dish/" + name)));
		for (var e : saucer.tex) {
			builder.texture(e, "block/dish/" + e);
		}
		builder.texture("particle", "block/dish/" + saucer.tex[0]);
		builder.texture("base", "block/dish/" + name + "_base");
		if (extra) {
			builder.texture("detail", "block/dish/" + name + "_detail");
		}
		builder.renderType("cutout");
		return builder;
	}

	public static void register() {
	}

	public enum DishType {
		COOKED(FoodType.SAUCER),
		STEAMED(FoodType.SAUCER);

		private final FoodType type;

		DishType(FoodType type) {
			this.type = type;
		}

		public Item create(FoodSaucerBlock block, Item.Properties p, boolean raw, int nutrition, float sat, List<EffectEntry> effs) {
			type.food(p, raw ? 0.5f : 1, nutrition, sat, effs);
			return new FoodBlockItem(block, p);
		}

	}

}
