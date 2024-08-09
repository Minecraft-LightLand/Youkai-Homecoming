package dev.xkmc.youkaishomecoming.content.client;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.Item;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public class YHRCParameters {

	public static final EnumProxy<RecipeBookCategories> MOKA_ICON =
			new EnumProxy<>(RecipeBookCategories.class, (Supplier<Item>) () -> YHBlocks.MOKA.asItem());
	public static final EnumProxy<RecipeBookCategories> KETTLE_ICON =
			new EnumProxy<>(RecipeBookCategories.class, (Supplier<Item>) () -> YHBlocks.KETTLE.asItem());

}
