package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;

public interface IYHDish {

	String getName();

	Saucer base();

	int height();

	BlockEntry<FoodSaucerBlock> block();

	default BlockEntry<FoodSaucerBlock> buildBlock(L2Registrate reg, String id, YHDish.Type type, boolean raw, boolean extra, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		return reg.block(id, p -> new FoodSaucerBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.LIGHT_GRAY_WOOL), this))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), build(pvd, id, extra)))
				.item((block, p) -> type.create(block, p.food(food(raw, nutrition, sat, effs)).craftRemainder(YHItems.SAUCER.asItem())))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/saucer/" + id)))
				.tag(tags).build()
				.register();
	}

	default FoodProperties food(boolean raw, int nutrition, float sat, List<EffectEntry> effs) {
		var builder = new FoodProperties.Builder()
				.nutrition(raw ? nutrition / 2 : nutrition)
				.saturationModifier(raw ? sat / 2 : sat);
		if (!raw) {
			for (var e : effs) {
				builder.effect(e::getEffect, e.chance());
			}
		}
		builder.usingConvertsTo(YHItems.SAUCER);
		return builder.build();
	}

	private BlockModelBuilder build(RegistrateBlockstateProvider pvd, String name, boolean extra) {
		var builder = pvd.models().getBuilder("block/" + name)
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/dish/" + name)));
		for (var e : base().tex) {
			builder.texture(e, "block/dish/" + e);
		}
		builder.texture("particle", "block/dish/" + base().tex[0]);
		builder.texture("base", "block/dish/" + name + "_base");
		if (extra) {
			builder.texture("detail", "block/dish/" + name + "_detail");
		}
		builder.renderType("cutout");
		return builder;
	}


}
