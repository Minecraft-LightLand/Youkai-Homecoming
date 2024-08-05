package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationBoxBlock;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationBoxBlockEntity;
import dev.xkmc.youkaishomecoming.content.block.donation.DonationShape;
import dev.xkmc.youkaishomecoming.content.block.donation.DoubleBlockHorizontal;
import dev.xkmc.youkaishomecoming.content.block.furniture.MokaKitBlock;
import dev.xkmc.youkaishomecoming.content.block.furniture.MoonLanternBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.client.model.generators.ModelFile;

public class YHBlocks {

	public static final BlockEntry<DelegateBlock> DONATION_BOX;
	public static final BlockEntityEntry<DonationBoxBlockEntity> DONATION_BOX_BE;

	static {

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
							.texture("maker", pvd.modLoc("block/moka_pot"))
							.texture("cup", pvd.modLoc("block/moka_cup"))
							.texture("foamer", pvd.modLoc("block/moka_foamer"))
							.renderType("cutout")))
					.simpleItem().tag(BlockTags.MINEABLE_WITH_PICKAXE).register();

			MOON_LANTERN = YoukaisHomecoming.REGISTRATE.block("moon_lantern", p -> new MoonLanternBlock(
							BlockBehaviour.Properties.copy(Blocks.LANTERN)))
					.blockstate(MoonLanternBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE).register();

		}

	}

	public static void register() {

	}


}
