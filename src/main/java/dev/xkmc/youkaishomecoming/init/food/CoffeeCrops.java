package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.content.block.CoffeaCropBlock;
import dev.xkmc.youkaishomecoming.content.block.WildCoffeaBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
import net.minecraft.world.level.block.ComposterBlock;
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
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum CoffeeCrops {
	COFFEA(PlantType.COFFEA, 6, 12, "green_coffee_bean", "coffee_berries");

	private final PlantType type;
	private final BlockEntry<? extends BushBlock> PLANT;
	private final BlockEntry<? extends Block> WILD;
	public final ItemEntry<ItemNameBlockItem> seed;
	public final ItemEntry<? extends Item> fruits;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	private final int rarity, density;

	CoffeeCrops(PlantType type, int rarity, int density, @Nullable String seedName, @Nullable String fruit) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		this.rarity = rarity;
		this.density = density;
		var sname = seedName == null ? name : seedName;
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, YoukaisHomecoming.loc(name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE, YoukaisHomecoming.loc(name));

		PLANT = type.plant(this);
		WILD = type.wild(this);

		var seedBuilder = CoffeeItems.seed(sname, p -> type.item(getPlant(), p));
		if (seedName != null || name.endsWith("bean")) seedBuilder.tag(ForgeTags.SEEDS);
		seed = seedBuilder.register();

		fruits = fruit == null ? seed :
				type.eatable() ? CoffeeItems.crop(fruit, p -> new Item(p.food(
						new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build()
				))) : CoffeeItems.crop(fruit, Item::new);

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
						pvd.modLoc("block/bags/crate_bottom"),
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

	public enum PlantType {
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

		;

		private final BiFunction<CoffeeCrops, String, BlockEntry<? extends BushBlock>> plant;
		private final Function<CoffeeCrops, BlockEntry<? extends Block>> wild;
		private final BiFunction<Block, Item.Properties, ItemNameBlockItem> item;

		PlantType(BiFunction<CoffeeCrops, String, BlockEntry<? extends BushBlock>> plant,
				  Function<CoffeeCrops, BlockEntry<? extends Block>> wild,
				  BiFunction<Block, Item.Properties, ItemNameBlockItem> item) {
			this.plant = plant;
			this.wild = wild;
			this.item = item;
		}

		public BlockEntry<? extends BushBlock> plant(CoffeeCrops crop) {
			return plant.apply(crop, crop.getName());
		}

		public BlockEntry<? extends Block> wild(CoffeeCrops crop) {
			return wild.apply(crop);
		}

		public ItemNameBlockItem item(Block plant, Item.Properties p) {
			return item.apply(plant, p);
		}

		public boolean eatable() {
			return false;
		}

	}

}
