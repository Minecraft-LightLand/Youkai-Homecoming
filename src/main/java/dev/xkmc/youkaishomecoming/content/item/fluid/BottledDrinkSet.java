package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.BottleBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;

public class BottledDrinkSet {

	public BlockEntry<BottleBlock> bottle;

	public BottledDrinkSet(YHDrink drink) {
		String folder = drink.folder;
		bottle = YoukaisHomecoming.REGISTRATE.block(drink.getName() + "_bottle", BottleBlock::new)
				.blockstate((ctx, pvd) ->
						BottleBlock.buildModel(ctx, pvd, drink))
				.loot((pvd, block) -> BottleBlock.buildLoot(pvd, block, drink))
				.item((block, prop) -> new BucketBottleItem(block, prop, drink))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/bottle/" + folder + "/" + ctx.getName())))
				.tag(YHTagGen.BOTTLED)
				.build()
				.register();
	}

}