package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaihomecoming.content.block.FoodSaucerBlock;
import dev.xkmc.youkaihomecoming.content.item.FoodSaucerItem;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum YHDish {
	BAMBOO_MIZUYOKAN(Saucer.SAUCER_1, 6, 0.6f, false, 6,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	DRIED_FISH(Saucer.SAUCER_2, 8, 0.8f, true, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	IMITATION_BEAR_PAW(Saucer.SAUCER_4, 12, 0.8f, true, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
	PASTITSIO(Saucer.SAUCER_2, 12, 0.8f, true, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SAUCE_GRILLED_FISH(Saucer.SAUCER_4, 12, 0.8f, true, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	STINKY_TOFU(Saucer.SAUCER_1, 8, 0.6f, false, 6,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	TOFU_BURGER(Saucer.SAUCER_2, 8, 0.6f, false, 3,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	BLOOD_CURD(Saucer.SAUCER_3, 8, 0.8f, true, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	;

	public final Saucer base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> block;

	YHDish(Saucer base, int nutrition, float sat, boolean meat, int height, EffectEntry... effs) {
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
		block = YoukaiHomecoming.REGISTRATE
				.block(getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd)))
				.item((block, p) -> new FoodSaucerItem(block, p.food(food)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName()))).build()
				.register();
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private BlockModelBuilder build(RegistrateBlockstateProvider pvd) {
		String name = getName();
		var builder = pvd.models().getBuilder("block/" + name)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/saucer_" + name)));
		builder.texture("base", "block/" + name);
		builder.texture("particle", "block/" + name);
		if (base.extra) {
			builder.texture("extra", "block/" + name + "_extra");
		}
		for (var e : base.tex) {
			builder.texture(e, "block/saucer_" + e);
		}
		builder.renderType("cutout");
		return builder;
	}

	public static void register() {
	}

}
