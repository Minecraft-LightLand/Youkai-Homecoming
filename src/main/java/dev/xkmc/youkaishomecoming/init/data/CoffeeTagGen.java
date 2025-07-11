package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.compat.sereneseasons.SeasonCompat;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import sereneseasons.core.SereneSeasons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CoffeeTagGen {

	public static final TagKey<Block> FARMLAND_COFFEA = block("farmland_coffea");

	public static final TagKey<Item> ICE = forgeItem("ice_cubes");

	public static final List<Consumer<RegistrateItemTagsProvider>> OPTIONAL_TAGS = new ArrayList<>();

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(FARMLAND_COFFEA).add(Blocks.PODZOL, Blocks.MUD, Blocks.SOUL_SOIL);
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genBlock(pvd);
		}
	}

	@SuppressWarnings("unchecked")
	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(ICE).add(CoffeeItems.ICE_CUBE.get());
		if (ModList.get().isLoaded(SereneSeasons.MOD_ID)) {
			SeasonCompat.genItem(pvd);
		}
		for (var e : OPTIONAL_TAGS) {
			e.accept(pvd);
		}

	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<Item> forgeItem(String id) {
		return ItemTags.create(new ResourceLocation("forge", id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(YoukaisHomecoming.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, YoukaisHomecoming.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String mod, String id) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(mod, id));
	}

}
