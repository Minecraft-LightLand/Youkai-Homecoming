package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public interface IYHFluidBottled extends IYHFluidItem {

	Fluid source();

	@Nullable
	default String bottleTextureFolder() {
		return null;
	}

	@Nullable
	default BottleTexture bottleSet() {
		return null;
	}

}
