package dev.xkmc.youkaishomecoming.content.block.deco;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.Lazy;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public enum Baskets {
	APPLE("apple", Items.APPLE, "apple"),
	GOLDEN_APPLE("golden_apple", Items.GOLDEN_APPLE, "apple"),
	CABBAGE("cabbage", ModItems.CABBAGE::get, "cabbage"),
	CARROT("carrot", Items.CARROT, "carrot"),
	GOLDEN_CARROT("golden_carrot", Items.GOLDEN_CARROT, "carrot"),
	CUCUMBER("cucumber", YHCrops.CUCUMBER::getFruits, YHTagGen.CUCUMBER, "cucumber"),
	;

	final Lazy<Ingredient> test;
	final ItemLike item;
	final BlockEntry<DelegateBlock> block;
	private final String id, model;
	private final boolean extra;

	Baskets(String id, ItemLike item, String model) {
		this(id, item, () -> Ingredient.of(item), model);
	}

	Baskets(String id, ItemLike item, TagKey<Item> tag, String model) {
		this(id, item, () -> Ingredient.of(tag), model);
	}

	Baskets(String id, ItemLike item, Supplier<Ingredient> test, String model) {
		this.item = item;
		this.test = Lazy.of(test);
		this.id = id;
		this.model = model;
		this.extra = model.equals("cabbage");

		this.block = YoukaisHomecoming.REGISTRATE.block(id + "_basket", p -> DelegateBlock.newBaseBlock(p,
						BlockProxy.HORIZONTAL, new BasketBlock.Filled(this)))
				.blockstate((ctx, pvd) -> BasketBlock.buildStackModel(ctx, pvd, id, model, extra))
				.initialProperties(() -> Blocks.BAMBOO_SLAB)
				.simpleItem()
				.tag(BlockTags.MINEABLE_WITH_AXE)
				.loot((pvd, b) -> BasketBlock.loot(pvd, b, item))
				.register();

	}

	public static void register() {

	}

}
