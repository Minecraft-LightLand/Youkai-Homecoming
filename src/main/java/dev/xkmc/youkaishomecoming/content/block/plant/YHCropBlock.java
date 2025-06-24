package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

import java.util.function.Supplier;

public class YHCropBlock extends CropBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(2, 0, 2, 14, 5, 14),
			Block.box(2, 0, 2, 14, 7, 14),
			Block.box(2, 0, 2, 14, 9, 14),
			Block.box(2, 0, 2, 14, 11, 14),
			Block.box(2, 0, 2, 14, 13, 14),
			Block.box(2, 0, 2, 14, 14, 14),
			Block.box(2, 0, 2, 14, 14, 14),
			Block.box(2, 0, 2, 14, 14, 14)};

	private final Supplier<Item> seed;

	public YHCropBlock(Properties prop, Supplier<Item> seed) {
		super(prop);
		this.seed = seed;
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return seed.get() == YHCrops.REDBEAN.getSeed() ? pState.is(YHTagGen.FARMLAND_REDBEAN) : super.mayPlaceOn(pState, pLevel, pPos);
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return seed.get() == YHCrops.REDBEAN.getSeed() ? PlantType.get("redbeans") : PlantType.CROP;
	}

	protected ItemLike getBaseSeedId() {
		return seed.get();
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_AGE[this.getAge(pState)];
	}

	public static void buildWildModel(DataGenContext<Block, ? extends BushBlock> ctx, RegistrateBlockstateProvider pvd, YHCrops crop) {
		String tex = "wild_" + crop.getName();
		pvd.simpleBlock(ctx.get(), pvd.models().cross(tex, pvd.modLoc("block/plants/" + crop.getName() + "/" + tex)).renderType("cutout"));
	}

}