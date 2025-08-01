package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.*;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationBoxBlock;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationBoxBlockEntity;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationShape;
import dev.xkmc.youkaishomecoming.content.block.donation.DoubleBlockHorizontal;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.block.food.IronBowlBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.MokaKitBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.MoonLanternBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodChairBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodTableBlock;
import dev.xkmc.youkaishomecoming.content.block.variants.*;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaishomecoming.content.pot.basin.*;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingInv;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotCookingRecipe;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.UnorderedCookingRecipe;
import dev.xkmc.youkaishomecoming.content.pot.cooking.large.BigSpoonItem;
import dev.xkmc.youkaishomecoming.content.pot.cooking.large.LargeCookingPotBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.large.LargeCookingPotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.cooking.large.LargeCookingPotRenderer;
import dev.xkmc.youkaishomecoming.content.pot.cooking.mid.MidCookingPotBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.mid.MidCookingPotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.cooking.mid.MidCookingPotRenderer;
import dev.xkmc.youkaishomecoming.content.pot.cooking.small.SmallCookingPotBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.small.SmallCookingPotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.cooking.small.SmallCookingPotRenderer;
import dev.xkmc.youkaishomecoming.content.pot.cooking.soup.SimpleSoupBaseRecipe;
import dev.xkmc.youkaishomecoming.content.pot.cooking.soup.SoupBaseRecipe;
import dev.xkmc.youkaishomecoming.content.pot.ferment.*;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.content.pot.moka.*;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRenderer;
import dev.xkmc.youkaishomecoming.content.pot.steamer.*;
import dev.xkmc.youkaishomecoming.content.pot.storage.bottle.SauceRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.storage.bottle.SauceRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.storage.bottle.SauceRackRenderer;
import dev.xkmc.youkaishomecoming.content.pot.storage.ingredient.IngredientRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.storage.ingredient.IngredientRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.storage.ingredient.IngredientRackRenderer;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.ShelfRenderer;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlock;
import dev.xkmc.youkaishomecoming.content.pot.storage.shelf.WineShelfBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.table.board.CuisineBoardBlock;
import dev.xkmc.youkaishomecoming.content.pot.table.board.CuisineBoardBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.table.board.CuisineBoardRenderer;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.*;
import dev.xkmc.youkaishomecoming.content.pot.tank.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHRecipeGen;
import dev.xkmc.youkaishomecoming.init.food.InitializationMarker;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Locale;
import java.util.function.Supplier;

public class YHBlocks {

	public enum WoodType {
		OAK(Blocks.OAK_PLANKS, Blocks.OAK_FENCE, Items.STRIPPED_OAK_WOOD, Items.OAK_SLAB),
		BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_SLAB),
		SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_SLAB),
		JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_SLAB),
		DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_SLAB),
		ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_SLAB),
		CRIMSON(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_SLAB),
		WARPED(Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, Items.STRIPPED_CRIMSON_HYPHAE, Items.WARPED_SLAB),
		MANGROVE(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_SLAB),
		CHERRY(Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE, Items.STRIPPED_CHERRY_WOOD, Items.CHERRY_SLAB),
		BAMBOO(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE, Items.STRIPPED_BAMBOO_BLOCK, Items.BAMBOO_SLAB),
		;

		private final Block plankProp, fenceProp;
		public final ItemLike plank, strippedWood, slab;
		public BlockEntry<MultiFenceBlock> fence;
		public BlockEntry<WoodTableBlock> table;
		public BlockEntry<WoodChairBlock> seat;

		WoodType(Block plankProp, Block fenceProp, ItemLike strippedWood, ItemLike slab) {
			this.plankProp = plankProp;
			this.fenceProp = fenceProp;
			this.plank = plankProp;
			this.strippedWood = strippedWood;
			this.slab = slab;
		}

	}

	public static final BlockEntry<MokaMakerBlock> MOKA;
	public static final BlockEntityEntry<MokaMakerBlockEntity> MOKA_BE;
	public static final RegistryEntry<RecipeType<MokaRecipe>> MOKA_RT;
	public static final RegistryEntry<RecipeSerializer<MokaRecipe>> MOKA_RS;
	public static final MenuEntry<MokaMenu> MOKA_MT;

	public static final BlockEntry<DelegateBlock> KETTLE;
	public static final BlockEntityEntry<KettleBlockEntity> KETTLE_BE;
	public static final RegistryEntry<RecipeType<KettleRecipe>> KETTLE_RT;
	public static final RegistryEntry<BaseRecipe.RecType<KettleRecipe, KettleRecipe, SimpleContainer>> KETTLE_RS;

	public static final BlockEntry<DryingRackBlock> RACK;
	public static final BlockEntityEntry<DryingRackBlockEntity> RACK_BE;
	public static final RegistryEntry<RecipeType<DryingRackRecipe>> RACK_RT;
	public static final RegistryEntry<RecipeSerializer<DryingRackRecipe>> RACK_RS;

	public static final BlockEntry<DelegateBlock> FERMENT;
	public static final BlockEntityEntry<FermentationTankBlockEntity> FERMENT_BE;
	public static final RegistryEntry<RecipeType<FermentationRecipe<?>>> FERMENT_RT;
	public static final RegistryEntry<BaseRecipe.RecType<SimpleFermentationRecipe, FermentationRecipe<?>, FermentationDummyContainer>> FERMENT_RS;

	public static final BlockEntry<DelegateBlock> BASIN;
	public static final BlockEntityEntry<BasinBlockEntity> BASIN_BE;
	public static final RegistryEntry<RecipeType<BasinRecipe<?>>> BASIN_RT;
	public static final RegistryEntry<BaseRecipe.RecType<SimpleBasinRecipe, BasinRecipe<?>, BasinInput>> BASIN_RS;

	public static final BlockEntry<DelegateBlock> STEAMER_POT;
	public static final BlockEntry<DelegateBlock> STEAMER_RACK;
	public static final BlockEntry<DelegateBlock> STEAMER_LID;
	public static final BlockEntityEntry<SteamerBlockEntity> STEAMER_BE;
	public static final RegistryEntry<RecipeType<SteamingRecipe>> STEAM_RT;
	public static final RegistryEntry<RecipeSerializer<SteamingRecipe>> STEAM_RS;

	public static final BlockEntry<CopperTankBlock> COPPER_TANK;
	public static final BlockEntityEntry<CopperTankBlockEntity> TANK_BE;
	public static final BlockEntry<DelegateBlock> COPPER_FAUCET;
	public static final BlockEntityEntry<CopperFaucetBlockEntity> FAUCET_BE;

	public static final BlockEntry<DelegateBlock> CUISINE_BOARD;
	public static final BlockEntityEntry<CuisineBoardBlockEntity> CUISINE_BOARD_BE;
	public static final RegistryEntry<RecipeType<CuisineRecipe<?>>> CUISINE_RT;
	public static final RegistryEntry<BaseRecipe.RecType<OrderedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_ORDER;
	public static final RegistryEntry<BaseRecipe.RecType<UnorderedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_UNORDER;
	public static final RegistryEntry<BaseRecipe.RecType<MixedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_MIXED;
	public static final RegistryEntry<BaseRecipe.RecType<FixedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_FIXED;

	public static final BlockEntry<IronBowlBlock> IRON_BOWL, IRON_POT, STOCKPOT;
	public static final BlockEntry<BowlBlock> WOOD_BOWL, BAMBOO_BOWL;
	public static final BlockEntry<DelegateBlock> SMALL_POT, SHORT_POT, LARGE_POT;
	public static final ItemEntry<BigSpoonItem> BIG_SPOON;
	public static final BlockEntityEntry<SmallCookingPotBlockEntity> SMALL_POT_BE;
	public static final BlockEntityEntry<MidCookingPotBlockEntity> MID_POT_BE;
	public static final BlockEntityEntry<LargeCookingPotBlockEntity> LARGE_POT_BE;
	public static final RegistryEntry<RecipeType<PotCookingRecipe<?>>> COOKING_RT;
	public static final RegistryEntry<BaseRecipe.RecType<UnorderedCookingRecipe, PotCookingRecipe<?>, CookingInv>> COOKING_UNORDER;
	public static final RegistryEntry<RecipeType<SoupBaseRecipe<?>>> SOUP_RT;
	public static final RegistryEntry<BaseRecipe.RecType<SimpleSoupBaseRecipe, SoupBaseRecipe<?>, CookingInv>> IMMEDIATE_SOUP;


	public static final BlockEntry<DelegateBlock> OAK_INGREDIENT_RACK;
	public static final BlockEntityEntry<IngredientRackBlockEntity> INGREDIENT_RACK_BE;

	public static final BlockEntry<DelegateBlock> OAK_SAUCE_RACK;
	public static final BlockEntityEntry<SauceRackBlockEntity> SAUCE_RACK_BE;

	public static final BlockEntry<DelegateBlock> SPRUCE_WINE_SHELF;
	public static final BlockEntityEntry<WineShelfBlockEntity> WINE_SHELF_BE;

	public static final BlockEntry<DelegateBlock> DONATION_BOX;
	public static final BlockEntityEntry<DonationBoxBlockEntity> DONATION_BOX_BE;

	public static final BlockEntry<MokaKitBlock> MOKA_KIT;
	public static final BlockEntry<MoonLanternBlock> MOON_LANTERN;

	public static FullSikkuiSet SIKKUI, CROSS_SIKKUI;
	public static SikkuiSet FRAMED_SIKKUI, GRID_SIKKUI;
	public static final BlockEntry<Block> FINE_GRID_SIKKUI;
	public static final BlockEntry<ThinTrapdoorBlock> FINE_GRID_SIKKUI_TD;
	public static final BlockEntry<ThinDoorBlock> FINE_GRID_SHOJI;

	public static final WoodSet HAY, STRAW;

	static {
		InitializationMarker.expectAndAdvance(1);
		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.TAB.getKey());

		// moka kettle, rack
		{
			MOKA = YoukaisHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			MOKA_BE = YoukaisHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
			MOKA_RT = YoukaisHomecoming.REGISTRATE.recipe("moka_pot");
			MOKA_RS = reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
			MOKA_MT = YoukaisHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

			KETTLE = YoukaisHomecoming.REGISTRATE.block("kettle", p -> DelegateBlock.newBaseBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL),
							BlockProxy.HORIZONTAL, new KettleBlock(), KettleBlock.TE))
					.blockstate(KettleBlock::buildModel).item().properties(e -> e.stacksTo(1)).build()
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = YoukaisHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = YoukaisHomecoming.REGISTRATE.recipe("kettle");
			KETTLE_RS = reg("kettle", () -> new BaseRecipe.RecType<>(KettleRecipe.class, KETTLE_RT));

			RACK = YoukaisHomecoming.REGISTRATE.block("drying_rack", p -> new DryingRackBlock(
							BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).noOcclusion()))
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = YoukaisHomecoming.REGISTRATE.recipe("drying_rack");
			RACK_RS = reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));
		}

		//ferment, basin
		{
			FERMENT = YoukaisHomecoming.REGISTRATE.block("fermentation_tank", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS),
									new FermentationTankBlock(), FermentationTankBlock.TE))
					.blockstate(FermentationTankBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_PICKAXE)
					.register();
			FERMENT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("fermentation_tank", FermentationTankBlockEntity::new)
					.validBlock(FERMENT).renderer(() -> FermentationTankRenderer::new).register();
			FERMENT_RT = YoukaisHomecoming.REGISTRATE.recipe("fermentation");
			FERMENT_RS = reg("simple_fermentation", () -> new BaseRecipe.RecType<>(SimpleFermentationRecipe.class, FERMENT_RT));

			BASIN = YoukaisHomecoming.REGISTRATE.block("wood_basin", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS),
									new BasinBlock(), BasinBlock.TE))
					.blockstate(BasinBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			BASIN_BE = YoukaisHomecoming.REGISTRATE.blockEntity("basin", BasinBlockEntity::new)
					.validBlock(BASIN).renderer(() -> BasinRenderer::new).register();

			BASIN_RT = YoukaisHomecoming.REGISTRATE.recipe("basin");
			BASIN_RS = reg("simple_basin", () -> new BaseRecipe.RecType<>(SimpleBasinRecipe.class, BASIN_RT));

		}

		// steamer
		{
			STEAMER_POT = YoukaisHomecoming.REGISTRATE.block("steamer_pot", p -> SteamerStates.createPotBlock())
					.blockstate(SteamerBlockJsons::genPotModel)
					.simpleItem()
					.loot(SteamerBlockJsons::genPotLoot)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.register();

			STEAMER_RACK = YoukaisHomecoming.REGISTRATE.block("steamer_rack", p -> SteamerStates.createRackBlock())
					.blockstate(SteamerBlockJsons::genRackModel)
					.simpleItem()
					.loot(SteamerBlockJsons::genRackLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			STEAMER_LID = YoukaisHomecoming.REGISTRATE.block("steamer_lid", p -> SteamerStates.createLidBlock())
					.blockstate(SteamerBlockJsons::genLidModel)
					.simpleItem()
					.defaultLoot()
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			STEAMER_BE = YoukaisHomecoming.REGISTRATE.blockEntity("steamer", SteamerBlockEntity::new)
					.renderer(() -> SteamerBlockRenderer::new)
					.validBlocks(STEAMER_POT, STEAMER_RACK)
					.register();

			STEAM_RT = YoukaisHomecoming.REGISTRATE.recipe("steaming");
			STEAM_RS = reg("steaming", () -> new SimpleCookingSerializer<>(SteamingRecipe::new, 100));

		}

		// pot
		{
			IRON_BOWL = BowlBlock.ironBowl("small_iron_pot")
					.item().build()
					.register();

			IRON_POT = YoukaisHomecoming.REGISTRATE.block("short_iron_pot", p -> new IronBowlBlock(p, BowlBlock.POT_SHAPE))
					.initialProperties(() -> Blocks.CAULDRON)
					.properties(p -> p.sound(SoundType.METAL))
					.blockstate(MidCookingPotBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.item().build()
					.register();

			STOCKPOT = YoukaisHomecoming.REGISTRATE.block("stockpot", p -> new IronBowlBlock(p, BowlBlock.STOCKPOT_SHAPE))
					.initialProperties(() -> Blocks.CAULDRON)
					.properties(p -> p.sound(SoundType.METAL))
					.blockstate(LargeCookingPotBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.item().build()
					.loot(LargeCookingPotBlock::createLoot)
					.register();

			BIG_SPOON = YoukaisHomecoming.REGISTRATE.item("big_spoon", BigSpoonItem::new)
					.properties(p -> p.stacksTo(1))
					.model(BigSpoonItem::buildModel)
					.register();

			WOOD_BOWL = BowlBlock.woodBowlFood("wood_bowl")
					.loot((pvd, block) -> pvd.dropOther(block, Items.BOWL))
					.register();

			BAMBOO_BOWL = BowlBlock.bambooBowl("bamboo_bowl")
					.loot((pvd, block) -> pvd.dropOther(block, Items.BAMBOO))
					.register();

			SMALL_POT = YoukaisHomecoming.REGISTRATE.block("cooking_small_iron_pot", SmallCookingPotBlock::create)
					.initialProperties(() -> Blocks.CAULDRON)
					.properties(p -> p.sound(SoundType.METAL))
					.blockstate(SmallCookingPotBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.loot((pvd, b) -> pvd.dropOther(b, IRON_BOWL.get()))
					.register();

			SMALL_POT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("cooking_small_iron_pot", SmallCookingPotBlockEntity::new)
					.validBlock(SMALL_POT)
					.renderer(() -> SmallCookingPotRenderer::new)
					.register();

			SHORT_POT = YoukaisHomecoming.REGISTRATE.block("cooking_short_iron_pot", MidCookingPotBlock::create)
					.initialProperties(() -> Blocks.CAULDRON)
					.properties(p -> p.sound(SoundType.METAL))
					.blockstate(MidCookingPotBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.loot((pvd, b) -> pvd.dropOther(b, IRON_POT.get()))
					.register();

			MID_POT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("cooking_short_iron_pot", MidCookingPotBlockEntity::new)
					.validBlock(SHORT_POT)
					.renderer(() -> MidCookingPotRenderer::new)
					.register();

			LARGE_POT = YoukaisHomecoming.REGISTRATE.block("cooking_stockpot", LargeCookingPotBlock::create)
					.initialProperties(() -> Blocks.CAULDRON)
					.properties(p -> p.sound(SoundType.METAL))
					.blockstate(LargeCookingPotBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.loot(LargeCookingPotBlock::createLoot)
					.register();

			LARGE_POT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("cooking_stockpot", LargeCookingPotBlockEntity::new)
					.validBlock(LARGE_POT)
					.renderer(() -> LargeCookingPotRenderer::new)
					.register();

			COOKING_RT = YoukaisHomecoming.REGISTRATE.recipe("pot_cooking");
			COOKING_UNORDER = reg("unordered_cooking", () -> new BaseRecipe.RecType<>(UnorderedCookingRecipe.class, COOKING_RT));
			SOUP_RT = YoukaisHomecoming.REGISTRATE.recipe("soup_base");
			IMMEDIATE_SOUP = reg("immediate_soup", () -> new BaseRecipe.RecType<>(SimpleSoupBaseRecipe.class, SOUP_RT));

		}

		// copper tank and faucet
		{
			COPPER_TANK = YoukaisHomecoming.REGISTRATE.block("copper_tank", p -> new CopperTankBlock(
							BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.COPPER)
									.strength(2f).requiresCorrectToolForDrops(),
							CopperTankBlock.INS, CopperTankBlock.TE))
					.blockstate(CopperTankBlock::buildStates)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
					.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()
							.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block)
									.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
											.setProperties(StatePropertiesPredicate.Builder.properties()
													.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)))))))
					.register();

			TANK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("copper_tank", CopperTankBlockEntity::new)
					.validBlock(COPPER_TANK)
					.register();

			COPPER_FAUCET = YoukaisHomecoming.REGISTRATE.block("copper_faucet", p -> DelegateBlock.newBaseBlock(
							BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.COPPER)
									.strength(2f).requiresCorrectToolForDrops().noOcclusion(),
							BlockProxy.HORIZONTAL, CopperFaucetBlock.INS, CopperFaucetBlock.TE))
					.blockstate(CopperFaucetBlock::buildStates)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.simpleItem()
					.defaultLoot()
					.register();

			FAUCET_BE = YoukaisHomecoming.REGISTRATE.blockEntity("copper_faucet", CopperFaucetBlockEntity::new)
					.validBlock(COPPER_FAUCET)
					.renderer(() -> CopperFaucetRenderer::new)
					.register();

		}

		// cuisine
		{
			CUISINE_BOARD = YoukaisHomecoming.REGISTRATE.block("cuisine_board", CuisineBoardBlock::create)
					.blockstate(CuisineBoardBlock::buildState)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.defaultLoot().simpleItem()
					.register();
			CUISINE_BOARD_BE = YoukaisHomecoming.REGISTRATE.blockEntity("cuisine_board", CuisineBoardBlockEntity::new)
					.validBlock(CUISINE_BOARD)
					.renderer(() -> CuisineBoardRenderer::new)
					.register();
			CUISINE_RT = YoukaisHomecoming.REGISTRATE.recipe("cuisine");
			CUISINE_ORDER = reg("cuisine_ordered", () -> new BaseRecipe.RecType<>(OrderedCuisineRecipe.class, CUISINE_RT));
			CUISINE_UNORDER = reg("cuisine_unordered", () -> new BaseRecipe.RecType<>(UnorderedCuisineRecipe.class, CUISINE_RT));
			CUISINE_MIXED = reg("cuisine_mixed", () -> new BaseRecipe.RecType<>(MixedCuisineRecipe.class, CUISINE_RT));
			CUISINE_FIXED = reg("cuisine_fixed", () -> new BaseRecipe.RecType<>(FixedCuisineRecipe.class, CUISINE_RT));
		}

		//storage blocks
		{

			OAK_INGREDIENT_RACK = YoukaisHomecoming.REGISTRATE.block("oak_ingredient_rack", p ->
							DelegateBlock.newBaseBlock(p, BlockProxy.HORIZONTAL, new IngredientRackBlock(), IngredientRackBlock.BE))
					.blockstate(IngredientRackBlock::buildModels)
					.properties(p -> p.noOcclusion())
					.simpleItem()
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			INGREDIENT_RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("ingredient_rack", IngredientRackBlockEntity::new)
					.renderer(() -> IngredientRackRenderer::new)
					.validBlock(OAK_INGREDIENT_RACK)
					.register();

			OAK_SAUCE_RACK = YoukaisHomecoming.REGISTRATE.block("oak_sauce_rack", p ->
							DelegateBlock.newBaseBlock(p, BlockProxy.HORIZONTAL, new SauceRackBlock(), SauceRackBlock.BE))
					.blockstate(SauceRackBlock::buildModels)
					.properties(p -> p.noOcclusion())
					.simpleItem()
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			SAUCE_RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("sauce_rack", SauceRackBlockEntity::new)
					.renderer(() -> SauceRackRenderer::new)
					.validBlock(OAK_SAUCE_RACK)
					.register();

			SPRUCE_WINE_SHELF = YoukaisHomecoming.REGISTRATE.block("spruce_wine_shelf", p ->
							DelegateBlock.newBaseBlock(p, BlockProxy.HORIZONTAL, new WineShelfBlock(), WineShelfBlock.BE))
					.blockstate(WineShelfBlock::buildModels)
					.properties(p -> p.noOcclusion())
					.simpleItem()
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			WINE_SHELF_BE = YoukaisHomecoming.REGISTRATE.blockEntity("wine_shelf", WineShelfBlockEntity::new)
					.renderer(() -> ShelfRenderer::new)
					.validBlock(SPRUCE_WINE_SHELF)
					.register();

		}

		InitializationMarker.expectAndAdvance(2);
		YHItems.register();

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.DECO.getKey());


		{
			DONATION_BOX = YoukaisHomecoming.REGISTRATE.block("donation_box", p -> DelegateBlock.newBaseBlock(
							BlockBehaviour.Properties.of().noLootTable().strength(2.0F).sound(SoundType.WOOD)
									.mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS),
							BlockProxy.HORIZONTAL, new DoubleBlockHorizontal(),
							new DonationShape(), DonationBoxBlock.TE
					)).blockstate(DonationBoxBlock::buildStates)
					.simpleItem()
					.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()))
					.register();

			DONATION_BOX_BE = YoukaisHomecoming.REGISTRATE.blockEntity("donation_box", DonationBoxBlockEntity::new)
					.validBlock(DONATION_BOX)
					.register();

			MOKA_KIT = YoukaisHomecoming.REGISTRATE.block("moka_kit", p -> new MokaKitBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
							.texture("maker", pvd.modLoc("block/deco/moka_pot"))
							.texture("cup", pvd.modLoc("block/deco/moka_cup"))
							.texture("foamer", pvd.modLoc("block/deco/moka_foamer"))
							.renderType("cutout")))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

			MOON_LANTERN = YoukaisHomecoming.REGISTRATE.block("moon_lantern", p -> new MoonLanternBlock(
							BlockBehaviour.Properties.copy(Blocks.LANTERN)))
					.blockstate(MoonLanternBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();

		}

		{

			var set = new BlockSetType("sikkui", true, SoundType.WOOD,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);

			var sikkuiProp = BlockBehaviour.Properties.copy(Blocks.CLAY);

			SIKKUI = new FullSikkuiSet("sikkui", sikkuiProp);
			FRAMED_SIKKUI = new SikkuiSet("framed_sikkui", sikkuiProp);
			CROSS_SIKKUI = new FullSikkuiSet("cross_framed_sikkui", sikkuiProp);
			GRID_SIKKUI = new SikkuiSet("grid_framed_sikkui", sikkuiProp);

			FINE_GRID_SIKKUI = YoukaisHomecoming.REGISTRATE.block("fine_grid_framed_sikkui", p -> new Block(BlockBehaviour.Properties.copy(Blocks.CLAY)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().cubeColumn("block/" + ctx.getName(),
									pvd.modLoc("block/sikkui/" + ctx.getName() + "_side"),
									pvd.modLoc("block/sikkui/" + ctx.getName() + "_top"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE)
					.simpleItem().register();

			FINE_GRID_SIKKUI_TD = thinTrapdoor("fine_grid_framed_sikkui", sikkuiProp, set, YoukaisHomecoming.loc("block/sikkui/fine_grid_framed_sikkui_side"));

			var doorProp = BlockBehaviour.Properties.copy(Blocks.CLAY)
					.noOcclusion().pushReaction(PushReaction.DESTROY);

			FINE_GRID_SHOJI = thinDoor("fine_grid_framed_shoji", doorProp, set);

			var prop = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
					.instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS);
			HAY = new WoodSet("hay", () -> Blocks.HAY_BLOCK, prop,
					new ResourceLocation("block/hay_block_top"),
					new ResourceLocation("block/hay_block_side"),
					new ResourceLocation("block/hay_block")
			);
			STRAW = new WoodSet("straw", ModBlocks.STRAW_BALE, prop,
					YoukaisHomecoming.loc("block/straw_bale_end"),
					YoukaisHomecoming.loc("block/straw_bale_side"),
					new ResourceLocation(FarmersDelight.MODID, "block/straw_bale")
			);

			for (var e : WoodType.values()) {
				String name = e.name().toLowerCase(Locale.ROOT);
				e.fence = YoukaisHomecoming.REGISTRATE.block(name + "_handrail",
								p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(e.fenceProp).noOcclusion()))
						.blockstate(MultiFenceBlock::genModel)
						.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
						.tag(BlockTags.MINEABLE_WITH_AXE).defaultLoot()
						.register();
				e.table = YoukaisHomecoming.REGISTRATE.block(name + "_dining_table", p -> new WoodTableBlock(
								BlockBehaviour.Properties.copy(e.plankProp)))
						.blockstate(WoodTableBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
				e.seat = YoukaisHomecoming.REGISTRATE.block(name + "_dining_chair", p -> new WoodChairBlock(
								BlockBehaviour.Properties.copy(e.plankProp)))
						.blockstate(WoodChairBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			}

		}

	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	private static BlockEntry<ThinTrapdoorBlock> thinTrapdoor(String id, BlockBehaviour.Properties prop, BlockSetType set, ResourceLocation side) {
		return YoukaisHomecoming.REGISTRATE.block(id + "_trap_door", p ->
						new ThinTrapdoorBlock(prop, set))
				.blockstate((ctx, pvd) -> ThinTrapdoorBlock.buildModels(pvd, ctx.get(), ctx.getName(), side))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.TRAPDOORS)
				.item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(
						new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_trap_door_bottom"))))
				.tag(ItemTags.TRAPDOORS).build()
				.register();
	}

	private static BlockEntry<ThinDoorBlock> thinDoor(String id, BlockBehaviour.Properties prop, BlockSetType set) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new ThinDoorBlock(prop, set))
				.blockstate((ctx, pvd) -> ThinDoorBlock.buildModels(pvd, ctx.get(), ctx.getName(),
						pvd.modLoc("block/sikkui/" + id + "_bottom"),
						pvd.modLoc("block/sikkui/" + id + "_top")))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.DOORS)
				.item().model((ctx, pvd) -> pvd.generated(ctx)).tag(ItemTags.DOORS).build()
				.loot((pvd, b) -> pvd.add(b, pvd.createDoorTable(b))).register();
	}

	public static void register() {
	}

	public static class WoodSet {

		private final Supplier<Block> base;

		public final BlockEntry<HayStairBlock> STAIR;
		public final BlockEntry<HaySlabBlock> SLAB;
		public final BlockEntry<HayTrapDoorBlock> TRAP_DOOR;
		public final BlockEntry<HayVerticalSlabBlock> VERTICAL;

		public WoodSet(String id, Supplier<Block> base, BlockBehaviour.Properties prop,
					   ResourceLocation top, ResourceLocation side, ResourceLocation original) {
			this.base = base;
			var set = new BlockSetType(id, true, SoundType.GRASS,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			STAIR = YoukaisHomecoming.REGISTRATE.block(id + "_stairs", p ->
							new HayStairBlock(() -> base.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side, top, top))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_STAIRS)
					.item().tag(ItemTags.WOODEN_STAIRS).build()
					.register();
			SLAB = YoukaisHomecoming.REGISTRATE.block(id + "_slab", p ->
							new HaySlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, top, top),
							pvd.models().slabTop(ctx.getName() + "_top", side, top, top),
							new ModelFile.UncheckedModelFile(original)))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_SLABS)
					.item().tag(ItemTags.WOODEN_SLABS).build()
					.register();
			TRAP_DOOR = YoukaisHomecoming.REGISTRATE.block(id + "_trap_door", p ->
							new HayTrapDoorBlock(prop, set))
					.blockstate((ctx, pvd) -> pvd.trapdoorBlock(ctx.get(), side, true))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_TRAPDOORS)
					.item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(
							new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_trap_door_bottom"))))
					.tag(ItemTags.WOODEN_TRAPDOORS).build()
					.register();
			VERTICAL = YoukaisHomecoming.REGISTRATE.block(id + "_vertical_slab", p ->
							new HayVerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), VerticalSlabBlock.buildModel(ctx, pvd)
							.texture("top", top).texture("side", side)))
					.tag(BlockTags.MINEABLE_WITH_AXE).item().build().register();
		}

		public void genRecipe(RegistrateRecipeProvider pvd) {
			pvd.stairs(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS,
					STAIR, null, true);
			pvd.slab(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS,
					SLAB, null, true);
			pvd.trapDoor(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS,
					TRAP_DOOR, null);
			YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VERTICAL.get(), 6)::unlockedBy, base.get().asItem())
					.pattern("X").pattern("X").pattern("X")
					.define('X', base.get())
					.save(pvd);
			pvd.stonecutting(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS, VERTICAL, 2);
		}

	}

	public static class SikkuiSet {

		public final BlockEntry<Block> BASE;
		public final BlockEntry<ThinTrapdoorBlock> TRAP_DOOR;

		public SikkuiSet(String id, BlockBehaviour.Properties prop) {
			var set = new BlockSetType(id, true, SoundType.GRASS,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			BASE = YoukaisHomecoming.REGISTRATE.block(id, p -> new Block(prop))
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(),
									pvd.modLoc("block/sikkui/" + ctx.getName()))))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL)
					.simpleItem().register();
			TRAP_DOOR = thinTrapdoor(id, prop, set, YoukaisHomecoming.loc("block/sikkui/" + id));
		}

		public void genRecipe(RegistrateRecipeProvider pvd) {
			pvd.stonecutting(DataIngredient.items(BASE.get()), RecipeCategory.MISC, TRAP_DOOR, 6);
		}

	}

	public static class FullSikkuiSet extends SikkuiSet {

		public final BlockEntry<StairBlock> STAIR;
		public final BlockEntry<SlabBlock> SLAB;
		public final BlockEntry<VerticalSlabBlock> VERTICAL;

		public FullSikkuiSet(String id, BlockBehaviour.Properties prop) {
			super(id, prop);
			ResourceLocation side = YoukaisHomecoming.loc("block/sikkui/" + id);
			STAIR = YoukaisHomecoming.REGISTRATE.block(id + "_stairs", p ->
							new StairBlock(() -> BASE.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE, BlockTags.STAIRS)
					.item().tag(ItemTags.STAIRS).build()
					.register();
			SLAB = YoukaisHomecoming.REGISTRATE.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(YoukaisHomecoming.loc("block/" + id))))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE, BlockTags.SLABS)
					.item().tag(ItemTags.SLABS).build()
					.register();
			VERTICAL = YoukaisHomecoming.REGISTRATE.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), VerticalSlabBlock.buildModel(ctx, pvd)
							.texture("top", side).texture("side", side)))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE).item().build().register();
		}

		public void genRecipe(RegistrateRecipeProvider pvd) {
			super.genRecipe(pvd);
			pvd.stairs(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS,
					STAIR, null, true);
			pvd.slab(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS,
					SLAB, null, true);
			YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VERTICAL.get(), 6)::unlockedBy, BASE.get().asItem())
					.pattern("X").pattern("X").pattern("X")
					.define('X', BASE.get())
					.save(pvd);
			pvd.stonecutting(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS, VERTICAL, 2);
		}

	}

}
