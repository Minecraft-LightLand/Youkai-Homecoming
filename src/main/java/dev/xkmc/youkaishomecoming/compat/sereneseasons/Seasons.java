package dev.xkmc.youkaishomecoming.compat.sereneseasons;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sereneseasons.core.SereneSeasons;

import java.util.Locale;

public enum Seasons {
	SPRING, SUMMER, AUTUMN, WINTER;

	public final TagKey<Item> item;
	public final TagKey<Block> block;

	Seasons() {
		String name = name().toLowerCase(Locale.ROOT);
		item = ItemTags.create(new ResourceLocation(SereneSeasons.MOD_ID, name + "_crops"));
		block = BlockTags.create(new ResourceLocation(SereneSeasons.MOD_ID, name + "_crops"));
	}

}
