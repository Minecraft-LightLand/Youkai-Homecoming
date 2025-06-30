package dev.xkmc.youkaishomecoming.content.pot.basin;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class BasinBlockEntity extends BaseBlockEntity implements
		BlockContainer, BaseContainerListener, FluidItemTile {

	@SerialClass.SerialField
	public final BasinItemContainer items = new BasinItemContainer().add(this);
	@SerialClass.SerialField
	public final BaseTank fluids = new BaseTank(1, 500).add(this);

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(items));
	private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluids);

	public BasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public BaseTank getFluidHandler() {
		return fluids;
	}

	@Override
	public SimpleContainer getItemHandler() {
		return items;
	}

	public void process() {
		if (level == null) return;
		if (level.isClientSide()) return;
		var cont = new BasinInput(this);
		var rec = level.getRecipeManager().getRecipeFor(YHBlocks.BASIN_RT.get(), cont, level);
		if (rec.isEmpty()) return;
		var ans = rec.get().assembleFluid(cont, level.registryAccess());
		var old = fluids.getFluidInTank(0);
		if (!old.isEmpty()) {
			if (!ans.isFluidEqual(old)) return;
			if (old.getAmount() + ans.getAmount() > fluids.getTankCapacity(0)) return;
		}
		items.getItem(0).shrink(1);
		fluids.fill(ans, IFluidHandler.FluidAction.EXECUTE);
		notifyTile();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), items);
		notifyTile();
	}

	public void notifyTile() {
		setChanged();
		sync();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return itemHandler.cast();
		}
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluidHandler.cast();
		}
		return super.getCapability(cap, side);
	}

}
