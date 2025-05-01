package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class YHFluid extends BaseFlowingFluid {
	public final IYHFluidHolder type;

	public YHFluid(Properties properties, IYHFluidHolder type) {
		super(properties);
		this.type = type;
	}

	public Fluid getSource() {
		return super.getSource();
	}

	public Fluid getFlowing() {
		return this;
	}

	public Item getBucket() {
		return Items.AIR;
	}

	protected BlockState createLegacyBlock(FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}

	public boolean isSource(FluidState state) {
		return false;
	}

	public int getAmount(FluidState state) {
		return 0;
	}
}
