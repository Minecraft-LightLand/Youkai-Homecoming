package dev.xkmc.youkaishomecoming.content.block.variants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HayVerticalSlabBlock extends VerticalSlabBlock {

	public HayVerticalSlabBlock(Properties pProperties) {
		super(pProperties);
	}

	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float dist) {
		entity.causeFallDamage(dist, 0.2F, level.damageSources().fall());
	}

}
