package dev.xkmc.youkaishomecoming.content.pot.table.item;

import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2core.serial.config.ConfigLoadOnStart;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.LinkedHashMap;

@SerialClass
@ConfigLoadOnStart
public class ModelIngredientData extends BaseConfig {

	@SerialField
	@ConfigCollect(CollectType.MAP_COLLECT)
	public LinkedHashMap<ResourceLocation, LinkedHashMap<String, Ingredient>> ingredients = new LinkedHashMap<>();

	public void postMerge() {
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
