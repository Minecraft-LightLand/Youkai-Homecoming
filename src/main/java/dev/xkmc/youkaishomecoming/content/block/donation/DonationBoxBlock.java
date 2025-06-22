package dev.xkmc.youkaishomecoming.content.block.donation;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.level.block.Block;

public class DonationBoxBlock {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.DONATION_BOX_BE, DonationBoxBlockEntity.class);

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().cube("block/" + ctx.getName(),
				pvd.modLoc("block/deco/" + ctx.getName() + "_down"),
				pvd.modLoc("block/deco/" + ctx.getName() + "_up"),
				pvd.modLoc("block/deco/" + ctx.getName() + "_end"),
				pvd.modLoc("block/deco/" + ctx.getName() + "_empty"),
				pvd.modLoc("block/deco/" + ctx.getName() + "_side"),
				pvd.modLoc("block/deco/" + ctx.getName() + "_side")
		).texture("particle", pvd.modLoc("block/deco/" + ctx.getName() + "_down")));
	}

}
