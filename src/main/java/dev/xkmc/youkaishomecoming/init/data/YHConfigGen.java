package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.youkaishomecoming.content.pot.table.item.ModelIngredientData;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public class YHConfigGen extends ConfigDataProvider {

	public YHConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Youkai Homecoming Config");
	}

	@Override
	public void add(Collector collector) {
		ModelIngredientData ans = new ModelIngredientData();
		for (var ent : VariantModelPart.getAll().entrySet()) {
			var map = new LinkedHashMap<String, Ingredient>();
			for (var pair : ent.getValue().ingredients.entrySet()) {
				map.put(pair.getKey(), pair.getValue().getIngredient());
			}
			ans.ingredients.put(ent.getKey(), map);
		}
		collector.add(YoukaisHomecoming.INGREDIENT, YoukaisHomecoming.loc("default"), ans);
	}

}
