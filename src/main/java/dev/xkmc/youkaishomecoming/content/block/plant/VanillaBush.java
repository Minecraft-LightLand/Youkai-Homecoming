package dev.xkmc.youkaishomecoming.content.block.plant;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

public class VanillaBush extends BushBlock {

	public VanillaBush(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return null;//TODO codec
	}

}
