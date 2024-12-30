package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.cuisinedelight.content.logic.CookTransformConfig;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.food.YHTea;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class YHConfigGen extends ConfigDataProvider {
	public YHConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Youkai Homecoming Config");
	}

	@Override
	public void add(Collector collector) {
		collector.add(CuisineDelight.INGREDIENT, YoukaisHomecoming.loc("ingredient"), IngredientConfig.build(
				IngredientConfig.get(Ingredient.of(YHFood.RAW_LAMPREY), FoodType.SEAFOOD,
						60, 120, 40, 0.5F, 0.5F, 2, 12,
						new IngredientConfig.EffectEntry(MobEffects.NIGHT_VISION, 0, 2400)
				),
				IngredientConfig.get(Ingredient.of(YHFood.RAW_LAMPREY_FILLET), FoodType.SEAFOOD,
						60, 120, 40, 0.5F, 0.5F, 1, 12,
						new IngredientConfig.EffectEntry(MobEffects.NIGHT_VISION, 0, 1800)
				),
				IngredientConfig.get(Ingredient.of(YHFood.ROE), FoodType.NONE,
						0, 40, 40, 0f, 0.5f, 1, 20,
						new IngredientConfig.EffectEntry(MobEffects.DIG_SPEED, 2, 2400)
				),
				IngredientConfig.get(Ingredient.of(YHTea.GREEN.leaves), FoodType.NONE,
						120, 240, 80, 0.5f, 1f, 0, 0,
						new IngredientConfig.EffectEntry(YHEffects.TEA, 1, 2400)
				),
				IngredientConfig.get(Ingredient.of(YHTea.BLACK.leaves), FoodType.NONE,
						120, 240, 80, 0.5f, 1f, 0, 0,
						new IngredientConfig.EffectEntry(YHEffects.TEA, 0, 2400),
						new IngredientConfig.EffectEntry(YHEffects.THICK, 0, 2400)
				),
				IngredientConfig.get(Ingredient.of(YHTea.WHITE.leaves), FoodType.NONE,
						120, 240, 80, 0.5f, 1f, 0, 0,
						new IngredientConfig.EffectEntry(YHEffects.TEA, 0, 2400),
						new IngredientConfig.EffectEntry(YHEffects.REFRESHING, 0, 2400)
				),
				IngredientConfig.get(Ingredient.of(YHTea.OOLONG.leaves), FoodType.NONE,
						120, 240, 80, 0.5f, 1f, 0, 0,
						new IngredientConfig.EffectEntry(YHEffects.TEA, 0, 2400),
						new IngredientConfig.EffectEntry(YHEffects.SMOOTHING, 0, 2400)
				)
		));

		collector.add(CuisineDelight.TRANSFORM, YoukaisHomecoming.loc("cooking"), new CookTransformConfig()
				.item(YHFood.RAW_LAMPREY.asItem(), YHFood.ROASTED_LAMPREY.asItem(), Stage.COOKED)
				.item(YHFood.RAW_LAMPREY_FILLET.asItem(), YHFood.ROASTED_LAMPREY_FILLET.asItem(), Stage.COOKED)
		);

	}

}
