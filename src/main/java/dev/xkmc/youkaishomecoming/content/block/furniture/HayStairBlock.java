package dev.xkmc.youkaishomecoming.content.block.furniture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class HayStairBlock extends StairBlock {

	public HayStairBlock(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}

	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float dist) {
		entity.causeFallDamage(dist, 0.2F, level.damageSources().fall());
	}

}
