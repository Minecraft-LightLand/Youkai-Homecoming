package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.youkaishomecoming.content.block.combined.CombinedBlockSet;
import dev.xkmc.youkaishomecoming.content.block.combined.IBlockSet;
import dev.xkmc.youkaishomecoming.content.block.furniture.MokaKitBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.MoonLanternBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodChairBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodTableBlock;
import dev.xkmc.youkaishomecoming.content.block.variants.*;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaishomecoming.content.pot.ferment.*;
import dev.xkmc.youkaishomecoming.content.pot.kettle.*;
import dev.xkmc.youkaishomecoming.content.pot.moka.*;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHRecipeGen;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Locale;
import java.util.function.Supplier;

public class YHBlocks {

	public enum WoodType implements IBlockSet {
		OAK(Blocks.OAK_PLANKS, Blocks.OAK_FENCE, Items.STRIPPED_OAK_WOOD, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
		BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE, Items.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_SLAB, Blocks.BRICK_STAIRS),
		SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, Items.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
		JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE, Items.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
		DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE, Items.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
		ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE, Items.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
		CRIMSON(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE, Items.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
		WARPED(Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, Items.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
		MANGROVE(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE, Items.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_STAIRS),
		CHERRY(Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE, Items.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_SLAB, Blocks.CHERRY_STAIRS),
		BAMBOO(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE, Items.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_SLAB, Blocks.BAMBOO_STAIRS),
		;

		private final Block plankProp, fenceProp, slab, stairs;
		private final ResourceLocation tex;
		public final ItemLike plank, strippedWood;
		public BlockEntry<MultiFenceBlock> fence;
		public BlockEntry<WoodTableBlock> table;
		public BlockEntry<WoodChairBlock> seat;
		public BlockEntry<VerticalSlabBlock> vertical;

		WoodType(Block plankProp, Block fenceProp, ItemLike strippedWood, Block slab, Block stairs) {
			this.plankProp = plankProp;
			this.fenceProp = fenceProp;
			this.plank = plankProp;
			this.strippedWood = strippedWood;
			this.slab = slab;
			this.stairs = stairs;
			tex = ResourceLocation.withDefaultNamespace("block/" + name().toLowerCase(Locale.ROOT) + "_planks");
		}


		@Override
		public BlockBehaviour.Properties prop() {
			return BlockBehaviour.Properties.ofFullCopy(plankProp);
		}

		@Override
		public Holder<Block> base() {
			return plankProp.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> stairs() {
			return stairs.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> slab() {
			return slab.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> vertical() {
			return vertical;
		}

		@Override
		public ResourceLocation top() {
			return tex;
		}

		@Override
		public ResourceLocation side() {
			return tex;
		}

	}

	private static final SR<RecipeType<?>> RT = SR.of(YoukaisHomecoming.REG, BuiltInRegistries.RECIPE_TYPE);
	private static final SR<RecipeSerializer<?>> RS = SR.of(YoukaisHomecoming.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static final SimpleEntry<CreativeModeTab> TAB_BLOCK, TAB_FURNITURE;

	public static final BlockEntry<MokaMakerBlock> MOKA;
	public static final BlockEntityEntry<MokaMakerBlockEntity> MOKA_BE;
	public static final Val<RecipeType<MokaRecipe>> MOKA_RT;
	public static final Val<BasePotSerializer<MokaRecipe>> MOKA_RS;
	public static final MenuEntry<MokaMenu> MOKA_MT;

	public static final BlockEntry<KettleBlock> KETTLE;
	public static final BlockEntityEntry<KettleBlockEntity> KETTLE_BE;
	public static final Val<RecipeType<KettleRecipe>> KETTLE_RT;
	public static final Val<BasePotSerializer<KettleRecipe>> KETTLE_RS;
	public static final MenuEntry<KettleMenu> KETTLE_MT;

	public static final BlockEntry<DryingRackBlock> RACK;
	public static final BlockEntityEntry<DryingRackBlockEntity> RACK_BE;
	public static final Val<RecipeType<DryingRackRecipe>> RACK_RT;
	public static final Val<RecipeSerializer<DryingRackRecipe>> RACK_RS;

	public static final BlockEntry<DelegateBlock> FERMENT;
	public static final BlockEntityEntry<FermentationTankBlockEntity> FERMENT_BE;
	public static final Val<RecipeType<FermentationRecipe<?>>> FERMENT_RT;
	public static final Val<BaseRecipe.RecType<SimpleFermentationRecipe, FermentationRecipe<?>, FermentationDummyContainer>> FERMENT_RS;

	public static final BlockEntry<MokaKitBlock> MOKA_KIT;
	public static final BlockEntry<MoonLanternBlock> MOON_LANTERN;

	public static SikkuiGroup SIKKUI, LIGHT_YELLOW_SIKKUI, BROWN_SIKKUI;

	public static final WoodSet HAY, STRAW;

	static {
		var reg = YoukaisHomecoming.REGISTRATE;

		{
			MOKA = reg.block("moka_pot", p -> new MokaMakerBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			MOKA_BE = reg.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
			MOKA_RT = RT.reg("moka_pot", RecipeType::simple);
			MOKA_RS = RS.reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
			MOKA_MT = reg.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

			KETTLE = reg.block("kettle", p -> new KettleBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(KettleBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = reg.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = RT.reg("kettle", RecipeType::simple);
			KETTLE_RS = RS.reg("kettle", () -> new BasePotSerializer<>(KettleRecipe::new));
			KETTLE_MT = reg.menu("kettle", KettleMenu::new, () -> KettleScreen::new).register();

			RACK = reg.block("drying_rack", p -> new DryingRackBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()))
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = reg.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = RT.reg("drying_rack", RecipeType::simple);
			RACK_RS = RS.reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));

			FERMENT = reg.block("fermentation_tank", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS),
									new FermentationTankBlock(), FermentationTankBlock.TE))
					.blockstate(FermentationTankBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.register();
			FERMENT_BE = reg.blockEntity("fermentation_tank", FermentationTankBlockEntity::new)
					.validBlock(FERMENT).renderer(() -> FermentationTankRenderer::new).register();
			FERMENT_RT = RT.reg("fermentation", RecipeType::simple);
			FERMENT_RS = RS.reg("simple_fermentation", () -> new BaseRecipe.RecType<>(SimpleFermentationRecipe.class, FERMENT_RT));

		}

		{

			MOON_LANTERN = reg.block("moon_lantern", p -> new MoonLanternBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)))
					.blockstate(MoonLanternBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();

		}

		TAB_FURNITURE = reg.buildModCreativeTab("furniture", "Youkai's Homecoming - Furniture",
				e -> e.icon(WoodType.OAK.table::asStack));

		{

			MOKA_KIT = reg.block("moka_kit", p -> new MokaKitBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
							.texture("maker", pvd.modLoc("block/moka_pot"))
							.texture("cup", pvd.modLoc("block/moka_cup"))
							.texture("foamer", pvd.modLoc("block/moka_foamer"))
							.renderType("cutout")))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

			for (var e : WoodType.values()) {
				String name = e.name().toLowerCase(Locale.ROOT);
				e.fence = reg.block(name + "_handrail",
								p -> new MultiFenceBlock(BlockBehaviour.Properties.ofFullCopy(e.fenceProp).noOcclusion()))
						.blockstate(MultiFenceBlock::genModel)
						.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
						.tag(BlockTags.MINEABLE_WITH_AXE).defaultLoot()
						.recipe((ctx, pvd) -> pvd.stonecutting(DataIngredient.items(e.plank), RecipeCategory.MISC, ctx))
						.register();

				e.table = reg.block(name + "_dining_table", p -> new WoodTableBlock(
								BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
						.blockstate(WoodTableBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
						.recipe((ctx, pvd) -> WoodTableBlock.genRecipe(pvd, e, ctx))
						.register();

				e.seat = reg.block(name + "_dining_chair", p -> new WoodChairBlock(
								BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
						.blockstate(WoodChairBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
						.recipe((ctx, pvd) -> WoodChairBlock.genRecipe(pvd, e, ctx))
						.register();

			}

		}

		TAB_BLOCK = reg.buildModCreativeTab("building_blocks", "Youkai's Homecoming - Building Blocks",
				e -> e.icon(YHBlocks.SIKKUI.FINE_GRID_SIKKUI::asStack));

		{
			SIKKUI = new SikkuiGroup(reg, "", (ctx, pvd) ->
					YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 2)::unlockedBy, ModItems.STRAW.get())
							.pattern("ABA").pattern("DCD").pattern("ABA")
							.define('A', Items.CLAY_BALL)
							.define('B', Items.BONE_MEAL)
							.define('D', Items.PAPER)
							.define('C', ModItems.STRAW.get())
							.save(pvd));
			LIGHT_YELLOW_SIKKUI = new SikkuiGroup(reg, "light_yellow_", (ctx, pvd) ->
					YHRecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 2)::unlockedBy, ModItems.STRAW.get())
							.requires(Items.SAND).requires(Items.CLAY).requires(ModItems.STRAW.get())
							.save(pvd));
			BROWN_SIKKUI = new SikkuiGroup(reg, "brown_", (ctx, pvd) ->
					YHRecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 2)::unlockedBy, ModItems.STRAW.get())
							.requires(Items.DIRT).requires(Items.CLAY).requires(ModItems.STRAW.get())
							.save(pvd));

			var prop = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
					.instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS);
			HAY = new WoodSet(reg, "hay", () -> Blocks.HAY_BLOCK, prop,
					ResourceLocation.withDefaultNamespace("block/hay_block_top"),
					ResourceLocation.withDefaultNamespace("block/hay_block_side"),
					ResourceLocation.withDefaultNamespace("block/hay_block")
			);
			STRAW = new WoodSet(reg, "straw", ModBlocks.STRAW_BALE, prop,
					YoukaisHomecoming.loc("block/straw_bale_end"),
					YoukaisHomecoming.loc("block/straw_bale_side"),
					ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "block/straw_bale")
			);

			for (var e : WoodType.values()) {
				String name = e.name().toLowerCase(Locale.ROOT);
				e.vertical = reg.block(name + "_vertical_slab", p ->
								new VerticalSlabBlock(prop))
						.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, e.side(), e.side()))
						.tag(YHTagGen.VERTICAL_SLAB, BlockTags.MINEABLE_WITH_AXE).item().build()
						.recipe((ctx, pvd) -> VerticalSlabBlock.genRecipe(pvd, () -> e.plank, ctx))
						.register();
			}
			
			for (var a : WoodType.values()) {
				for (var b : WoodType.values()) {
					CombinedBlockSet.add(reg, a, b);
				}
			}

		}

	}

	private static BlockEntry<ThinTrapdoorBlock> thinTrapdoor(L2Registrate reg, String id, BlockBehaviour.Properties prop, BlockSetType set, ResourceLocation side, Supplier<Block> base) {
		return reg.block(id + "_trap_door", p ->
						new ThinTrapdoorBlock(prop, set))
				.blockstate((ctx, pvd) -> ThinTrapdoorBlock.buildModels(pvd, ctx.get(), ctx.getName(), side))
				.tag(YHTagGen.SIKKUI, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.TRAPDOORS)
				.item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(
						new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_trap_door_bottom"))))
				.tag(ItemTags.TRAPDOORS).tab(TAB_FURNITURE.key()).build()
				.recipe((ctx, pvd) -> pvd.stonecutting(DataIngredient.items(base.get()), RecipeCategory.MISC, ctx, 6))
				.register();
	}

	private static BlockEntry<ThinDoorBlock> thinDoor(
			L2Registrate reg, String id, BlockBehaviour.Properties prop, BlockSetType set,
			ResourceLocation bottom, ResourceLocation top, Supplier<Block> base
	) {
		return reg.block(id, p -> new ThinDoorBlock(prop, set))
				.blockstate((ctx, pvd) -> ThinDoorBlock.buildModels(pvd, ctx.get(), ctx.getName(), bottom, top))
				.tag(YHTagGen.SIKKUI, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE, BlockTags.DOORS)
				.item().model((ctx, pvd) -> pvd.generated(ctx)).tag(ItemTags.DOORS).tab(TAB_FURNITURE.key()).build()
				.loot((pvd, b) -> pvd.add(b, pvd.createDoorTable(b)))
				.recipe((ctx, pvd) -> pvd.stonecutting(DataIngredient.items(base.get()), RecipeCategory.MISC, ctx, 3))
				.register();
	}

	public static void register() {

	}

	public static class SikkuiGroup {

		public FullSikkuiSet SIKKUI, CROSS_SIKKUI;
		public SikkuiSet FRAMED_SIKKUI, GRID_SIKKUI;
		public BlockEntry<Block> FINE_GRID_SIKKUI;
		public BlockEntry<ThinTrapdoorBlock> FINE_GRID_SIKKUI_TD;
		public BlockEntry<ThinDoorBlock> FINE_GRID_SHOJI;

		public SikkuiGroup(L2Registrate reg, String id, Ingredient dye) {
			this(reg, id, (ctx, pvd) -> SikkuiGroup.genColor(pvd, YHBlocks.SIKKUI.SIKKUI.BASE, ctx, dye));
		}

		public SikkuiGroup(L2Registrate reg, String id, BlockRecipe builder) {

			var set = new BlockSetType(id + "_sikkui", true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.WOOD,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);

			var sikkuiProp = BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY);

			SIKKUI = new FullSikkuiSet(reg, id + "sikkui", sikkuiProp, builder);
			FRAMED_SIKKUI = new SikkuiSet(reg, id + "framed_sikkui", sikkuiProp, (ctx, pvd) -> genStickRecipe(pvd, SIKKUI.BASE, ctx));
			CROSS_SIKKUI = new FullSikkuiSet(reg, id + "cross_framed_sikkui", sikkuiProp, (ctx, pvd) -> genStickRecipe(pvd, FRAMED_SIKKUI.BASE, ctx));
			GRID_SIKKUI = new SikkuiSet(reg, id + "grid_framed_sikkui", sikkuiProp, (ctx, pvd) -> genStickRecipe(pvd, CROSS_SIKKUI.BASE, ctx));

			FINE_GRID_SIKKUI = reg.block(id + "fine_grid_framed_sikkui",
							p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().cubeColumn("block/" + ctx.getName(),
									pvd.modLoc("block/sikkui/" + ctx.getName() + "_side"),
									pvd.modLoc("block/sikkui/" + id + "framed_sikkui"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE)
					.simpleItem()
					.recipe((ctx, pvd) -> genStickRecipe(pvd, GRID_SIKKUI.BASE, ctx))
					.register();

			FINE_GRID_SIKKUI_TD = thinTrapdoor(reg, id + "fine_grid_framed_sikkui", sikkuiProp, set,
					reg.loc("block/sikkui/" + id + "fine_grid_framed_sikkui_side"), FINE_GRID_SIKKUI);

			var doorProp = BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)
					.noOcclusion().pushReaction(PushReaction.DESTROY);

			FINE_GRID_SHOJI = thinDoor(reg, id + "fine_grid_framed_shoji", doorProp, set,
					reg.loc("block/sikkui/" + id + "fine_grid_framed_shoji_bottom"),
					reg.loc("block/sikkui/" + id + "fine_grid_framed_sikkui_side"),
					FINE_GRID_SIKKUI);

		}

		private static void genStickRecipe(RegistrateRecipeProvider pvd, Supplier<? extends ItemLike> parent, Supplier<? extends ItemLike> self) {
			YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, self.get())::unlockedBy, parent.get().asItem())
					.pattern(" A ").pattern("ABA").pattern(" A ")
					.define('A', Items.STICK)
					.define('B', parent.get())
					.save(pvd);
		}

		protected static void genColor(RegistrateRecipeProvider pvd, Supplier<? extends ItemLike> parent, Supplier<? extends ItemLike> self, Ingredient dye) {
			YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, self.get(), 8)::unlockedBy, parent.get().asItem())
					.pattern("AAA").pattern("ABA").pattern("AAA")
					.define('B', dye)
					.define('A', parent.get())
					.save(pvd);
		}

	}

	public static class WoodSet {

		private final Supplier<Block> base;

		public final BlockEntry<HayStairBlock> STAIR;
		public final BlockEntry<HaySlabBlock> SLAB;
		public final BlockEntry<HayTrapDoorBlock> TRAP_DOOR;
		public final BlockEntry<HayVerticalSlabBlock> VERTICAL;

		public WoodSet(L2Registrate reg, String id, Supplier<Block> base, BlockBehaviour.Properties prop,
					   ResourceLocation top, ResourceLocation side, ResourceLocation original) {
			this.base = base;
			var set = new BlockSetType(id, true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.GRASS,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			STAIR = reg.block(id + "_stairs", p ->
							new HayStairBlock(base.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side, top, top))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_STAIRS)
					.item().tag(ItemTags.WOODEN_STAIRS).build()
					.recipe((ctx, pvd) -> pvd.stairs(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			SLAB = reg.block(id + "_slab", p ->
							new HaySlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, top, top),
							pvd.models().slabTop(ctx.getName() + "_top", side, top, top),
							new ModelFile.UncheckedModelFile(original)))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_SLABS)
					.item().tag(ItemTags.WOODEN_SLABS).build()
					.recipe((ctx, pvd) -> pvd.slab(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			TRAP_DOOR = reg.block(id + "_trap_door", p ->
							new HayTrapDoorBlock(prop, set))
					.blockstate((ctx, pvd) -> pvd.trapdoorBlock(ctx.get(), side, true))
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.WOODEN_TRAPDOORS)
					.item().model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(
							new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_trap_door_bottom"))))
					.tag(ItemTags.WOODEN_TRAPDOORS).build()
					.recipe((ctx, pvd) -> pvd.trapDoor(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null))
					.register();
			VERTICAL = reg.block(id + "_vertical_slab", p ->
							new HayVerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, top, side))
					.tag(YHTagGen.VERTICAL_SLAB, BlockTags.MINEABLE_WITH_AXE).item().build()
					.recipe((ctx, pvd) -> VerticalSlabBlock.genRecipe(pvd, base, ctx))
					.register();
		}

	}

	public static class SikkuiSet {

		public final BlockEntry<Block> BASE;
		public final BlockEntry<ThinTrapdoorBlock> TRAP_DOOR;

		public SikkuiSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, BlockRecipe builder) {
			var set = new BlockSetType(id, true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.GRAVEL,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			BASE = reg.block(id, p -> new Block(prop))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), reg.loc("block/sikkui/" + id))))
					.tag(YHTagGen.SIKKUI, BlockTags.MINEABLE_WITH_SHOVEL)
					.simpleItem().recipe(builder).register();
			TRAP_DOOR = thinTrapdoor(reg, id, prop, set, reg.loc("block/sikkui/" + id), BASE);
		}

	}

	public static class FullSikkuiSet extends SikkuiSet {

		public final BlockEntry<StairBlock> STAIR;
		public final BlockEntry<SlabBlock> SLAB;
		public final BlockEntry<VerticalSlabBlock> VERTICAL;

		public FullSikkuiSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, BlockRecipe builder) {
			super(reg, id, prop, builder);
			ResourceLocation side = reg.loc("block/sikkui/" + id);
			STAIR = reg.block(id + "_stairs", p ->
							new StairBlock(BASE.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(YHTagGen.SIKKUI, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE, BlockTags.STAIRS)
					.item().tag(ItemTags.STAIRS).build()
					.recipe((ctx, pvd) -> pvd.stairs(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			SLAB = reg.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
					.tag(YHTagGen.SIKKUI, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE, BlockTags.SLABS)
					.item().tag(ItemTags.SLABS).build()
					.recipe((ctx, pvd) -> pvd.slab(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			VERTICAL = reg.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, side, side))
					.tag(YHTagGen.SIKKUI, YHTagGen.VERTICAL_SLAB, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE).item().build()
					.recipe((ctx, pvd) -> VerticalSlabBlock.genRecipe(pvd, BASE, ctx))
					.register();
		}

	}

	public static class StoneSet {

		public final Supplier<Block> BASE;
		public final BlockEntry<StairBlock> STAIR;
		public final BlockEntry<SlabBlock> SLAB;
		public final BlockEntry<VerticalSlabBlock> VERTICAL;

		public StoneSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, Supplier<Block> base) {
			this.BASE = base;
			ResourceLocation side = reg.loc("block/" + id);
			STAIR = reg.block(id + "_stairs", p ->
							new StairBlock(BASE.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.STAIRS)
					.item().tag(ItemTags.STAIRS).build()
					.recipe((ctx, pvd) -> pvd.stairs(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			SLAB = reg.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.SLABS)
					.item().tag(ItemTags.SLABS).build()
					.recipe((ctx, pvd) -> pvd.slab(DataIngredient.items(BASE.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
					.register();
			VERTICAL = reg.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, side, side))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, YHTagGen.VERTICAL_SLAB).item().build()
					.recipe((ctx, pvd) -> VerticalSlabBlock.genRecipe(pvd, BASE, ctx))
					.register();
		}

	}

	public interface BlockRecipe extends NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateRecipeProvider> {
	}

}
