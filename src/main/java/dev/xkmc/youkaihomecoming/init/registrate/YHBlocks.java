package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaihomecoming.content.block.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

public class YHBlocks {

	public static final BlockEntry<MultiFenceBlock> FENCE;

	static {
		FENCE = YoukaiHomecoming.REGISTRATE.block("wooden_fence",
						p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
				.blockstate(MultiFenceBlock::genModel)
				.item().model((ctx, pvd) -> pvd.getBuilder("item/" + ctx.getName())
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/fence_base")))
						.texture("fence", pvd.modLoc("block/" + ctx.getName()))).build()
				.defaultLoot()//TODO
				.register();
	}

	public static void register() {


	}

}
