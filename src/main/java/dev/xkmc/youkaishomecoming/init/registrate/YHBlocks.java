package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.block.furniture.*;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.*;
import dev.xkmc.youkaishomecoming.content.pot.moka.*;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlock;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHRecipeGen;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Locale;
import java.util.function.Supplier;

public class YHBlocks {

	public enum WoodType {
		OAK(Items.OAK_PLANKS),
		BIRCH(Items.BIRCH_PLANKS),
		SPRUCE(Items.SPRUCE_PLANKS),
		JUNGLE(Items.JUNGLE_PLANKS),
		DARK_OAK(Items.DARK_OAK_PLANKS),
		ACACIA(Items.ACACIA_PLANKS),
		CRIMSON(Items.CRIMSON_PLANKS),
		WARPED(Items.WARPED_PLANKS),
		MANGROVE(Items.MANGROVE_PLANKS),
		CHERRY(Items.CHERRY_PLANKS),
		BAMBOO(Items.BAMBOO_PLANKS),
		;

		public final ItemLike item;
		public BlockEntry<MultiFenceBlock> fence;

		WoodType(ItemLike item) {
			this.item = item;
		}
	}

	public static final BlockEntry<MokaMakerBlock> MOKA;
	public static final BlockEntityEntry<MokaMakerBlockEntity> MOKA_BE;
	public static final RegistryEntry<RecipeType<MokaRecipe>> MOKA_RT;
	public static final RegistryEntry<RecipeSerializer<MokaRecipe>> MOKA_RS;
	public static final MenuEntry<MokaMenu> MOKA_MT;

	public static final BlockEntry<KettleBlock> KETTLE;
	public static final BlockEntityEntry<KettleBlockEntity> KETTLE_BE;
	public static final RegistryEntry<RecipeType<KettleRecipe>> KETTLE_RT;
	public static final RegistryEntry<RecipeSerializer<KettleRecipe>> KETTLE_RS;
	public static final MenuEntry<KettleMenu> KETTLE_MT;

	public static final BlockEntry<DryingRackBlock> RACK;
	public static final BlockEntityEntry<DryingRackBlockEntity> RACK_BE;
	public static final RegistryEntry<RecipeType<DryingRackRecipe>> RACK_RT;
	public static final RegistryEntry<RecipeSerializer<DryingRackRecipe>> RACK_RS;

	public static final BlockEntry<MokaKitBlock> MOKA_KIT;
	public static final WoodSet HAY, STRAW;

	static {

		{
			MOKA = YoukaisHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			MOKA_BE = YoukaisHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
			MOKA_RT = YoukaisHomecoming.REGISTRATE.recipe("moka_pot");
			MOKA_RS = reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
			MOKA_MT = YoukaisHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

			KETTLE = YoukaisHomecoming.REGISTRATE.block("kettle", p -> new KettleBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(KettleBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = YoukaisHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = YoukaisHomecoming.REGISTRATE.recipe("kettle");
			KETTLE_RS = reg("kettle", () -> new BasePotSerializer<>(KettleRecipe::new));
			KETTLE_MT = YoukaisHomecoming.REGISTRATE.menu("kettle", KettleMenu::new, () -> KettleScreen::new).register();

			RACK = YoukaisHomecoming.REGISTRATE.block("drying_rack", p -> new DryingRackBlock(
							BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).noOcclusion()))
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = YoukaisHomecoming.REGISTRATE.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = YoukaisHomecoming.REGISTRATE.recipe("drying_rack");
			RACK_RS = reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));
		}

		MOKA_KIT = YoukaisHomecoming.REGISTRATE.block("moka_kit", p -> new MokaKitBlock(
						BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
						.texture("maker", pvd.modLoc("block/moka_pot"))
						.texture("cup", pvd.modLoc("block/moka_cup"))
						.texture("foamer", pvd.modLoc("block/moka_foamer"))
						.renderType("cutout")))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

		var prop = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
				.instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS);
		HAY = new WoodSet("hay", () -> Blocks.HAY_BLOCK, prop,
				new ResourceLocation("block/hay_block_top"),
				new ResourceLocation("block/hay_block_side"),
				new ResourceLocation("block/hay_block")
		);
		STRAW = new WoodSet("straw", ModBlocks.STRAW_BALE, prop,
				new ResourceLocation(YoukaisHomecoming.MODID, "block/straw_bale_end"),
				new ResourceLocation(YoukaisHomecoming.MODID, "block/straw_bale_side"),
				new ResourceLocation(FarmersDelight.MODID, "block/straw_bale")
		);

		for (var e : WoodType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			e.fence = YoukaisHomecoming.REGISTRATE.block(name + "_handrail",
							p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
					.blockstate(MultiFenceBlock::genModel)
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
					.tag(BlockTags.MINEABLE_WITH_AXE).defaultLoot()
					.register();
		}

	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
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

}
