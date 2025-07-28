package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.BottleBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.PushReaction;

public class BottledDrinkSet extends BottleTexture {

	public final BlockEntry<BottleBlock> bottle;
	public final IYHFluidHolder drink;

	public BottledDrinkSet(YHDrink drink) {
		this(drink, drink.getName() + "_bottle", drink.folder);

	}

	public BottledDrinkSet(IYHFluidHolder drink, String name, String folder) {
		this.drink = drink;
		bottle = YoukaisHomecoming.REGISTRATE.block(name, BottleBlock::new)
				.initialProperties(() -> Blocks.GLASS_PANE)
				.properties(p -> p.pushReaction(PushReaction.DESTROY))
				.blockstate((ctx, pvd) ->
						BottleBlock.buildModel(ctx, pvd, drink))
				.loot(BottleBlock::buildLoot)
				.item((block, prop) -> new BucketBottleItem(block, prop, drink))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/bottle/" + folder + "/" + ctx.getName())))
				.tag(YHTagGen.BOTTLED)
				.tab(YoukaisHomecoming.TAB.getKey())
				.build()
				.register();
	}

	@Override
	public IYHFluidHolder holder() {
		return drink;
	}

	@Override
	public String bottleModel() {
		return bottle.getId().getPath();
	}

}