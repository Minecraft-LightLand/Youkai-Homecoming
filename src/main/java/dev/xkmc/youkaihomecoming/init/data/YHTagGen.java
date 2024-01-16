package dev.xkmc.youkaihomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class YHTagGen {

	public static final TagKey<Item> FLESH_FOOD = item("flesh_food");

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(new ResourceLocation(YoukaiHomecoming.MODID, id));
	}

}
