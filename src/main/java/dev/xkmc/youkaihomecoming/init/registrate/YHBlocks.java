package dev.xkmc.youkaihomecoming.init.registrate;

import dev.xkmc.youkaihomecoming.content.block.MultiFenceBlock;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;

public class YHBlocks {

	private enum WoodType {
		ACACIA, BAMBOO, BIRCH, CHERRY, CRIMSON, DARK_OAK, JUNGLE, MANGROVE, OAK, WARPED,
	}

	static {
		for (var e : WoodType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			YoukaiHomecoming.REGISTRATE.block(name + "_handrail",
							p -> new MultiFenceBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_FENCE).noOcclusion()))
					.blockstate(MultiFenceBlock::genModel)
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/handrail/" + ctx.getName()))).build()
					.defaultLoot()//TODO
					.register();
		}

	}

	public static void register() {


	}

}
