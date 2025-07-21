package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipeBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

import java.util.List;

import static net.minecraft.world.level.block.Block.popResource;

@SerialClass
public class KettleBlockEntity extends TimedRecipeBlockEntity<KettleRecipe, SimpleContainer>
		implements InfoTile, HeatableBlockEntity, LeftClickBlock, FluidItemTile, BlockContainer {

	@SerialClass.SerialField
	private final KettleContainer items = new KettleContainer(4).setMax(1).add(this);

	@SerialClass.SerialField
	public final BaseTank fluids = new BaseTank(1, 1000).add(this);

	@SerialClass.SerialField
	private int heat = 0;

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(items));
	private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluids);

	public KettleBlockEntity(BlockEntityType<KettleBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public RecipeType<KettleRecipe> getRecipeType() {
		return YHBlocks.KETTLE_RT.get();
	}

	@Override
	protected boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public BaseTank getFluidHandler() {
		return fluids;
	}

	@Override
	public SimpleContainer getItemHandler() {
		return items;
	}

	@Override
	protected boolean shouldStopProcessing(Level level) {
		if (heat < 1000) return true;
		var stack = fluids.getFluidInTank(0);
		if (stack.getAmount() < 1000) return true;
		return !stack.getFluid().is(FluidTags.WATER);
	}

	@Override
	protected SimpleContainer createContainer() {
		return items;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	@Override
	protected void finishRecipe(Level level, KettleRecipe recipe) {
		items.clear();
		fluids.set(1, 0, recipe.result.copy());
		heat = 0;
	}

	@Override
	public void tick() {
		if (level == null) return;
		var fluid = fluids.getFluidInTank(0);
		if (fluid.getFluid().is(FluidTags.WATER)) {
			if (heat >= fluid.getAmount())
				heat = fluid.getAmount();
			else {
				if (isHeated(level, getBlockPos()))
					heat++;
			}
		} else heat = 0;
		super.tick();
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), items);
		notifyTile();
	}

	public void readFromStack(ItemStack stack) {
		try {
			var root = stack.getTag();
			if (root == null) return;
			if (root.contains("KettleContents", Tag.TAG_LIST)) {
				var list = root.getList("KettleContents", Tag.TAG_COMPOUND);
				for (var e : list) {
					if (e instanceof CompoundTag c)
						items.addItem(ItemStack.of(c));
				}
			}
			if (root.contains("KettleFluid", Tag.TAG_COMPOUND)) {
				fluids.set(1, 0, FluidStack.loadFluidStackFromNBT(root.getCompound("KettleFluid")));
			}
			heat = root.getInt("KettleHeat");
		} catch (Exception ignored) {

		}
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		if (level.isClientSide) return true;
		ItemStack stack = state.getBlock().asItem().getDefaultInstance();
		ListTag list = new ListTag();
		for (var e : items.getAsList()) {
			list.add(e.save(new CompoundTag()));
		}
		items.clear();
		stack.addTagElement("KettleContents", list);
		stack.addTagElement("KettleFluid", fluids.getFluidInTank(0).writeToNBT(new CompoundTag()));
		stack.addTagElement("KettleHeat", IntTag.valueOf(heat));
		level.removeBlock(pos, false);
		if (player.getMainHandItem().isEmpty()) {
			player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		} else {
			popResource(level, pos, stack);
		}
		return true;
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return itemHandler.cast();
		}
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluidHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public TileTooltip getImage(boolean shift, BlockHitResult hit) {
		return new TileTooltip(items.getAsList(), fluids.getAsList(), 3, 2);
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) return List.of();
		var prog = inProgress();
		if (prog > 0) return List.of(YHLangData.BREWING_PROGRESS.get(Math.round(prog * 100) + "%"));
		return List.of(YHLangData.HEAT_PROGRESS.get(heat / 10 + "%"));
	}

	public void heatUp(int val) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) {
			heat = 0;
			return;
		}
		heat = Math.min(fluid.getAmount(), heat + val);
	}

	public void prepareforHotWater(int space) {
		var fluid = fluids.getFluidInTank(0);
		if (!fluid.getFluid().is(FluidTags.WATER)) return;
		if (heat < fluid.getAmount() && fluid.getAmount() + space > 1000) {
			fluid.setAmount(Math.max(heat, 1000 - space));
		}
	}

}
