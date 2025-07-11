package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.block.MokaKitBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotItem;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotSerializer;
import dev.xkmc.youkaishomecoming.content.pot.moka.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

public class CoffeeBlocks {

	public static final BlockEntry<MokaMakerBlock> MOKA;
	public static final BlockEntityEntry<MokaMakerBlockEntity> MOKA_BE;
	public static final RegistryEntry<RecipeType<MokaRecipe>> MOKA_RT;
	public static final RegistryEntry<RecipeSerializer<MokaRecipe>> MOKA_RS;
	public static final MenuEntry<MokaMenu> MOKA_MT;

	public static final BlockEntry<MokaKitBlock> MOKA_KIT;


	static {

		MOKA = YoukaisHomecoming.REGISTRATE.block("moka_pot", p -> new MokaMakerBlock(
						BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
				.blockstate(MokaMakerBlock::buildModel).item(BasePotItem::new).properties(e -> e.stacksTo(1)).build()
				.loot(BasePotBlock::buildLoot).tag(BlockTags.MINEABLE_WITH_PICKAXE).register();
		MOKA_BE = YoukaisHomecoming.REGISTRATE.blockEntity("moka_pot", MokaMakerBlockEntity::new).validBlock(MOKA).register();
		MOKA_RT = YoukaisHomecoming.REGISTRATE.recipe("moka_pot");
		MOKA_RS = reg("moka_pot", () -> new BasePotSerializer<>(MokaRecipe::new));
		MOKA_MT = YoukaisHomecoming.REGISTRATE.menu("moka_pot", MokaMenu::new, () -> MokaScreen::new).register();

		CoffeeItems.register();

		MOKA_KIT = YoukaisHomecoming.REGISTRATE.block("moka_kit", p -> new MokaKitBlock(
						BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).sound(SoundType.METAL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/moka_kit")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_kit")))
						.texture("maker", pvd.modLoc("block/deco/moka_pot"))
						.texture("cup", pvd.modLoc("block/deco/moka_cup"))
						.texture("foamer", pvd.modLoc("block/deco/moka_foamer"))
						.renderType("cutout")))
				.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();


	}

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
