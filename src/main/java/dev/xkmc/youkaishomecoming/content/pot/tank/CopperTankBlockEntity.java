package dev.xkmc.youkaishomecoming.content.pot.tank;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

import java.util.List;

@SerialClass
public class CopperTankBlockEntity extends BaseBlockEntity implements TickableBlockEntity, InfoTile, BaseContainerListener, HeatableBlockEntity {

	@SerialClass.SerialField
	private final BaseTank water = new BaseTank(1, 8000)
			.setPredicate(e -> e.getFluid().isSame(Fluids.WATER))
			.add(this);

	@SerialClass.SerialField
	private int heatedWater = 0;

	public CopperTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide()) return;
		if (getBlockState().getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
			if (level.getGameTime() % 5 != 0) return;
			var fluid = level.getFluidState(getBlockPos().above());
			if (fluid.is(FluidTags.WATER) && fluid.isSource()) {
				if (getRoot().water.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE) > 0) {
					notifyTile();
				}
			}
		} else {
			int next;
			if (!isHeated(level, getBlockPos())) {
				next = Math.max(0, heatedWater - 1);
			} else {
				next = heatedWater + 2;
			}
			var ans = Math.min(water.getFluidInTank(0).getAmount(), next);
			if (ans != heatedWater) {
				heatedWater = ans;
				notifyTile();
			}
		}
	}

	public int consumeHeat(int consume) {
		var root = getRoot();
		int ans = Math.min(root.heatedWater, consume);
		root.heatedWater -= ans;
		return ans;
	}

	@Override
	public TileTooltip getImage(boolean shift, BlockHitResult hit) {
		return new TileTooltip(List.of(), List.of(getRoot().water.getFluidInTank(0)), 1, 1);
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		var root = getRoot();
		int heat = root.heatedWater;
		int water = root.water.getFluidInTank(0).getAmount();
		int perc = Mth.clamp( heat * 100 / water, 0, 100);
		return List.of(Component.literal(perc + "%"));
	}

	private CopperTankBlockEntity getRoot() {
		if (getBlockState().getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
			var level = getLevel();
			if (level == null) return this;
			if (level.getBlockEntity(getBlockPos().below()) instanceof CopperTankBlockEntity be)
				return be;
		}
		return this;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			var ans = getRoot().water;
			return LazyOptional.of(() -> ans).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void notifyTile() {
		sync();
		setChanged();
	}

	public int getHeat() {
		return getRoot().heatedWater;
	}

}
