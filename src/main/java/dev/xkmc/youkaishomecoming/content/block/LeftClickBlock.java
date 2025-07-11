package dev.xkmc.youkaishomecoming.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface LeftClickBlock {
	boolean leftClick(BlockState state, Level level, BlockPos pos, Player player);

}
