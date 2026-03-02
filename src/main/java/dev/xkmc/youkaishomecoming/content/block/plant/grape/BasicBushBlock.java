package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

public class BasicBushBlock extends BushBlock {

	protected BasicBushBlock(Properties prop) {
		super(prop);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return null;
	}
}
