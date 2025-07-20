package dev.xkmc.youkaishomecoming.content.pot.storage.shelf;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.generators.ModelFile;

public class WineShelfBlock implements OnClickBlockMethod {

	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.WINE_SHELF_BE, WineShelfBlockEntity.class);

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var dir = state.getValue(BlockProxy.HORIZONTAL_FACING);
		if (hit.getDirection() == dir) {
			if (level.getBlockEntity(pos) instanceof WineShelfBlockEntity be) {
				var vec = hit.getLocation().subtract(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
						.yRot(-(float) ((180 - dir.toYRot()) / 180 * Math.PI));
				int x = Mth.clamp((int) ((-vec.x + 0.5) * 3), 0, 2);
				int y = Mth.clamp((int) ((-vec.y + 0.5) * 3), 0, 2);
				if (be.click(player, hand, x + y * 3)) {
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	public static void buildModels(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/wine_shelf")))
				.texture("top", pvd.modLoc("block/shelf/" + ctx.getName() + "_top"))
				.texture("side", pvd.modLoc("block/shelf/" + ctx.getName() + "_side"))
				.texture("inside", pvd.modLoc("block/shelf/" + ctx.getName() + "_inside"))
				.renderType("cutout"));
	}

}
