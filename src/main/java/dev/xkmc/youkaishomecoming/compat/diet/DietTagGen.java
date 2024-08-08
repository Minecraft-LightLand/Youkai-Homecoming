package dev.xkmc.youkaishomecoming.compat.diet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Locale;

public enum DietTagGen {
	FRUITS, GRAINS, PROTEINS, SUGARS, VEGETABLES;

	public final TagKey<Item> tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("diet", name().toLowerCase(Locale.ROOT)));

}
