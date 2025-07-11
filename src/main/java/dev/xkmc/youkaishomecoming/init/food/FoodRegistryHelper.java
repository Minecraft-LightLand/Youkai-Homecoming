package dev.xkmc.youkaishomecoming.init.food;

import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FoodRegistryHelper {

	static {
		InitializationMarker.expectAndAdvance(5);
	}

	private static boolean mochi = false;

	public static String getId(FoodType type, TagKey<Item>[] tags) {
		if (type.isFlesh()) return "food/flesh/";
		switch (type) {
			case BOTTLE:
				return "food/bottle/";
			case STICK:
				return "food/stick/";
			case BOWL:
				return "food/bowl/";
			case IRON_BOWL:
				return "bowl/iron/";
			case BAMBOO_BOWL:
				return "bowl/bamboo/";
		}
		if (tags.length > 0 && tags[0] == YHTagGen.DANGO) {
			mochi = true;
			return "food/mochi/";
		}
		if (!mochi) return "food/basic/";
		return "food/simple/";
	}

}
