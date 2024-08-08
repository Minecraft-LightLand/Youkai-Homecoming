package dev.xkmc.youkaishomecoming.content.client;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class YHRCParameters {

	public static final Supplier<Item> MOKA_ICON = () -> YHBlocks.MOKA.asItem();
	public static final Supplier<Item> KETTLE_ICON = () -> YHBlocks.KETTLE.asItem();

}
