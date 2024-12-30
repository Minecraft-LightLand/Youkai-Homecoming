package dev.xkmc.youkaishomecoming.content.block.furniture;

import com.mojang.serialization.MapCodec;
import dev.xkmc.youkaishomecoming.content.block.variants.HorizontalLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MokaKitBlock extends HorizontalLoggedBlock {

	public static final VoxelShape SHAPE = box(2.0, 0.0, 2.0, 14.0, 8.5, 14.0);

	public MokaKitBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected MapCodec<? extends MokaKitBlock> codec() {
		return null;//TODO
	}

}
