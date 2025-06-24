package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.plant.*;
import dev.xkmc.youkaishomecoming.content.block.plant.grape.GrapeCropBlock;
import dev.xkmc.youkaishomecoming.content.block.plant.grape.GrapeJsonGen;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RootedClimbingCropBlock;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeClimbingSeedItem;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeCropJsonGen;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum YHCrops {
	SOYBEAN(PlantType.CROSS, 8, 12, null, "pods"),
	REDBEAN(PlantType.CROSS, 8, 12, null, null),
	COFFEA(PlantType.COFFEA, 6, 12, "green_coffee_bean", "coffee_berries"),
	TEA(PlantType.TEA, 6, 12, "tea_seeds", "tea_leaves"),
	UDUMBARA(PlantType.UDUMBARA, 6, 12, "udumbara_seeds", "udumbara_flower"),
	MANDRAKE(PlantType.MANDRAKE, 6, 12, "mandrake_root", "mandrake_flower"),
	CUCUMBER(PlantType.CUCUMBER, 8, 24, "cucumber_seeds", "cucumber"),
	RED_GRAPE(PlantType.GRAPE, 8, 12, null, null),
	BLACK_GRAPE(PlantType.GRAPE, 8, 12, null, null),
	WHITE_GRAPE(PlantType.GRAPE, 8, 12, null, null),
	;

	private final PlantType type;
	private final BlockEntry<? extends BushBlock> PLANT;
	private final BlockEntry<? extends BushBlock> WILD;
	public final ItemEntry<ItemNameBlockItem> seed;
	public final ItemEntry<? extends Item> fruits;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	private final int rarity, density;

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
		if (seedName != null || name.endsWith("bean")) seedBuilder.tag(ForgeTags.SEEDS);
		seed = seedBuilder.register();

		fruits = fruit == null ? seed : YHItems.crop(fruit, Item::new);

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


	public void registerComposter() {
		ComposterBlock.COMPOSTABLES.put(getSeed(), 0.3f);
		if (getSeed() != getFruits())
			ComposterBlock.COMPOSTABLES.put(getFruits(), 0.5f);
		ComposterBlock.COMPOSTABLES.put(getWildPlant().asItem(), 0.65f);
	}

	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(density, 5, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getWildPlant())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
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
				.block(id + "_crate", p -> new Block(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(
						ctx.getName(),
						pvd.modLoc("block/bags/" + ctx.getName() + "_side"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_bottom"),
						pvd.modLoc("block/bags/" + ctx.getName() + "_top")
				).texture("particle", pvd.modLoc("block/bags/" + ctx.getName() + "_top"))))
				.tag(Tags.Blocks.STORAGE_BLOCKS, BlockTags.MINEABLE_WITH_AXE)
				.item().tag(Tags.Items.STORAGE_BLOCKS).build()
				.register();
	}

	public static BlockEntry<Block> createBag(String id) {
		return YoukaisHomecoming.REGISTRATE
				.block(id + "_bag", p -> new Block(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL)))
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

	public BlockBuilder<BushBlock, L2Registrate> wildCrop() {
		return YoukaisHomecoming.REGISTRATE.block("wild_" + getName(), BushBlock::new)
				.initialProperties(() -> Blocks.DANDELION)
				.blockstate((ctx, pvd) -> YHCropBlock.buildWildModel(ctx, pvd, this))
				.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("block/plants/" + getTypeName() + "/wild_" + getName()))).build()
				.tag(ModTags.WILD_CROPS);
	}

	public BlockEntry<BushBlock> wildCropDropFruit() {
		return wildCrop()
				.loot((ctx, pvd) -> PlantJsonGen.wildDropFruit(ctx, pvd, this))
				.register();
	}

	public BlockEntry<BushBlock> wildCropDropSeed() {
		return wildCrop()
				.loot((ctx, pvd) -> PlantJsonGen.wildDropSeed(ctx, pvd, this))
				.register();
	}

	public enum PlantType {
		CROP((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCropModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropFruit, ItemNameBlockItem::new),
		CROSS((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCrossModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropFruit, ItemNameBlockItem::new),
		COFFEA((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new CoffeaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> CoffeaCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> CoffeaCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				(crop) -> YoukaisHomecoming.REGISTRATE.block("wild_" + crop.getName(), p ->
								new WildCoffeaBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION)))
						.blockstate((ctx, pvd) -> WildCoffeaBlock.buildWildModel(ctx, pvd, crop))
						.loot((ctx, pvd) -> WildCoffeaBlock.buildWildLoot(ctx, pvd, crop))
						.item().tag(ModTags.WILD_CROPS_ITEM).model((ctx, pvd) ->
								pvd.generated(ctx, pvd.modLoc("block/plants/" + crop.getTypeName() + "/wild_" + crop.getName() + "_top"))).build()
						.tag(ModTags.WILD_CROPS)
						.register(),
				ItemNameBlockItem::new),
		TEA((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new TeaCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noOcclusion().forceSolidOff()
								.randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY),
								crop::getSeed))
				.blockstate((ctx, pvd) -> TeaCropBlock.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> TeaCropBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropSeed, ItemNameBlockItem::new),
		UDUMBARA((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new UdumbaraBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).lightLevel(s -> 2), crop::getSeed, crop::getFruits))
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCrossModel(ctx, pvd, name))
				.loot((pvd, block) -> UdumbaraBlock.buildPlantLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropSeed, ItemNameBlockItem::new),
		MANDRAKE((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new YHCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed))
				.blockstate((ctx, pvd) -> PlantJsonGen.buildCrossModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildDoubleLoot(pvd, block, crop))
				.register(),
				YHCrops::wildCropDropSeed, ItemNameBlockItem::new),
		CUCUMBER((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new CucumberCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed, crop::getFruits))
				.blockstate((ctx, pvd) -> RopeCropJsonGen.buildRootedModel(ctx, pvd, name))
				.loot((pvd, block) -> PlantJsonGen.buildPlantLoot(pvd, block, crop))
				.tag(BlockTags.CLIMBABLE)
				.register(),
				YHCrops::wildCropDropFruit, RopeClimbingSeedItem::new),
		GRAPE((crop, name) -> YoukaisHomecoming.REGISTRATE.block(name, p ->
						new GrapeCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), crop::getSeed, crop::getFruits))
				.blockstate((ctx, pvd) -> GrapeJsonGen.buildPlantModel(ctx, pvd, name))
				.loot((pvd, block) -> GrapeJsonGen.buildPlantLoot(pvd, block, crop))
				.tag(BlockTags.CLIMBABLE)
				.register(),
				YHCrops::wildCropDropFruit, RopeClimbingSeedItem::new),

		;

		private final BiFunction<YHCrops, String, BlockEntry<? extends BushBlock>> plant;
		private final Function<YHCrops, BlockEntry<? extends BushBlock>> wild;
		private final BiFunction<Block, Item.Properties, ItemNameBlockItem> item;

		PlantType(BiFunction<YHCrops, String, BlockEntry<? extends BushBlock>> plant,
				  Function<YHCrops, BlockEntry<? extends BushBlock>> wild,
				  BiFunction<Block, Item.Properties, ItemNameBlockItem> item) {
			this.plant = plant;
			this.wild = wild;
			this.item = item;
		}

		public BlockEntry<? extends BushBlock> plant(YHCrops crop) {
			return plant.apply(crop, crop.getName());
		}

		public BlockEntry<? extends BushBlock> wild(YHCrops crop) {
			return wild.apply(crop);
		}

		public ItemNameBlockItem item(Block plant, Item.Properties p) {
			return item.apply(plant, p);
		}
	}

}
