package dev.xkmc.youkaishomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;

public class PlantJsonGen {


	public static void buildCrossModel(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(CropBlock.AGE);
			String tex = name + "/" + name + "_stage" + age;
			return ConfiguredModel.builder().modelFile(pvd.models()
					.getBuilder(tex)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/cross_crop")))
					.texture("cross", "block/plants/" + tex)
					.renderType("cutout")).build();
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, CropBlock block, CoffeeCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge()))
										.invert())
								.add(LootItem.lootTableItem(crop.getSeed())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge())))
								.add(LootItem.lootTableItem(crop.getFruits())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, block.getMaxAge())))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
	}

	public static void wildDropFruit(RegistrateBlockLootTables pvd, Block block, CoffeeCrops crop) {
		var silk = MatchTool.toolMatches(ItemPredicate.Builder.item()
				.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
						MinMaxBounds.Ints.atLeast(1)))).or(
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)));
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(silk)
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(crop.getFruits())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
				)));
	}

	public static void wildDropSeed(RegistrateBlockLootTables pvd, Block block, CoffeeCrops crop) {
		var silk = MatchTool.toolMatches(ItemPredicate.Builder.item()
				.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
						MinMaxBounds.Ints.atLeast(1)))).or(
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)));
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(silk)
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(crop.getSeed())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
				)));
	}

}
