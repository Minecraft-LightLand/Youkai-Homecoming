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
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum YHDish {
	BAMBOO_MIZUYOKAN(Saucer.SAUCER_1, 6, 0.6f, 6,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	DRIED_FISH(Saucer.SAUCER_2, 8, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	IMITATION_BEAR_PAW(Saucer.SAUCER_4, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(MobEffects.DAMAGE_BOOST, 3600, 1, 1),
			new EffectEntry(MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1)),
	PASTITSIO(Saucer.SAUCER_2, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SAUCE_GRILLED_FISH(Saucer.SAUCER_4, 12, 0.8f, 3,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	STINKY_TOFU(Saucer.SAUCER_1, 8, 0.6f, 6,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1)),
	TOFU_BURGER(Saucer.SAUCER_2, 8, 0.6f, 3,
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	BLOOD_CURD(Saucer.SAUCER_3, 8, 0.8f, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	SEVEN_COLORED_YOKAN(Saucer.SAUCER_1, 8, 0.8f, 6,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 3600, 1, 1)),
	;

	public final Saucer base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> block;

	YHDish(Saucer base, int nutrition, float sat, int height, EffectEntry... effs) {
		this.base = base;
		this.height = height;
		var builder = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		for (var e : effs) {
			builder.effect(e::getEffect, e.chance());
		}
		var food = builder.build();
		block = YoukaisHomecoming.REGISTRATE
				.block(getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd)))
				.item((block, p) -> new FoodSaucerItem(block, p.food(food).craftRemainder(YHItems.SAUCER.asItem())))
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
