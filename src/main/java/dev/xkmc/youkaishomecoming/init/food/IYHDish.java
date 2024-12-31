package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public interface IYHDish {

	String getName();

	Saucer base();

	int height();

	BlockEntry<FoodSaucerBlock> block();

	default BlockEntry<FoodSaucerBlock> buildBlock(L2Registrate reg, FoodProperties food) {
		return reg.block(getName(), p -> new FoodSaucerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd)))
				.item((block, p) -> new FoodSaucerItem(block, p.food(food)))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + ctx.getName()))).build()
				.register();
	}

	default BlockModelBuilder build(RegistrateBlockstateProvider pvd) {
		String name = getName();
		var builder = pvd.models().getBuilder("block/" + name)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/saucer_" + name)));
		builder.texture("base", "block/" + name);
		builder.texture("particle", "block/" + name);
		if (base().extra) {
			builder.texture("extra", "block/" + name + "_extra");
		}
		for (var e : base().tex) {
			builder.texture(e, YoukaisHomecoming.loc("block/saucer_" + e));
		}
		builder.renderType("cutout");
		return builder;
	}


}
