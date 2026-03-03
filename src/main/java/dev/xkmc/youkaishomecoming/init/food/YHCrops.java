package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.plant.*;
import dev.xkmc.youkaishomecoming.content.block.plant.grape.GrapeCropBlock;
import dev.xkmc.youkaishomecoming.content.block.plant.grape.GrapeVineSet;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.CucumberCropBlock;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeClimbingSeedItem;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeCropJsonGen;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum YHCrops {
	SOYBEAN(PlantType.CROSS, 8, 12, null, "pods"),
	REDBEAN(PlantType.CROSS, 8, 12, null, null),
	TEA(PlantType.TEA, 6, 12, "tea_seeds", "tea_leaves"),
	UDUMBARA(PlantType.UDUMBARA, 6, 12, "udumbara_leaves", "udumbara_flower"),
	CUCUMBER(PlantType.CUCUMBER, 8, 24, "cucumber_seeds", "cucumber"),
	RED_GRAPE(PlantType.GRAPE, 8, 12, "red_grape_seeds", "red_grape"),
	BLACK_GRAPE(PlantType.GRAPE, 4, 48, "black_grape_seeds", "black_grape"),
	WHITE_GRAPE(PlantType.GRAPE, 8, 12, "white_grape_seeds", "white_grape"),
	;

	private final PlantType type;
	private final BlockEntry<? extends BushBlock> PLANT;
	private final BlockEntry<? extends Block> WILD;
	public final ItemEntry<ItemNameBlockItem> seed;
	public final ItemEntry<? extends Item> fruits;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	private final int rarity, density;
	public final GrapeVineSet set;

	YHCrops(PlantType type, int rarity, int density, @Nullable String seedName, @Nullable String fruit) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		this.rarity = rarity;
		this.density = density;
		var sname = seedName == null ? name : seedName;
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, YoukaisHomecoming.loc(name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE, YoukaisHomecoming.loc(name));

		PLANT = type.plant(this);
		WILD = type.wild(this);

		var seedBuilder = YHItems.seed(sname, p -> type.item(getPlant(), p));
		if (seedName != null || name.endsWith("bean")) seedBuilder.tag(Tags.Items.SEEDS);
		seed = seedBuilder.dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(fruit == null ? 0.5f : 0.3f)).register();

		fruits = fruit == null ? seed :
				(type.eatable() ? YHItems.crop(fruit, p -> new Item(p.food(
						new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build()
				))) : YHItems.crop(fruit, Item::new)).dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.5f)).register();

		if (type == PlantType.GRAPE) {
			set = new GrapeVineSet(this);
		} else set = null;

	}

	public Block getPlant() {
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

	public String getTypeName() {
		if (type == PlantType.GRAPE) {
			return getName().split("_")[1];
		}
		return getName();
	}

	public void registerConfigs(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		if (this == BLACK_GRAPE) {
			FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
					new RandomPatchConfiguration(density, 5, 5,
							PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
											BlockStateProvider.simple(getWildPlant())),
									BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
											BlockPredicate.matchesTag(Direction.UP.getNormal(), BlockTags.LEAVES)))));
			return;
		}
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(density, 5, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getWildPlant())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
	}

	public void registerPlacements(BootstrapContext<PlacedFeature> ctx) {
		PlacementUtils.register(ctx, placementKey, ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	public BlockEntry<Block> createBag() {
		return createBag(getName());
	}


	public BlockEntry<Block> createCrate() {
		return createCrate(getName());
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	public static BlockEntry<Block> createCrate(String id) {
		return YoukaisHomecoming.REGISTRATE
				.block(id + "_crate", Block::new)
				.initialProperties(() -> Blocks.DARK_OAK_PLANKS)
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(
						ctx.getName(),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side"),
						pvd.modLoc("block/bags/crate_bottom"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_top")
				).texture("particle", pvd.modLoc("block/bags/" + ctx.getName() + "_top"))))
				.tag(Tags.Blocks.STORAGE_BLOCKS, BlockTags.MINEABLE_WITH_AXE)
				.item().tag(Tags.Items.STORAGE_BLOCKS).build()
				.register();
	}

	public static BlockEntry<Block> createBag(String id) {
		return YoukaisHomecoming.REGISTRATE
				.block(id + "_bag", Block::new).initialProperties(() -> Blocks.BROWN_WOOL)
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cube(
						ctx.getName(),
						pvd.modLoc("block/bags/" + ctx.getName() + "_bottom"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_top"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side_tied"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side_tied")
				).texture("particle", pvd.modLoc("block/bags/" + ctx.getName() + "_top"))))
				.tag(Tags.Blocks.STORAGE_BLOCKS)
				.item().tag(Tags.Items.STORAGE_BLOCKS).build()
				.register();
	}

	public static void register() {

	}

	public BlockBuilder<BasicBushBlock, L2Registrate> wildCrop() {
		return YoukaisHomecoming.REGISTRATE.block("wild_" + getName(), BasicBushBlock::new)
				.initialProperties(() -> Blocks.DANDELION)
				.blockstate((ctx, pvd) -> YHCropBlock.buildWildModel(ctx, pvd, this))
				.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("block/plants/" + getTypeName() + "/wild_" + getName())))
				.dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.8f)).build()
				.tag(ModTags.WILD_CROPS);
	}

	public BlockEntry<BasicBushBlock> wildCropDropFruit() {
		return wildCrop()
				.loot((ctx, pvd) -> PlantJsonGen.wildDropFruit(ctx, pvd, this))
				.register();
	}

	public BlockEntry<BasicBushBlock> wildCropDropSeed() {
		return wildCrop()
				.loot((ctx, pvd) -> PlantJsonGen.wildDropSeed(ctx, pvd, this))
				.register();
	}

	public enum PlantType {
		CROP((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(p, crop::getSeed))
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCropModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropFruit, ItemNameBlockItem::new),
		CROSS((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(p, crop::getSeed))
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCrossModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropFruit, ItemNameBlockItem::new),
		TEA((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new TeaCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noOcclusion().forceSolidOff()
								.randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY),
								crop::getSeed))
				.blockstate((ctx, pvd) -> TeaCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> TeaCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropSeed, ItemNameBlockItem::new),
		UDUMBARA((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new UdumbaraBlock(p.lightLevel(s -> 2), crop::getSeed, crop::getFruits))
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCrossModel(ctx, pvd, name))
				.loot((pvd, block) -> UdumbaraBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropSeed, ItemNameBlockItem::new),
		CUCUMBER((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new CucumberCropBlock(p, crop::getSeed, crop::getFruits))
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate((ctx, pvd) -> RopeCropJsonGen.buildRootedModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.tag(BlockTags.CLIMBABLE)
				.register(),
				YHCrops::wildCropDropFruit, RopeClimbingSeedItem::new),
		GRAPE((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new GrapeCropBlock(p, crop))
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate((ctx, pvd) -> GrapeVineSet.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> GrapeVineSet.buildPlantLoot(pvd, block, crop))
				.tag(BlockTags.CLIMBABLE)
				.register(),
				GrapeVineSet::wildBush, RopeClimbingSeedItem::new),

		;

		private final BiFunction<YHCrops, String, BlockEntry<? extends BushBlock>> plant;
		private final Function<YHCrops, BlockEntry<? extends Block>> wild;
		private final BiFunction<Block, Item.Properties, ItemNameBlockItem> item;

		PlantType(BiFunction<YHCrops, String, BlockEntry<? extends BushBlock>> plant,
				  Function<YHCrops, BlockEntry<? extends Block>> wild,
				  BiFunction<Block, Item.Properties, ItemNameBlockItem> item) {
			this.plant = plant;
			this.wild = wild;
			this.item = item;
		}

		public BlockEntry<? extends BushBlock> plant(YHCrops crop) {
			return plant.apply(crop, crop.getName());
		}

		public BlockEntry<? extends Block> wild(YHCrops crop) {
			return wild.apply(crop);
		}

		public ItemNameBlockItem item(Block plant, Item.Properties p) {
			return item.apply(plant, p);
		}

		public boolean eatable() {
			return this == CUCUMBER || this == GRAPE;
		}

	}

}
