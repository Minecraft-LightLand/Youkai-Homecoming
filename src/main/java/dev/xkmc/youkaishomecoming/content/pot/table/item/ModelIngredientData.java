package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;

@SerialClass
public class ModelIngredientData extends BaseConfig {

	@SerialClass.SerialField
	@ConfigCollect(CollectType.MAP_COLLECT)
	public LinkedHashMap<ResourceLocation, LinkedHashMap<String, Ingredient>> ingredients = new LinkedHashMap<>();

	public void onSync() {
		var map = VariantModelPart.getAll();
		for (var ent : ingredients.entrySet()) {
			var part = map.get(ent.getKey());
			if (part == null) continue;
			for (var e : ent.getValue().entrySet()) {
				var ing = e.getValue();
				part.addMapping(e.getKey(), () -> ing);
			}
		}
	}
}
