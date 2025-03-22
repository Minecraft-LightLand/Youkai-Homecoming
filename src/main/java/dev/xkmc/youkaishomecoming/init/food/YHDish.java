package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum YHDish {
	BAMBOO_MIZUYOKAN(Type.COOKED, 6, 0.6f, false, 4,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	DRIED_FISH(Type.COOKED, 8, 0.8f, true, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	IMITATION_BEAR_PAW(Type.STEAMED, 12, 0.8f, true, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
	PASTITSIO(Type.COOKED, 12, 0.8f, true, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SAUCE_GRILLED_FISH(Type.COOKED, 12, 0.8f, true, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	STINKY_TOFU(Type.COOKED, 8, 0.6f, false, 5,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	TOFU_BURGER(Type.COOKED, 8, 0.6f, false, 3,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	BLOOD_CURD(Type.COOKED, 8, 0.8f, true, 2,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SEVEN_COLORED_YOKAN(Type.COOKED, 8, 0.8f, false, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 3600, 1, 1)),

	BLOODY_FLESH(Type.COOKED, 6, 0.8f, true, 3,//TODO flesh, recipes
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	COLD_TOFU(Type.COOKED, 8, 0.8f, false, 4,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	CUMBERLAND_LOIN(Type.COOKED, 12, 0.8f, true, 2,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SCHOLAR_GINKGO(Type.STEAMED, 10, 0.8f, false, 2,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	TOMATO_SAUCE_HORSE_MACKEREL(Type.COOKED, 12, 0.8f, true, 2,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),

	;

	public final Type base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> raw, block;

	YHDish(Type base, int nutrition, float sat, boolean meat, int height, EffectEntry... effs) {
		this.base = base;
		this.height = height;
		var builder = new FoodProperties.Builder()
				.nutrition(nutrition).saturationMod(sat);
		for (var e : effs) {
			builder.effect(e::getEffect, e.chance());
		}
		if (meat)
			builder.meat();
		var food = builder.build();
		if (base == Type.STEAMED) {
			raw = YoukaisHomecoming.REGISTRATE
					.block("raw_" + getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd, false)))
					.item((block, p) -> new FoodSaucerItem(block, p.craftRemainder(YHItems.SAUCER.asItem())))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName()))).build()
					.register();
		} else raw = null;
		block = YoukaisHomecoming.REGISTRATE
				.block(getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd, base != Type.RAW)))
				.item((block, p) -> new FoodSaucerItem(block, p.food(food).craftRemainder(YHItems.SAUCER.asItem())))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName()))).build()
				.register();
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private BlockModelBuilder build(RegistrateBlockstateProvider pvd, boolean extra) {
		String name = getName();
		var builder = pvd.models().getBuilder("block/" + name)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/dish/" + name)));
		builder.texture("saucer", "block/saucer");
		builder.texture("base", "block/dish/" + name + "_base");
		builder.texture("particle", "block/dish/" + name + "_base");
		if (extra) {
			builder.texture("detail", "block/dish/" + name + "_detail");
		}
		builder.renderType("cutout");
		return builder;
	}

	public boolean isFlesh() {
		return this == BLOOD_CURD;
	}

	public static void register() {
	}

	public enum Type {
		COOKED, STEAMED, RAW
	}

}
