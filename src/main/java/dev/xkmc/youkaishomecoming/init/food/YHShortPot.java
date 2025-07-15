package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.block.food.PotFoodBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.mid.MidCookingPotBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Locale;

public enum YHShortPot implements ItemLike {
	COD_STEW(ModItems.BAKED_COD_STEW::get, 2),
	SHIRAYUKI(YHFood.SHIRAYUKI, 2),
	HAN_PALACE(YHFood.HAN_PALACE, 2),
	TOFU_CRAB_STEW(YHFood.TOFU_CRAB_STEW, 2);

	public final BlockEntry<PotFoodBlock> block;

	YHShortPot(ItemLike bowl, int serve) {
		String name = name().toLowerCase(Locale.ROOT);
		block = YoukaisHomecoming.REGISTRATE.block("pot_of_" + name, p -> serve == 2 ?
						new PotFoodBlock.Pot2(p, BowlBlock.POT_SHAPE, bowl) :
						new PotFoodBlock.Pot4(p, BowlBlock.STOCKPOT_SHAPE, bowl)
				).properties(p -> p.mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))
				.blockstate(MidCookingPotBlock::buildPotFood)
				.item().properties(p -> p.craftRemainder(serve == 2 ?
						YHBlocks.IRON_POT.get().asItem() :
						YHBlocks.STOCKPOT.get().asItem()
				)).build()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.lang(YHItems.toEnglishName("pot_of_" + name))
				.register();
	}

	@Override
	public Item asItem() {
		return block.asItem();
	}

	public static void register() {

	}

}
