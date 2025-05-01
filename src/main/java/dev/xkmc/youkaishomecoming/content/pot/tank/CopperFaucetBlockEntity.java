package dev.xkmc.youkaishomecoming.content.pot.tank;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@SerialClass
public class CopperFaucetBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	protected FluidStack cache = FluidStack.EMPTY;

	public CopperFaucetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean activate() {
		var level = getLevel();
		if (level == null) return false;
		Direction attached = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		var psrc = getBlockPos().relative(attached.getOpposite());
		var pdst = getBlockPos().below();
		var src = level.getBlockEntity(psrc);
		var dst = level.getBlockEntity(pdst);
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (!activateHeat(tank, kettle)) return false;
		}
		var fsrc = level.getCapability(Capabilities.FluidHandler.BLOCK, psrc, attached);
		var fdst = level.getCapability(Capabilities.FluidHandler.BLOCK, pdst, Direction.UP);
		if (fsrc == null || fdst == null)
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
		var psrc = getBlockPos().relative(attached.getOpposite());
		var pdst = getBlockPos().below();
		var src = level.getBlockEntity(psrc);
		var dst = level.getBlockEntity(pdst);
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (!activateHeat(tank, kettle)) return false;
		}
		var fsrc = level.getCapability(Capabilities.FluidHandler.BLOCK, psrc, attached);
		var fdst = level.getCapability(Capabilities.FluidHandler.BLOCK, pdst, Direction.UP);
		if (fsrc == null || fdst == null)
			return false;
		var sdrain = fsrc.drain(50, IFluidHandler.FluidAction.SIMULATE);
		var fill = fdst.fill(sdrain, IFluidHandler.FluidAction.SIMULATE);
		if (fill <= 0)
			return false;
		var drain = fsrc.drain(fill, IFluidHandler.FluidAction.EXECUTE);
		cache = drain.copy();
		sync();
		if (src instanceof CopperTankBlockEntity tank && dst instanceof KettleBlockEntity kettle) {
			if (kettle.getBlockState().getValue(BlockStateProperties.WATERLOGGED)) return false;
			kettle.setWater(0);
			int amount = fdst.fill(drain, IFluidHandler.FluidAction.EXECUTE);
			int consume = tank.consumeHeat(amount);
			if (consume > 0) {
				kettle.cookingTick(consume, true);
				if (kettle.getWater() > 0) {
					kettle.setWater(0);
					return false;
				}
			}
		} else {
			fdst.fill(drain, IFluidHandler.FluidAction.EXECUTE);
		}
		return true;
	}

}
