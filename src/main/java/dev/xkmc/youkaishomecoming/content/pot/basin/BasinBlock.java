package dev.xkmc.youkaishomecoming.content.pot.basin;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.FallOnBlockMethod;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class BasinBlock implements FallOnBlockMethod, ShapeBlockMethod, OnClickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.BASIN_BE, BasinBlockEntity.class);

	private static final VoxelShape SHAPE;

	static {
		var out = Block.box(0, 0, 0, 16, 9, 16);
		var in = Block.box(1, 4, 1, 15, 10, 15);
		SHAPE = Shapes.join(out, in, BooleanOp.ONLY_FIRST);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float v) {
		if (level.getBlockEntity(pos) instanceof BasinBlockEntity be) {
			be.process();
			if (entity instanceof ServerPlayer sp) {
				YHCriteriaTriggers.BASIN.trigger(sp);
			}
		}
		return false;
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof BasinBlockEntity be) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.isEmpty() && player.isShiftKeyDown()) {
				if (!level.isClientSide()) {
					be.dumpInventory();
				}
				return InteractionResult.SUCCESS;
			} else {
				return FluidItemTile.addItem(be, stack, level, pos, player, hand, hit);
			}
		}
		return InteractionResult.PASS;
	}


	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/" + ctx.getName())))
				.texture("top", pvd.modLoc("block/utensil/" + ctx.getName() + "_top"))
				.texture("side", pvd.modLoc("block/utensil/" + ctx.getName() + "_side"))
				.texture("bottom", pvd.modLoc("block/utensil/" + ctx.getName() + "_bottom"))
				.texture("inside", pvd.modLoc("block/utensil/" + ctx.getName() + "_inside"))
		);
	}

}
