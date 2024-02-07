package dev.xkmc.youkaihomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class WildCoffeaBlock extends DoublePlantBlock {

	public WildCoffeaBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public static void buildWildLoot(RegistrateBlockLootTables pvd, WildCoffeaBlock block, YHCrops crop) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
								.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
										MinMaxBounds.Ints.atLeast(1)))))
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(crop.getFruits())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
				).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER)))));
	}

	public static void buildWildModel(DataGenContext<Block, WildCoffeaBlock> ctx, RegistrateBlockstateProvider pvd, YHCrops crop) {
		String tex = crop.getName();
		pvd.getVariantBuilder(ctx.get()).forAllStatesExcept(state ->
				ConfiguredModel.builder().modelFile(pvd.models()
								.cross(tex + (state.getValue(HALF) == DoubleBlockHalf.UPPER ? "" : "_bottom"),
										pvd.modLoc("block/wild_" + tex +
												(state.getValue(HALF) == DoubleBlockHalf.UPPER ? "_top" : "_bottom")))
								.renderType("cutout"))
						.build());
	}


}
