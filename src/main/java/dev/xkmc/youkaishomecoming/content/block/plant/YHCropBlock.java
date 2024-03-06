
package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import java.util.function.Supplier;

public class YHCropBlock extends CropBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	private final Supplier<Item> seed;

	public YHCropBlock(Properties prop, Supplier<Item> seed) {
		super(prop);
		this.seed = seed;
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return seed.get() == YHCrops.REDBEAN.getSeed() ? pState.is(YHTagGen.FARMLAND_REDBEAN) : super.mayPlaceOn(pState, pLevel, pPos);
	}

	protected ItemLike getBaseSeedId() {
		return seed.get();
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_AGE[this.getAge(pState)];
	}

	public static void buildCropModel(DataGenContext<Block, ? extends YHCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(CropBlock.AGE);
			String tex = name + "_stage" + age;
			return ConfiguredModel.builder().modelFile(pvd.models().crop(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
		});
	}

	public static void buildCrossModel(DataGenContext<Block, ? extends YHCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(CropBlock.AGE);
			String tex = name + "_stage" + age;
			return ConfiguredModel.builder().modelFile(pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, YHCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getFruits())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
	}

	public static void buildWildLoot(RegistrateBlockLootTables pvd, BushBlock block, YHCrops crop) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
								.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
										MinMaxBounds.Ints.atLeast(1)))))
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(crop.getFruits())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
				)));
	}

	public static void buildWildModel(DataGenContext<Block, ? extends BushBlock> ctx, RegistrateBlockstateProvider pvd, YHCrops crop) {
		String tex = "wild_" + crop.getName();
		pvd.simpleBlock(ctx.get(), pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout"));
	}

}