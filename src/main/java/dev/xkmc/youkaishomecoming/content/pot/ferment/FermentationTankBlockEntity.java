package dev.xkmc.youkaishomecoming.content.pot.ferment;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class FermentationTankBlockEntity extends BaseBlockEntity
		implements BlockContainer, BaseContainerListener, TickableBlockEntity, InfoTile {

	@SerialField
	public final FermentationItemContainer items = new FermentationItemContainer().setMax(1).add(this);

	@SerialField
	public final BaseTank fluids = new BaseTank(1, 1000).add(this);

	private final IItemHandler itemHandler = new InvWrapper(items);

	@SerialField
	private int totalTime = 0, fermentationProgress = 0;
	@SerialField
	private ResourceLocation recipeId = null;

	private boolean doRecipeSearch = true;
	private RecipeHolder<FermentationRecipe<?>> recipe = null;

	public FermentationTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			if (totalTime > 0) {
				if (getBlockState().getValue(FermentationTankBlock.OPEN)) {
					if (fermentationProgress > 0) {
						fermentationProgress--;
					}
				} else {
					fermentationProgress++;
				}
			}
			return;
		}
		if (doRecipeSearch) {
			// find recipes
			if (!items.isEmpty() || !fluids.isEmpty()) {
				var cont = new FermentationDummyContainer(items, fluids);
				var opt = level.getRecipeManager().getRecipeFor(YHBlocks.FERMENT_RT.get(), cont, level);
				if (opt.isPresent()) {
					recipe = opt.get();
					totalTime = recipe.value().getFermentationTime();
					if (!recipe.id().equals(recipeId)) {
						fermentationProgress = 0;
						recipeId = recipe.id();
					} else if (fermentationProgress > totalTime) {
						fermentationProgress = totalTime - 1;
					}
				} else {
					recipeId = null;
					recipe = null;
					totalTime = 0;
					fermentationProgress = 0;
				}
				sync();
			}
			doRecipeSearch = false;
		}
		if (totalTime > 0) {
			if (getBlockState().getValue(FermentationTankBlock.OPEN)) {
				if (fermentationProgress > 0) {
					fermentationProgress--;
				}
			} else {
				fermentationProgress++;
			}
			if (fermentationProgress >= totalTime) {
				if (recipe != null) {
					recipe.value().assemble(new FermentationDummyContainer(items, fluids), level.registryAccess());
					level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(FermentationTankBlock.OPEN, true));
					notifyTile();
				}
				fermentationProgress = 0;
				totalTime = 0;
				recipeId = null;
				recipe = null;
			}
		}
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
		doRecipeSearch = true;
	}

	@Override
	public TileTooltip getImage() {
		return new TileTooltip(items.getAsList(), fluids.getAsList());
	}

	public float inProgress() {
		return totalTime == 0 ? 0 : Mth.clamp(1f * fermentationProgress / totalTime, 0, 1);
	}

	@Override
	public List<Component> lines() {
		float progress = inProgress();
		if (totalTime <= 0) {
			return List.of();
		} else {
			return List.of(YHLangData.FERMENT_PROGRESS.get(Math.round(progress * 100) + "%"));
		}
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
