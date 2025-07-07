package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;

public class BottleBlock extends Block {

	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 4);
	private static final VoxelShape SHAPE = box(6, 0, 6, 10, 14, 10);

	public BottleBlock(Properties prop) {
		super(prop);
		registerDefaultState(defaultBlockState().setValue(COUNT, 1));
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COUNT);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;//TODO
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, BottleBlock block, YHDrink drink) {
		pvd.dropSelf(block);
		//TODO
	}

	public static void buildModel(DataGenContext<Block, BottleBlock> ctx, RegistrateBlockstateProvider pvd, YHDrink drink) {
		String folder = drink.folder;
		//TODO
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bottle/" + folder)))
				.texture("bottle", pvd.modLoc("block/bottle/" + folder + "/" + ctx.getName()))
				.renderType("cutout")
		);
	}


}
