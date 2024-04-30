package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
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
public class FermentationTankBlockEntity extends BaseBlockEntity
		implements BlockContainer, BaseContainerListener, TickableBlockEntity, InfoTile {

	@SerialClass.SerialField
	public final FermentationItemContainer items = new FermentationItemContainer().setMax(1).add(this);

	@SerialClass.SerialField
	private final BaseTank fluids = new BaseTank(1, 1000).add(this);

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(items));
	private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluids);

	public FermentationTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		//TODO
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
	public TileTooltip getTooltip() {
		return new TileTooltip(items.getAsList(), fluids.getAsList());
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
