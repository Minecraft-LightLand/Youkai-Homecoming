package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.Locale;

public enum YHTea {
	BLACK, GREEN, OOLONG, WHITE, DARK, YELLOW;

	public final ItemEntry<Item> leaves;

	YHTea() {
		String name = name().toLowerCase(Locale.ROOT) + "_tea";
		leaves = YHItems.crop(name + "_leaves", Item::new)
				.dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.3f)).register();
	}

	public BlockEntry<Block> createBags() {
		return YHCrops.createBag(name().toLowerCase(Locale.ROOT) + "_tea");
	}

	public static void register() {

	}


}
