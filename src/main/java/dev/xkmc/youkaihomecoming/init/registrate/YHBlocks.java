package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaihomecoming.content.block.furniture.MokaKitBlock;
import dev.xkmc.youkaihomecoming.content.block.furniture.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaihomecoming.content.pot.kettle.*;
import dev.xkmc.youkaihomecoming.content.pot.moka.*;
import dev.xkmc.youkaihomecoming.content.pot.rack.DryingRackBlock;
import dev.xkmc.youkaihomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaihomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaihomecoming.content.pot.rack.DryingRackRenderer;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

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

	static {

		{
			MOKA = YoukaiHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			MOKA_BE = YoukaiHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
			MOKA_RT = YoukaiHomecoming.REGISTRATE.recipe("moka_pot");
			MOKA_RS = reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
			MOKA_MT = YoukaiHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

			KETTLE = YoukaiHomecoming.REGISTRATE.block("kettle", p -> new KettleBlock(
							BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
					.blockstate(KettleBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
					.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
			KETTLE_BE = YoukaiHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
			KETTLE_RT = YoukaiHomecoming.REGISTRATE.recipe("kettle");
			KETTLE_RS = reg("kettle", () -> new BasePotSerializer<>(KettleRecipe::new));
			KETTLE_MT = YoukaiHomecoming.REGISTRATE.menu("kettle", KettleMenu::new, () -> KettleScreen::new).register();

			RACK = YoukaiHomecoming.REGISTRATE.block("drying_rack", p -> new DryingRackBlock(
							BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).noOcclusion()))
					.blockstate(DryingRackBlock::buildModel)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();
			RACK_BE = YoukaiHomecoming.REGISTRATE.blockEntity("drying_rack", DryingRackBlockEntity::new)
					.validBlock(RACK).renderer(() -> DryingRackRenderer::new).register();
			RACK_RT = YoukaiHomecoming.REGISTRATE.recipe("drying_rack");
			RACK_RS = reg("drying_rack", () -> new SimpleCookingSerializer<>(DryingRackRecipe::new, 100));
		}

		MOKA_KIT = YoukaiHomecoming.REGISTRATE.block("moka_kit", p -> new MokaKitBlock(
						BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
						.texture("maker", pvd.modLoc("block/moka_pot"))
						.texture("cup", pvd.modLoc("block/moka_cup"))
						.texture("foamer", pvd.modLoc("block/moka_foamer"))
						.renderType("cutout")))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

		for (var e : WoodType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			e.fence = YoukaiHomecoming.REGISTRATE.block(name + "_handrail",
							p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
					.blockstate(MultiFenceBlock::genModel)
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
					.tag(BlockTags.MINEABLE_WITH_AXE).defaultLoot()
					.register();
		}

	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaiHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {


	}

}
