package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.block.YHCropBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Locale;

public enum YHCrops {
	SOYBEAN("pods"), REDBEAN(null);

	private final BlockEntry<YHCropBlock> PLANT;
	private final BlockEntry<BushBlock> WILD;
	private final ItemEntry<ItemNameBlockItem> seed;
	private final ItemEntry<? extends Item> fruits;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	YHCrops(@Nullable String fruit) {
		String name = name().toLowerCase(Locale.ROOT);
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(YoukaiHomecoming.MODID, name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(YoukaiHomecoming.MODID, name));

		PLANT = YoukaiHomecoming.REGISTRATE.block(name, p -> new YHCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), this::getSeed))
				.blockstate(this::buildPlantModel)
				.loot(this::buildPlantLoot)
				.register();

		WILD = YoukaiHomecoming.REGISTRATE.block("wild_" + name, p -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)))
				.blockstate(this::buildWildModel)
				.loot(this::buildWildLoot)
				.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) -> pvd.generated(ctx)).build()
				.tag(ModTags.WILD_CROPS)
				.register();

		seed = YoukaiHomecoming.REGISTRATE
				.item(name, p -> new ItemNameBlockItem(getPlant(), p))
				.register();


		fruits = fruit == null ? seed : YoukaiHomecoming.REGISTRATE.item(fruit, Item::new)
				.register();

	}

	private void buildPlantModel(DataGenContext<Block, YHCropBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(CropBlock.AGE);
			String tex = getName() + "_stage" + age;
			return ConfiguredModel.builder().modelFile(pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
		});
	}

	private void buildWildModel(DataGenContext<Block, BushBlock> ctx, RegistrateBlockstateProvider pvd) {
		String tex = "wild_" + getName();
		pvd.simpleBlock(ctx.get(), pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout"));
	}

	private void buildPlantLoot(RegistrateBlockLootTables pvd, YHCropBlock block) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(getFruits())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)))
								.add(LootItem.lootTableItem(getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
	}

	private void buildWildLoot(RegistrateBlockLootTables pvd, BushBlock block) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
								.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
										MinMaxBounds.Ints.atLeast(1)))))
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(getFruits())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
				)));
	}

	public YHCropBlock getPlant() {
		return PLANT.get();
	}

	public Block getWildPlant() {
		return WILD.get();
	}

	public Item getSeed() {
		return seed.get();
	}

	public Item getFruits() {
		return fruits.get();
	}

	public void registerComposter() {
		ComposterBlock.COMPOSTABLES.put(getSeed(), 0.3f);
		if (getSeed() != getFruits())
			ComposterBlock.COMPOSTABLES.put(getFruits(), 0.5f);
		ComposterBlock.COMPOSTABLES.put(getWildPlant().asItem(), 0.65f);
	}

	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(24, 5, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getWildPlant())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.SAND)))));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		PlacementUtils.register(ctx, placementKey, ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	public static void register() {

	}

}
