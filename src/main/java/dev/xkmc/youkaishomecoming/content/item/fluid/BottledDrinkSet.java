package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.food.BottleBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;

public class BottledDrinkSet {

	private static List<BottledDrinkSet> LIST = new ArrayList<>();

	public final BlockEntry<BottleBlock> bottle;
	public final YHDrink drink;
	public final int index;

	public BottledDrinkSet(YHDrink drink) {
		index = LIST.size();
		this.drink = drink;
		LIST.add(this);
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

	public static void buildBottleModel(DataGenContext<Item, SlipBottleItem> ctx, RegistrateItemModelProvider pvd) {
		var model = pvd.generated(ctx, pvd.modLoc("item/sake_bottle"));

		model.override()
				.predicate(YoukaisHomecoming.loc("slip"), 1 / 32f)
				.model(pvd.getBuilder(ctx.getName() + "_overlay")
						.parent(new ModelFile.UncheckedModelFile("item/generated"))
						.texture("layer0", pvd.modLoc("item/sake_bottle"))
						.texture("layer1", pvd.modLoc("item/sake_bottle_overlay")))
				.end();

		int n = LIST.size();
		for (var e : LIST) {
			model.override()
					.predicate(YoukaisHomecoming.loc("bottle"), (e.index + 0.5f) / n)
					.model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + e.bottle.getId().getPath())))
					.end();
		}
	}

	public static float texture(ItemStack stack) {
		var fluid = SlipBottleItem.getFluid(stack);
		if (fluid.isEmpty()) return 0;
		if (fluid.getFluid() instanceof YHFluid liquid && liquid.type instanceof YHDrink drink && drink.set != null)
			return (drink.set.index + 1) * 1f / LIST.size();
		return 0;
	}

}