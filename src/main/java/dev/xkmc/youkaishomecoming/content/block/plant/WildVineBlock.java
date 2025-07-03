package dev.xkmc.youkaishomecoming.content.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WildVineBlock extends Block {

	public WildVineBlock(Properties prop) {
		super(prop);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.above()).is(BlockTags.LEAVES);
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState fstate, LevelAccessor level, BlockPos cpos, BlockPos fpos) {
		if (!state.canSurvive(level, cpos)) {
			return Blocks.AIR.defaultBlockState();
		}
		return state;
	}

}
