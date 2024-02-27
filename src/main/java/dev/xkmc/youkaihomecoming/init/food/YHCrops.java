package dev.xkmc.youkaihomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaihomecoming.content.block.CoffeaCropBlock;
import dev.xkmc.youkaihomecoming.content.block.TeaCropBlock;
import dev.xkmc.youkaihomecoming.content.block.WildCoffeaBlock;
import dev.xkmc.youkaihomecoming.content.block.YHCropBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
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
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Locale;
import java.util.function.BiFunction;

public enum YHCrops {
	SOYBEAN(PlantType.CROP, null, "pods"),
	REDBEAN(PlantType.CROP, null, null),
	COFFEA(PlantType.COFFEA, "green_coffee_bean", "coffee_berries"),
	TEA(PlantType.TEA, "tea_seeds", "tea_leaves");

	private final BlockEntry<? extends CropBlock> PLANT;
	private final BlockEntry<? extends BushBlock> WILD;
	public final ItemEntry<ItemNameBlockItem> seed;
	public final ItemEntry<? extends Item> fruits;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	YHCrops(PlantType type, @Nullable String seedName, @Nullable String fruit) {
		String name = name().toLowerCase(Locale.ROOT);
		if (seedName == null) seedName = name;
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(YoukaiHomecoming.MODID, name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(YoukaiHomecoming.MODID, name));

		PLANT = type.plant(name, this);
		WILD = type.wild(name, this);

		seed = YHItems.crop(seedName, p -> new ItemNameBlockItem(getPlant(), p));

		fruits = fruit == null ? seed : YHItems.crop(fruit, Item::new);

	}

	public CropBlock getPlant() {
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
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		PlacementUtils.register(ctx, placementKey, ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	public BlockEntry<Block> createBag() {
		return createBag(getName());
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	public static BlockEntry<Block> createBag(String id) {
		return YoukaiHomecoming.REGISTRATE
				.block(id + "_bag", p -> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cube(
						ctx.getName(),
						pvd.modLoc("block/" + ctx.getName() + "_bottom"),
						pvd.modLoc("block/" + ctx.getName() + "_top"),
						pvd.modLoc("block/" + ctx.getName() + "_side"),
						pvd.modLoc("block/" + ctx.getName() + "_side"),
						pvd.modLoc("block/" + ctx.getName() + "_side_tied"),
						pvd.modLoc("block/" + ctx.getName() + "_side_tied")
				).texture("particle", pvd.modLoc("block/" + ctx.getName() + "_top"))))
				.simpleItem().register();
	}

	public static void register() {

	}

	public enum PlantType {
		CROP((name, crop) -> YoukaiHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> YHCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> YHCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				(name, crop) -> YoukaiHomecoming.REGISTRATE.block("wild_" + name, p -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)))
						.blockstate((ctx, pvd) -> YHCropBlock.buildWildModel(ctx, pvd, crop))
						.loot((ctx, pvd) -> YHCropBlock.buildWildLoot(ctx, pvd, crop))
						.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/wild_" + name))).build()
						.tag(ModTags.WILD_CROPS)
						.register()
		),
		COFFEA((name, crop) -> YoukaiHomecoming.REGISTRATE.block(name, p ->
						new CoffeaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> CoffeaCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> CoffeaCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				(name, crop) -> YoukaiHomecoming.REGISTRATE.block("wild_" + name, p -> new WildCoffeaBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)))
						.blockstate((ctx, pvd) -> WildCoffeaBlock.buildWildModel(ctx, pvd, crop))
						.loot((ctx, pvd) -> WildCoffeaBlock.buildWildLoot(ctx, pvd, crop))
						.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/wild_" + name + "_top"))).build()
						.tag(ModTags.WILD_CROPS)
						.register()
		),
		TEA((name, crop) -> YoukaiHomecoming.REGISTRATE.block(name, p ->
						new TeaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> TeaCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> TeaCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				(name, crop) -> YoukaiHomecoming.REGISTRATE.block("wild_" + name, p -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)))
						.blockstate((ctx, pvd) -> YHCropBlock.buildWildModel(ctx, pvd, crop))
						.loot((ctx, pvd) -> TeaCropBlock.buildWildLoot(ctx, pvd, crop))
						.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/wild_" + name))).build()
						.tag(ModTags.WILD_CROPS)
						.register()
		),
		;

		private final BiFunction<String, YHCrops, BlockEntry<? extends CropBlock>> plant;
		private final BiFunction<String, YHCrops, BlockEntry<? extends BushBlock>> wild;

		PlantType(BiFunction<String, YHCrops, BlockEntry<? extends CropBlock>> plant,
				  BiFunction<String, YHCrops, BlockEntry<? extends BushBlock>> wild) {
			this.plant = plant;
			this.wild = wild;
		}

		public BlockEntry<? extends CropBlock> plant(String name, YHCrops crop) {
			return plant.apply(name, crop);
		}

		public BlockEntry<? extends BushBlock> wild(String name, YHCrops crop) {
			return wild.apply(name, crop);
		}

	}

}
