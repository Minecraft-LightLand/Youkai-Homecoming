package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHDish {
	BAMBOO_MIZUYOKAN(Saucer.CERAMIC, Type.COOKED, 6, 0.6f, false, 4, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.SUGARS.tag),
	DRIED_FISH(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, true, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	IMITATION_BEAR_PAW(Saucer.CERAMIC, Type.STEAMED, 12, 0.8f, true, 3, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	PASTITSIO(Saucer.CERAMIC, Type.COOKED, 12, 0.8f, true, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag, DietTagGen.PROTEINS.tag),
	SAUCE_GRILLED_FISH(Saucer.PORCELAIN, Type.COOKED, 12, 0.8f, true, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.PROTEINS.tag),
	STINKY_TOFU(Saucer.CERAMIC, Type.COOKED, 8, 0.6f, false, 5, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	TOFU_BURGER(Saucer.CERAMIC, Type.COOKED, 8, 0.6f, false, 3, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SEVEN_COLORED_YOKAN(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, false, 4, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 3600, 1, 1)),
			DietTagGen.VEGETABLES.tag, DietTagGen.GRAINS.tag),

	COLD_TOFU(Saucer.CERAMIC, Type.COOKED, 8, 0.8f, false, 4, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	CUMBERLAND_LOIN(Saucer.CERAMIC, Type.COOKED, 10, 0.8f, true, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),
	SCHOLAR_GINKGO(Saucer.CERAMIC, Type.STEAMED, 6, 0.8f, false, 2, List.of(
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
			DietTagGen.VEGETABLES.tag),
	TOMATO_SAUCE_COD(Saucer.CERAMIC, Type.COOKED, 10, 0.8f, true, 2, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
			DietTagGen.PROTEINS.tag),

	;

	public final Saucer saucer;
	public final Type base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> raw, block;

	YHDish(Saucer saucer, Type type, int nutrition, float sat, boolean meat, int height, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.saucer = saucer;
		this.base = type;
		this.height = height;
		if (type == Type.STEAMED) {
			raw = YoukaisHomecoming.REGISTRATE
					.block("raw_" + getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd, false)))
					.item((block, p) -> new FoodSaucerItem(block, p.food(food(nutrition / 2, sat / 2, meat, List.of()))
							.craftRemainder(YHItems.SAUCER.asItem())))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName())))
					.tag(tags).build()
					.register();
		} else raw = null;
		block = YoukaisHomecoming.REGISTRATE
				.block(getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd, true)))
				.item((block, p) -> type.create(block, p.food(food(nutrition, sat, meat, effs))
						.craftRemainder(YHItems.SAUCER.asItem())))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName())))
				.tag(tags).build()
				.register();
	}

	private static FoodProperties food(int nutrition, float sat, boolean meat, List<EffectEntry> effs) {
		var builder = new FoodProperties.Builder()
				.nutrition(nutrition).saturationMod(sat);
		for (var e : effs) {
			builder.effect(e::getEffect, e.chance());
		}
		if (meat)
			builder.meat();
		return builder.build();
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private BlockModelBuilder build(RegistrateBlockstateProvider pvd, boolean extra) {
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

	public enum Type {
		COOKED, STEAMED, FLESH;

		public Item create(FoodSaucerBlock block, Item.Properties properties) {
			return new FoodSaucerItem(block, properties);
		}
	}

}
