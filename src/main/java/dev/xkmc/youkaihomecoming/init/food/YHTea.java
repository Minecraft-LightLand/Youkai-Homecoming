package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public enum YHTea {
	BLACK, GREEN, OOLONG, WHITE;

	public final ItemEntry<Item> leaves;

	YHTea() {
		String name = name().toLowerCase(Locale.ROOT) + "_tea";
		leaves = YoukaiHomecoming.REGISTRATE.item(name + "_leaves", Item::new).register();
	}

	public BlockEntry<Block> createBags() {
		return YHCrops.createBag(name().toLowerCase(Locale.ROOT) + "_tea");
	}

	public static void register() {

	}


}
