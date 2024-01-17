package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.item.SuwakoHatItem;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import dev.xkmc.youkaihomecoming.init.food.YHFood;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class YHItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with");

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static final ItemEntry<SuwakoHatItem> SUWAKO_HAT;
	public static final ItemEntry<MobBucketItem> LAMPREY_BUCKET;


	static {
		SUWAKO_HAT = YoukaiHomecoming.REGISTRATE
				.item("suwako_hat", p -> new SuwakoHatItem(p.rarity(Rarity.EPIC)))
				.tag(Tags.Items.ARMORS_HELMETS)
				.register();

		YHCrops.register();

		YHFood.register();

		LAMPREY_BUCKET = YoukaiHomecoming.REGISTRATE
				.item("lamprey_bucket", p -> new MobBucketItem(
						YHEntities.LAMPREY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH,
						p.stacksTo(1).craftRemainder(Items.BUCKET)))
				.defaultLang()
				.register();
	}

	public static void register() {
	}

}
