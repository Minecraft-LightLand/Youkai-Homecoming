package dev.xkmc.youkaishomecoming.content.pot.tank;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@SerialClass
public class CopperFaucetBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialClass.SerialField
	protected FluidStack cache = FluidStack.EMPTY;

	public CopperFaucetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean activate() {
		var level = getLevel();
		if (level == null) return false;
		Direction attached = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		var src = level.getBlockEntity(getBlockPos().relative(attached.getOpposite()));
		var dst = level.getBlockEntity(getBlockPos().below());
		if (src == null || dst == null)
			return false;
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (!activateHeat(tank, kettle)) return false;
		}
		var fsrc = src.getCapability(ForgeCapabilities.FLUID_HANDLER, attached);
		var fdst = dst.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP);
		if (fsrc.resolve().isEmpty() || fdst.resolve().isEmpty())
			return false;
		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.OPEN, true));
		return true;
	}

	private boolean activateHeat(CopperTankBlockEntity tank, KettleBlockEntity kettle) {
		return tank.getHeat() >= 50;
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide) return;
		if (getBlockState().getValue(BlockStateProperties.OPEN)) {
			if (!tryTransfer()) {
				cache = FluidStack.EMPTY;
				sync();
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.OPEN, false));
			}
		}
	}

	private boolean tryTransfer() {
		var level = getLevel();
		if (level == null) return false;
		Direction attached = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		var src = level.getBlockEntity(getBlockPos().relative(attached.getOpposite()));
		var dst = level.getBlockEntity(getBlockPos().below());
		if (src == null || dst == null)
			return false;
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (!activateHeat(tank, kettle)) return false;
		}
		var fsrc = src.getCapability(ForgeCapabilities.FLUID_HANDLER, attached);
		var fdst = dst.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP);
		if (fsrc.resolve().isEmpty() || fdst.resolve().isEmpty())
			return false;
		var hsrc = fsrc.resolve().get();
		var hdst = fdst.resolve().get();
		var sdrain = hsrc.drain(50, IFluidHandler.FluidAction.SIMULATE);
		var fill = hdst.fill(sdrain, IFluidHandler.FluidAction.SIMULATE);
		if (fill <= 0)
			return false;
		var drain = hsrc.drain(fill, IFluidHandler.FluidAction.EXECUTE);
		cache = drain.copy();
		sync();
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (kettle.getBlockState().getValue(BlockStateProperties.WATERLOGGED)) return false;
			kettle.setWater(0);
			int amount = hdst.fill(drain, IFluidHandler.FluidAction.EXECUTE);
			int consume = tank.consumeHeat(amount);
			if (consume > 0) {
				kettle.cookingTick(consume, true);
				if (kettle.getWater() > 0) {
					kettle.setWater(0);
					return false;
				}
			}
		} else {
			hdst.fill(drain, IFluidHandler.FluidAction.EXECUTE);
		}
		return true;
	}

}
