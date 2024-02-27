package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaihomecoming.content.block.KettleBlock;
import dev.xkmc.youkaihomecoming.content.block.MokaMakerBlock;
import dev.xkmc.youkaihomecoming.content.block.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

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
	public static final BlockEntry<KettleBlock> KETTLE;

	static {
		MOKA = YoukaiHomecoming.REGISTRATE.block("moka_maker", p -> new MokaMakerBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)))
				.blockstate(MokaMakerBlock::buildModel)
				.simpleItem()
				.register();

		KETTLE = YoukaiHomecoming.REGISTRATE.block("kettle", p -> new KettleBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)))
				.blockstate(KettleBlock::buildModel)
				.simpleItem()
				.register();

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

	public static void register() {


	}

}
