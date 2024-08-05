package dev.xkmc.youkaishomecoming.content.block.furniture;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WoodTableBlock extends Block {

	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(0, 14, 0, 16, 16, 16),
			Block.box(5, 13, 5, 11, 14, 11),
			Block.box(7, 4, 7, 9, 13, 9),
			Block.box(6, 2, 6, 10, 4, 10),
			Block.box(4, 0, 4, 12, 2, 12)
	);

	public WoodTableBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildStates(DataGenContext<Block, WoodTableBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_dining_table")))
				.texture("all", pvd.modLoc("block/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout"));
	}
}
