package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.core.DelegateBlock;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
		WARPED(Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_SLAB),
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

	private static final SR<RecipeType<?>> RT = SR.of(YoukaisHomecoming.REG, BuiltInRegistries.RECIPE_TYPE);
	private static final SR<RecipeSerializer<?>> RS = SR.of(YoukaisHomecoming.REG, BuiltInRegistries.RECIPE_SERIALIZER);

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

	public static FullSikkuiSet SIKKUI, CROSS_SIKKUI;
	public static SikkuiSet FRAMED_SIKKUI, GRID_SIKKUI;
	public static final BlockEntry<Block> FINE_GRID_SIKKUI;
	public static final BlockEntry<ThinTrapdoorBlock> FINE_GRID_SIKKUI_TD;
	public static final BlockEntry<ThinDoorBlock> FINE_GRID_SHOJI;

	public static final WoodSet HAY, STRAW;

	static {

		{
			MOKA = YoukaisHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			MOKA_BE = YoukaisHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
			MOKA_RT = RT.reg("moka_pot", RecipeType::simple);
			MOKA_RS = RS.reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
			MOKA_MT = YoukaisHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

			KETTLE = YoukaisHomecoming.REGISTRATE.block("kettle", p -> new KettleBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(KettleBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = YoukaisHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = RT.reg("kettle", RecipeType::simple);
			KETTLE_RS = RS.reg("kettle", () -> new BasePotSerializer<>(KettleRecipe::new));
			KETTLE_MT = YoukaisHomecoming.REGISTRATE.menu("kettle", KettleMenu::new, () -> KettleScreen::new).register();

			RACK = YoukaisHomecoming.REGISTRATE.block("drying_rack", p -> new DryingRackBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()))
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = RT.reg("drying_rack", RecipeType::simple);
			RACK_RS = RS.reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));

			FERMENT = YoukaisHomecoming.REGISTRATE.block("fermentation_tank", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS),
									new FermentationTankBlock(), FermentationTankBlock.TE))
					.blockstate(FermentationTankBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.register();
			FERMENT_BE = YoukaisHomecoming.REGISTRATE.blockEntity("fermentation_tank", FermentationTankBlockEntity::new)
					.validBlock(FERMENT).renderer(() -> FermentationTankRenderer::new).register();
			FERMENT_RT = RT.reg("fermentation", RecipeType::simple);
			FERMENT_RS = RS.reg("simple_fermentation", () -> new BaseRecipe.RecType<>(SimpleFermentationRecipe.class, FERMENT_RT));

		}

		{

			MOKA_KIT = YoukaisHomecoming.REGISTRATE.block("moka_kit", p -> new MokaKitBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
							.texture("maker", pvd.modLoc("block/moka_pot"))
							.texture("cup", pvd.modLoc("block/moka_cup"))
							.texture("foamer", pvd.modLoc("block/moka_foamer"))
							.renderType("cutout")))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

			MOON_LANTERN = YoukaisHomecoming.REGISTRATE.block("moon_lantern", p -> new MoonLanternBlock(
							BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)))
					.blockstate(MoonLanternBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();

		}

		{

			var set = new BlockSetType("sikkui", true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.WOOD,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);

			var sikkuiProp = BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY);

			SIKKUI = new FullSikkuiSet("sikkui", sikkuiProp);
			FRAMED_SIKKUI = new SikkuiSet("framed_sikkui", sikkuiProp);
			CROSS_SIKKUI = new FullSikkuiSet("cross_framed_sikkui", sikkuiProp);
			GRID_SIKKUI = new SikkuiSet("grid_framed_sikkui", sikkuiProp);

			FINE_GRID_SIKKUI = YoukaisHomecoming.REGISTRATE.block("fine_grid_framed_sikkui",
							p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().cubeColumn("block/" + ctx.getName(),
									pvd.modLoc("block/" + ctx.getName() + "_side"),
									pvd.modLoc("block/" + ctx.getName() + "_top"))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_AXE)
					.simpleItem().register();

			FINE_GRID_SIKKUI_TD = thinTrapdoor("fine_grid_framed_sikkui", sikkuiProp, set, YoukaisHomecoming.loc("block/fine_grid_framed_sikkui_side"));

			var doorProp = BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY)
					.noOcclusion().pushReaction(PushReaction.DESTROY);

			FINE_GRID_SHOJI = thinDoor("fine_grid_framed_shoji", doorProp, set);

			var prop = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
					.instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS);
			HAY = new WoodSet("hay", () -> Blocks.HAY_BLOCK, prop,
					ResourceLocation.withDefaultNamespace("block/hay_block_top"),
					ResourceLocation.withDefaultNamespace("block/hay_block_side"),
					ResourceLocation.withDefaultNamespace("block/hay_block")
			);
			STRAW = new WoodSet("straw", ModBlocks.STRAW_BALE, prop,
					YoukaisHomecoming.loc("block/straw_bale_end"),
					YoukaisHomecoming.loc("block/straw_bale_side"),
					ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "block/straw_bale")
			);

			for (var e : WoodType.values()) {
				String name = e.name().toLowerCase(Locale.ROOT);
				e.fence = YoukaisHomecoming.REGISTRATE.block(name + "_handrail",
								p -> new MultiFenceBlock(BlockBehaviour.Properties.ofFullCopy(e.fenceProp).noOcclusion()))
						.blockstate(MultiFenceBlock::genModel)
						.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
						.tag(BlockTags.MINEABLE_WITH_AXE).defaultLoot()
						.register();
				e.table = YoukaisHomecoming.REGISTRATE.block(name + "_dining_table", p -> new WoodTableBlock(
								BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
						.blockstate(WoodTableBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
				e.seat = YoukaisHomecoming.REGISTRATE.block(name + "_dining_chair", p -> new WoodChairBlock(
								BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
						.blockstate(WoodChairBlock::buildStates)
						.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			}

		}

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
						pvd.modLoc("block/" + id + "_bottom"),
						pvd.modLoc("block/" + id + "_top")))
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
			var set = new BlockSetType(id, true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.GRASS,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			STAIR = YoukaisHomecoming.REGISTRATE.block(id + "_stairs", p ->
							new HayStairBlock(base.get().defaultBlockState(), prop))
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
			var set = new BlockSetType(id, true, true, true,
					BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.GRAVEL,
					SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN,
					SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
					SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
			BASE = YoukaisHomecoming.REGISTRATE.block(id, p -> new Block(prop))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL)
					.simpleItem().register();
			TRAP_DOOR = thinTrapdoor(id, prop, set, YoukaisHomecoming.loc("block/" + id));
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
			ResourceLocation side = YoukaisHomecoming.loc("block/" + id);
			STAIR = YoukaisHomecoming.REGISTRATE.block(id + "_stairs", p ->
							new StairBlock(BASE.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE, BlockTags.STAIRS)
					.item().tag(ItemTags.STAIRS).build()
					.register();
			SLAB = YoukaisHomecoming.REGISTRATE.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
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
