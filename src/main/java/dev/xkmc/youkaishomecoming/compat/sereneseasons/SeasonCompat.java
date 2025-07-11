package dev.xkmc.youkaishomecoming.compat.sereneseasons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.world.level.block.Block;

public class SeasonCompat {

	private static final Multimap<Seasons, YHCrops> CROP = new ImmutableMultimap.Builder<Seasons, YHCrops>()
			.putAll(Seasons.SPRING, YHCrops.TEA, YHCrops.CUCUMBER)
			//.putAll(Seasons.SUMMER, YHCrops.COFFEA, YHCrops.BLACK_GRAPE, YHCrops.WHITE_GRAPE, YHCrops.RED_GRAPE)
			.putAll(Seasons.AUTUMN, YHCrops.SOYBEAN)
			.putAll(Seasons.WINTER, YHCrops.REDBEAN)
			.build();

	public static void genItem(RegistrateItemTagsProvider pvd) {
		for (var s : Seasons.values()) {
			var item = pvd.addTag(s.item);
			for (var t : CROP.get(s)) {
				item.add(t.getSeed().asItem());
			}
		}
	}

	public static void genBlock(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		for (var s : Seasons.values()) {
			var block = pvd.addTag(s.block);
			for (var t : CROP.get(s)) {
				block.add(t.getPlant());
			}
		}
	}

}
