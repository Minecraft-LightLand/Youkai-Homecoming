package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.youkaishomecoming.content.block.food.BowlBlock;
import dev.xkmc.youkaishomecoming.content.block.food.IronBowlBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.MoonLanternBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodChairBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodTableBlock;
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
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleContainer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
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
import dev.xkmc.youkaishomecoming.init.food.InitializationMarker;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;

import java.util.Locale;

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

	private static final SR<RecipeType<?>> RT = SR.of(YoukaisHomecoming.REG, Registries.RECIPE_TYPE);
	private static final SR<RecipeSerializer<?>> RS = SR.of(YoukaisHomecoming.REG, Registries.RECIPE_SERIALIZER);

	public static final BlockEntry<DelegateBlock> KETTLE;
	public static final BlockEntityEntry<KettleBlockEntity> KETTLE_BE;
	public static final Val<RecipeType<KettleRecipe>> KETTLE_RT;
	public static final Val<BaseRecipe.RecType<KettleRecipe, KettleRecipe, KettleContainer>> KETTLE_RS;

	public static final BlockEntry<DryingRackBlock> RACK;
	public static final BlockEntityEntry<DryingRackBlockEntity> RACK_BE;
	public static final Val<RecipeType<DryingRackRecipe>> RACK_RT;
	public static final Val<RecipeSerializer<DryingRackRecipe>> RACK_RS;

	public static final BlockEntry<DelegateBlock> FERMENT;
	public static final BlockEntityEntry<FermentationTankBlockEntity> FERMENT_BE;
	public static final Val<RecipeType<FermentationRecipe<?>>> FERMENT_RT;
	public static final Val<BaseRecipe.RecType<SimpleFermentationRecipe, FermentationRecipe<?>, FermentationDummyContainer>> FERMENT_RS;

	public static final BlockEntry<DelegateBlock> BASIN;
	public static final BlockEntityEntry<BasinBlockEntity> BASIN_BE;
	public static final Val<RecipeType<BasinRecipe<?>>> BASIN_RT;
	public static final Val<BaseRecipe.RecType<SimpleBasinRecipe, BasinRecipe<?>, BasinInput>> BASIN_RS;

	public static final BlockEntry<DelegateBlock> STEAMER_POT;
	public static final BlockEntry<DelegateBlock> STEAMER_RACK;
	public static final BlockEntry<DelegateBlock> STEAMER_LID;
	public static final BlockEntityEntry<SteamerBlockEntity> STEAMER_BE;
	public static final Val<RecipeType<SteamingRecipe>> STEAM_RT;
	public static final Val<RecipeSerializer<SteamingRecipe>> STEAM_RS;

	public static final BlockEntry<CopperTankBlock> COPPER_TANK;
	public static final BlockEntityEntry<CopperTankBlockEntity> TANK_BE;
	public static final BlockEntry<DelegateBlock> COPPER_FAUCET;
	public static final BlockEntityEntry<CopperFaucetBlockEntity> FAUCET_BE;

	public static final BlockEntry<DelegateBlock> CUISINE_BOARD;
	public static final BlockEntityEntry<CuisineBoardBlockEntity> CUISINE_BOARD_BE;
	public static final Val<RecipeType<CuisineRecipe<?>>> CUISINE_RT;
	public static final Val<BaseRecipe.RecType<OrderedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_ORDER;
	public static final Val<BaseRecipe.RecType<UnorderedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_UNORDER;
	public static final Val<BaseRecipe.RecType<MixedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_MIXED;
	public static final Val<BaseRecipe.RecType<FixedCuisineRecipe, CuisineRecipe<?>, CuisineInv>> CUISINE_FIXED;

	public static final BlockEntry<IronBowlBlock> IRON_BOWL, IRON_POT, STOCKPOT;
	public static final BlockEntry<BowlBlock> WOOD_BOWL, BAMBOO_BOWL;
	public static final BlockEntry<DelegateBlock> SMALL_POT, SHORT_POT, LARGE_POT;
	public static final ItemEntry<BigSpoonItem> BIG_SPOON;
	public static final BlockEntityEntry<SmallCookingPotBlockEntity> SMALL_POT_BE;
	public static final BlockEntityEntry<MidCookingPotBlockEntity> MID_POT_BE;
	public static final BlockEntityEntry<LargeCookingPotBlockEntity> LARGE_POT_BE;
	public static final Val<RecipeType<PotCookingRecipe<?>>> COOKING_RT;
	public static final Val<BaseRecipe.RecType<UnorderedCookingRecipe, PotCookingRecipe<?>, CookingInv>> COOKING_UNORDER;
	public static final Val<RecipeType<SoupBaseRecipe<?>>> SOUP_RT;
	public static final Val<BaseRecipe.RecType<SimpleSoupBaseRecipe, SoupBaseRecipe<?>, CookingInv>> IMMEDIATE_SOUP;


	public static final BlockEntry<DelegateBlock> OAK_INGREDIENT_RACK;
	public static final BlockEntityEntry<IngredientRackBlockEntity> INGREDIENT_RACK_BE;

	public static final BlockEntry<DelegateBlock> OAK_SAUCE_RACK;
	public static final BlockEntityEntry<SauceRackBlockEntity> SAUCE_RACK_BE;

	public static final BlockEntry<DelegateBlock> SPRUCE_WINE_SHELF;
	public static final BlockEntityEntry<WineShelfBlockEntity> WINE_SHELF_BE;

	public static final BlockEntry<MoonLanternBlock> MOON_LANTERN;


	static {
		InitializationMarker.expectAndAdvance(1);
		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.TAB.key());

		// moka kettle, rack
		{
			KETTLE = YoukaisHomecoming.REGISTRATE.block("kettle", p -> DelegateBlock.newBaseBlock(
							p.sound(SoundType.METAL),
							BlockTemplates.HORIZONTAL, new KettleBlock(), KettleBlock.TE))
					.initialProperties(() -> Blocks.TERRACOTTA)
					.blockstate(KettleBlock::buildModel).item().properties(e -> e.stacksTo(1)).build()
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = YoukaisHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = RT.reg("kettle", RecipeType::simple);
			KETTLE_RS = RS.reg("kettle", () -> new BaseRecipe.RecType<>(KettleRecipe.class, KETTLE_RT));

			RACK = YoukaisHomecoming.REGISTRATE.block("drying_rack", p -> new DryingRackBlock(
							p.noOcclusion()))
					.initialProperties(() -> Blocks.BAMBOO_PLANKS)
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = RT.reg("drying_rack", RecipeType::simple);
			RACK_RS = RS.reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));
		}

		//ferment, basin
		{
			FERMENT = YoukaisHomecoming.REGISTRATE.block("fermentation_tank", p ->
							DelegateBlock.newBaseBlock(p,
									new FermentationTankBlock(), FermentationTankBlock.TE))
					.initialProperties(() -> Blocks.OAK_PLANKS)
					.blockstate(FermentationTankBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.MINEABLE_WITH_PICKAXE)
					.register();
			FERMENT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("fermentation_tank", FermentationTankBlockEntity::new)
					.validBlock(FERMENT).renderer(() -> FermentationTankRenderer::new).register();
			FERMENT_RT = RT.reg("fermentation", RecipeType::simple);
			FERMENT_RS = RS.reg("simple_fermentation", () -> new BaseRecipe.RecType<>(SimpleFermentationRecipe.class, FERMENT_RT));

			BASIN = YoukaisHomecoming.REGISTRATE.block("wood_basin", p ->
							DelegateBlock.newBaseBlock(p,
									new BasinBlock(), BasinBlock.TE))
					.initialProperties(() -> Blocks.OAK_PLANKS)
					.blockstate(BasinBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.register();

			BASIN_BE = YoukaisHomecoming.REGISTRATE.blockEntity("basin", BasinBlockEntity::new)
					.validBlock(BASIN).renderer(() -> BasinRenderer::new).register();

			BASIN_RT = RT.reg("basin", RecipeType::simple);
			BASIN_RS = RS.reg("simple_basin", () -> new BaseRecipe.RecType<>(SimpleBasinRecipe.class, BASIN_RT));

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

			STEAM_RT = RT.reg("steaming", RecipeType::simple);
			STEAM_RS = RS.reg("steaming", () -> new SimpleCookingSerializer<>(SteamingRecipe::new, 100));

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

			COOKING_RT = RT.reg("pot_cooking", RecipeType::simple);
			COOKING_UNORDER = RS.reg("unordered_cooking", () -> new BaseRecipe.RecType<>(UnorderedCookingRecipe.class, COOKING_RT));
			SOUP_RT = RT.reg("soup_base", RecipeType::simple);
			IMMEDIATE_SOUP = RS.reg("immediate_soup", () -> new BaseRecipe.RecType<>(SimpleSoupBaseRecipe.class, SOUP_RT));

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
							BlockTemplates.HORIZONTAL, CopperFaucetBlock.INS, CopperFaucetBlock.TE))
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
			CUISINE_RT = RT.reg("cuisine", RecipeType::simple);
			CUISINE_ORDER = RS.reg("cuisine_ordered", () -> new BaseRecipe.RecType<>(OrderedCuisineRecipe.class, CUISINE_RT));
			CUISINE_UNORDER = RS.reg("cuisine_unordered", () -> new BaseRecipe.RecType<>(UnorderedCuisineRecipe.class, CUISINE_RT));
			CUISINE_MIXED = RS.reg("cuisine_mixed", () -> new BaseRecipe.RecType<>(MixedCuisineRecipe.class, CUISINE_RT));
			CUISINE_FIXED = RS.reg("cuisine_fixed", () -> new BaseRecipe.RecType<>(FixedCuisineRecipe.class, CUISINE_RT));
		}

		//storage blocks
		{

			OAK_INGREDIENT_RACK = YoukaisHomecoming.REGISTRATE.block("oak_ingredient_rack", p ->
							DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new IngredientRackBlock(), IngredientRackBlock.BE))
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
							DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new SauceRackBlock(), SauceRackBlock.BE))
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
							DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new WineShelfBlock(), WineShelfBlock.BE))
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

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.DECO.key());

		{
			MOON_LANTERN = YoukaisHomecoming.REGISTRATE.block("moon_lantern", MoonLanternBlock::new)
					.initialProperties(() -> Blocks.LANTERN)
					.blockstate(MoonLanternBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();

		}

		{

			for (var e : WoodType.values()) {
				String name = e.name().toLowerCase(Locale.ROOT);
				e.table = YoukaisHomecoming.REGISTRATE.block(name + "_dining_table", WoodTableBlock::new)
						.initialProperties(() -> e.plankProp)
						.blockstate(WoodTableBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
				e.seat = YoukaisHomecoming.REGISTRATE.block(name + "_dining_chair", WoodChairBlock::new)
						.initialProperties(() -> e.plankProp)
						.blockstate(WoodChairBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			}

		}

	}

	public static void register() {
	}

}
