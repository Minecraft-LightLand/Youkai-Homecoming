package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.block.YHCakeBlock;
import dev.xkmc.youkaihomecoming.content.block.YHCandleCakeBlock;
import dev.xkmc.youkaihomecoming.content.item.YHFoodItem;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CakeEntry {
	private final String base;

	public final BlockEntry<YHCakeBlock> block;
	public final BlockEntry<YHCandleCakeBlock> candle;
	public final BlockEntry<YHCandleCakeBlock>[] colored_candles;
	public final ItemEntry<YHFoodItem> item;

	@SuppressWarnings({"unchecked", "rawtype", "unsafe"})
	public CakeEntry(String base, MapColor color, FoodType type, int nut, float sat, EffectEntry... effects) {
		this.base = base;
		var props = BlockBehaviour.Properties.of().mapColor(color).forceSolidOn().strength(0.5F)
				.sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY);
		item = type.build("food/", base + "_cake_slice", nut, sat, new TagKey[0], List.of(effects));
		block = YoukaiHomecoming.REGISTRATE.block(base + "_cake", p -> new YHCakeBlock(item::get, props))
				.blockstate(this::genCakeModels).loot((pvd, block) -> pvd.dropOther(block, item.get()))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build().register();
		this.candle = YoukaiHomecoming.REGISTRATE.block(base + "_candle_cake",
						p -> new YHCandleCakeBlock(block, Blocks.CANDLE, props))
				.blockstate((ctx, pvd) -> genCandleModels(ctx, pvd, "candle"))
				.loot((pvd, block) -> pvd.dropOther(block, Items.CANDLE))
				.tag(BlockTags.CANDLE_CAKES)
				.register();
		colored_candles = new BlockEntry[DyeColor.values().length];
		for (DyeColor dye : DyeColor.values()) {
			String color_name = dye.getName();
			Block candle = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color_name + "_candle"));
			assert candle != null;
			this.colored_candles[dye.ordinal()] = YoukaiHomecoming.REGISTRATE.block(color_name + "_" + base + "_candle_cake",
							p -> new YHCandleCakeBlock(block, candle, props))
					.blockstate((ctx, pvd) -> genCandleModels(ctx, pvd, color_name + "_candle"))
					.loot((pvd, block) -> pvd.dropOther(block, candle.asItem()))
					.tag(BlockTags.CANDLE_CAKES)
					.register();
		}
	}

	private void genCandleModels(DataGenContext<Block, YHCandleCakeBlock> ctx, RegistrateBlockstateProvider pvd, String candle) {
		BlockModelBuilder nolit = genCandleCakeModel(ctx, pvd, candle, false);
		BlockModelBuilder lit = genCandleCakeModel(ctx, pvd, candle, true);
		pvd.getVariantBuilder(ctx.getEntry()).forAllStates(e ->
				ConfiguredModel.builder().modelFile(e.getValue(BlockStateProperties.LIT) ? lit : nolit).build());
	}

	private void genCakeModels(DataGenContext<Block, YHCakeBlock> ctx, RegistrateBlockstateProvider pvd) {
		BlockModelBuilder[] slice = new BlockModelBuilder[7];
		slice[0] = genCakeModel(pvd, "cake");
		for (int i = 1; i <= 6; i++) {
			slice[i] = genCakeModel(pvd, "cake_slice" + i);
		}
		pvd.getVariantBuilder(ctx.getEntry()).forAllStates(e ->
				ConfiguredModel.builder().modelFile(slice[e.getValue(BlockStateProperties.BITES)]).build());
	}

	private BlockModelBuilder genCakeModel(RegistrateBlockstateProvider pvd, String model) {
		return pvd.models().withExistingParent(base + "_" + model, new ResourceLocation("block/" + model))
				.texture("particle", pvd.modLoc("block/" + base + "_cake_side"))
				.texture("bottom", pvd.modLoc("block/" + base + "_cake_bottom"))
				.texture("top", pvd.modLoc("block/" + base + "_cake_top"))
				.texture("side", pvd.modLoc("block/" + base + "_cake_side"))
				.texture("inside", pvd.modLoc("block/" + base + "_cake_inner"));
	}

	private BlockModelBuilder genCandleCakeModel(DataGenContext<Block, YHCandleCakeBlock> ctx, RegistrateBlockstateProvider pvd, String candle, boolean lit) {
		String name = ctx.getName();
		if (lit) {
			name += "_lit";
			candle += "_lit";
		}
		return pvd.models().withExistingParent(name, new ResourceLocation("block/template_cake_with_candle"))
				.texture("particle", pvd.modLoc("block/" + base + "_cake_side"))
				.texture("bottom", pvd.modLoc("block/" + base + "_cake_bottom"))
				.texture("top", pvd.modLoc("block/" + base + "_cake_top"))
				.texture("side", pvd.modLoc("block/" + base + "_cake_side"))
				.texture("candle", pvd.mcLoc("block/" + candle));
	}

	public static void register() {
	}

}

