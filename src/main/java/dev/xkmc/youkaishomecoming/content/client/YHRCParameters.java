package dev.xkmc.youkaishomecoming.content.client;

import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class YHRCParameters {

	public static final EnumProxy<RecipeBookCategories> MOKA_ICON =
			new EnumProxy<>(RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(YHBlocks.MOKA.asStack()));
	public static final EnumProxy<RecipeBookCategories> KETTLE_ICON =
			new EnumProxy<>(RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(YHBlocks.KETTLE.asStack()));

}
