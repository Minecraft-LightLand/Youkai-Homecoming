package dev.xkmc.youkaishomecoming.content.pot.basin;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.FallOnBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class BasinBlock implements FallOnBlockMethod, ShapeBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.FERMENT_BE, FermentationTankBlockEntity.class);

	private static final VoxelShape SHAPE;

	static {
		var out = Block.box(0, 0, 0, 16, 9, 16);
		var in = Block.box(1, 1, 1, 15, 9, 15);
		SHAPE = Shapes.join(out, in, BooleanOp.FIRST);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float v) {
		if (level.getBlockEntity(pos) instanceof BasinBlockEntity be) {
			be.process();
		}
		return false;
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + ctx.getName())))
				.texture("top", pvd.modLoc("block/utensil/" + ctx.getName() + "_top"))
				.texture("side", pvd.modLoc("block/utensil/" + ctx.getName() + "_side"))
				.texture("bottom", pvd.modLoc("block/utensil/" + ctx.getName() + "_bottom"))
				.texture("inside", pvd.modLoc("block/utensil/" + ctx.getName() + "_inside"))
		);
	}

}
