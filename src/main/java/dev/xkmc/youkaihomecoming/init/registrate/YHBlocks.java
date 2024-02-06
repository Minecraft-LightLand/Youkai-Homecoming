package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaihomecoming.content.block.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class YHBlocks {

	public static final BlockEntry<MultiFenceBlock> FENCE;

	static {
		FENCE = YoukaiHomecoming.REGISTRATE.block("spruce_handrail",
						p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
				.blockstate(MultiFenceBlock::genModel)
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.defaultLoot()//TODO
				.register();
	}

	public static void register() {


	}

}
