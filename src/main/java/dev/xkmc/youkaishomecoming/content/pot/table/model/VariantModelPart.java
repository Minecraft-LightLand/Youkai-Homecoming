package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.Lazy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VariantModelPart {

	private final ResourceLocation path;
	private final int max;

	private final Map<String, Entry> ingredients = new LinkedHashMap<>();

	public VariantModelPart(ResourceLocation path, int max) {
		this.path = path;
		this.max = max;
	}

	public void addModels(List<ResourceLocation> list) {
		for (var ent : ingredients.entrySet()) {
			String id = ent.getKey();
			for (int i = 0; i < max; i++) {
				list.add(path.withSuffix("/" + id + "/" + i));
			}
		}
	}

	public void build(TableModelProvider pvd) {
		for (var ent : ingredients.entrySet()) {
			String id = ent.getKey();
			for (int i = 0; i < max; i++) {
				pvd.create(path.withSuffix("/" + id + "/" + i), path)
						.tex(id, ent.getValue().tex);
			}
		}
	}

	public synchronized Entry addMapping(String id, Supplier<Ingredient> ing) {
		var ans = new Entry(id, Lazy.of(ing), path.withPath(e -> "block/table/" + e + "/" + id));
		ingredients.put(id, ans);
		return ans;
	}

	public Entry addMapping(String id, ItemLike item) {
		return addMapping(id, () -> Ingredient.of(item));
	}

	public Entry addMapping(String id, TagKey<Item> tag) {
		return addMapping(id, () -> Ingredient.of(tag));
	}

	public static class Entry {

		private final String id;
		private final Lazy<Ingredient> ingredient;
		private ResourceLocation tex;

		public Entry(String id, Lazy<Ingredient> ingredient, ResourceLocation tex) {
			this.id = id;
			this.ingredient = ingredient;
			this.tex = tex;
		}

		public void tex(ResourceLocation tex) {
			this.tex = tex;
		}

	}

}
