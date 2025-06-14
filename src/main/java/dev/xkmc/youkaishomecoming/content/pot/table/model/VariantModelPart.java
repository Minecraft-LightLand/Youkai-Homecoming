package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VariantModelPart {

	private final String name;
	private final ResourceLocation path;
	protected final int max;

	private final Map<String, Entry> ingredients = new LinkedHashMap<>();

	public VariantModelPart(String name, ResourceLocation path, int max) {
		this.name = name;
		this.path = path;
		this.max = max;
	}

	public ResourceLocation modelAt(String id, int index) {
		return path.withPath(e -> "cuisine/" + e + "/" + id + "/" + index);
	}

	public void addModels(List<ResourceLocation> list) {
		for (var ent : ingredients.entrySet()) {
			String id = ent.getKey();
			for (int i = 0; i < max; i++) {
				list.add(modelAt(id, i));
				if (ent.getValue().seared != null) {
					list.add(modelAt(id + "_seared", i));
				}
			}
		}
	}

	public void build(TableModelProvider pvd) {
		for (int i = 0; i < max; i++) {
			var model = path.withPrefix("table/").withSuffix("_" + i);
			for (var ent : ingredients.entrySet()) {
				String id = ent.getKey();
				pvd.create(modelAt(id, i), model).tex(name, ent.getValue().tex);
				if (ent.getValue().seared != null) {
					pvd.create(modelAt(id + "_seared", i), model).tex(name, ent.getValue().tex);
				}
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

	public @Nullable String find(ItemStack stack) {
		for (var ent : ingredients.entrySet()) {
			if (ent.getValue().ingredient.get().test(stack)) {
				return ent.getKey();
			}
		}
		return null;
	}

	public static class Entry {

		private final String id;
		private final Lazy<Ingredient> ingredient;
		private ResourceLocation tex, seared;

		public Entry(String id, Lazy<Ingredient> ingredient, ResourceLocation tex) {
			this.id = id;
			this.ingredient = ingredient;
			this.tex = tex;
		}

		public void tex(ResourceLocation tex) {
			this.tex = tex;
		}

		public void seareable() {
			this.seared = tex.withSuffix("_seared");
		}

	}

}
