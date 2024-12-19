package dev.xkmc.youkaishomecoming.content.block.variants;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.youkaishomecoming.init.data.YHRecipeGen;
import dev.xkmc.youkaishomecoming.util.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

import java.util.function.Supplier;

public class VerticalSlabBlock extends HorizontalLoggedBlock {

	private static final VoxelShape[] SHAPES;

	static {
		SHAPES = new VoxelShape[4];
		var box = new VoxelBuilder(0, 0, 0, 16, 16, 8);
		for (int i = 0; i < 4; i++) {
			Direction dir = Direction.from2DDataValue(i);
			SHAPES[i] = box.rotateFromNorth(dir);
		}
	}

	public VerticalSlabBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPES[pState.getValue(FACING).get2DDataValue()];
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = defaultBlockState();
		FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
		var face = ctx.getClickedFace();
		Direction dir;
		if (face.getAxis().isVertical()) {
			var loc = ctx.getClickLocation();
			dir = Direction.getNearest(Mth.positiveModulo(loc.x, 1) - 0.5, 0, Mth.positiveModulo(loc.z, 1) - 0.5);
		} else {
			if (ctx.replacingClickedOnBlock())
				dir = face;
			else dir = face.getOpposite();
		}
		state = state.setValue(FACING, dir);
		return state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return null;//TODO
	}

	private static void cube(ModelBuilder<?> builder) {
		var elem = builder.element();
		elem.from(0, 0, 0).to(16, 16, 8);
		elem.face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#side").end();
		elem.face(Direction.SOUTH).uvs(16, 0, 0, 16).texture("#side").end();
		elem.face(Direction.UP).uvs(16, 0, 0, 8).texture("#top").end();
		elem.face(Direction.DOWN).uvs(16, 8, 0, 16).texture("#top").end();
		elem.face(Direction.WEST).uvs(8, 0, 16, 16).texture("#side").end();
		elem.face(Direction.EAST).uvs(0, 0, 8, 16).texture("#side").end();
	}

	public static BlockModelBuilder buildModel(DataGenContext<Block, ? extends VerticalSlabBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.models().withExistingParent(ctx.getName(), "block/block");
		cube(builder);
		builder.texture("particle", "#top");
		return builder;
	}

	public static void buildBlockState(DataGenContext<Block, ? extends VerticalSlabBlock> ctx, RegistrateBlockstateProvider pvd,
									   ResourceLocation top, ResourceLocation side) {
		var model = buildModel(ctx, pvd).texture("top", top).texture("side", side);
		pvd.getVariantBuilder(ctx.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).uvLock(true)
				.rotationY(((int) (state.getValue(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build());
	}

	public static void genRecipe(RegistrateRecipeProvider pvd, Supplier<? extends ItemLike> base, Supplier<? extends VerticalSlabBlock> vertical) {
		YHRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, vertical.get(), 6)::unlockedBy, base.get().asItem())
				.pattern("X").pattern("X").pattern("X")
				.define('X', base.get())
				.save(pvd);
		pvd.stonecutting(DataIngredient.items(base.get()), RecipeCategory.BUILDING_BLOCKS, vertical, 2);
	}

}
