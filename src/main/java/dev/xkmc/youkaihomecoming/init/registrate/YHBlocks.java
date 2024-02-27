package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaihomecoming.content.block.furniture.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaihomecoming.content.pot.kettle.*;
import dev.xkmc.youkaihomecoming.content.pot.moka.*;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

	static {

		MOKA = YoukaiHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)))
				.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).build()
				.loot(BasePotBlock::buildLoot)
				.register();
		MOKA_BE = YoukaiHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
		MOKA_RT = YoukaiHomecoming.REGISTRATE.recipe("moka_pot");
		MOKA_RS = reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
		MOKA_MT = YoukaiHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

		KETTLE = YoukaiHomecoming.REGISTRATE.block("kettle", p -> new KettleBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)))
				.blockstate(KettleBlock::buildModel).item(BasePotItem::new).build()
				.loot(BasePotBlock::buildLoot).register();
		KETTLE_BE = YoukaiHomecoming.REGISTRATE.blockEntity("kettle", KettleBlockEntity::new).validBlock(KETTLE).register();
		KETTLE_RT = YoukaiHomecoming.REGISTRATE.recipe("kettle");
		KETTLE_RS = reg("kettle", () -> new BasePotSerializer<>(KettleRecipe::new));
		KETTLE_MT = YoukaiHomecoming.REGISTRATE.menu("kettle", KettleMenu::new, () -> KettleScreen::new).register();


		for (var e : WoodType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			e.fence = YoukaiHomecoming.REGISTRATE.block(name + "_handrail",
							p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
					.blockstate(MultiFenceBlock::genModel)
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
					.defaultLoot()
					.register();
		}

	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaiHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {


	}

}
