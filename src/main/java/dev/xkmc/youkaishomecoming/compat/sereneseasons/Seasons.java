package dev.xkmc.youkaishomecoming.compat.sereneseasons;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public enum Seasons {
	SPRING, SUMMER, AUTUMN, WINTER;

	public final TagKey<Item> item;
	public final TagKey<Block> block;

	Seasons() {
		String name = name().toLowerCase(Locale.ROOT);
		item = ItemTags.create(ResourceLocation.fromNamespaceAndPath("sereneseasons", name + "_crops"));
		block = BlockTags.create(ResourceLocation.fromNamespaceAndPath("sereneseasons", name + "_crops"));
	}

}
