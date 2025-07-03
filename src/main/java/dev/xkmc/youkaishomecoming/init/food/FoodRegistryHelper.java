package dev.xkmc.youkaishomecoming.init.food;

import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FoodRegistryHelper {

	private static boolean mochi = false;

	public static String getId(FoodType type, TagKey<Item>[] tags) {
		if (type.isFlesh()) return "food/flesh/";
		if (type == FoodType.BOTTLE) return "food/bottle/";
		if (type == FoodType.STICK || type == FoodType.MEAT_STICK) return "food/stick/";
		if (type == FoodType.BOWL || type == FoodType.BOWL_MEAT) return "food/bowl/";
		if (tags.length > 0 && tags[0] == YHTagGen.DANGO) {
			mochi = true;
			return "food/mochi/";
		}
		if (!mochi) return "food/basic/";
		return "food/simple/";
	}

}
