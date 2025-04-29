package dev.xkmc.youkaishomecoming.content.pot.kettle;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KettleBlockEntity extends BasePotBlockEntity implements InfoTile {

	public static final int WATER_BOTTLE = 200, WATER_BUCKET = 600;

	private static final String WATER = "KettleWaterAmount";

	private final WrappedFluidTank tank = new WrappedFluidTank(this);

	private int water;

	public KettleBlockEntity(BlockEntityType<KettleBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
		return new KettleMenu(YHBlocks.KETTLE_MT.get(), id, player, this, this);
	}

	@Override
	public RecipeType<? extends BasePotRecipe> getRecipeType() {
		return YHBlocks.KETTLE_RT.get();
	}

	public int get(int index) {
		if (index == 2) return getWater();
		return super.get(index);
	}

	public void set(int index, int value) {
		if (index == 2) setWater(value);
		super.set(index, value);
	}

	public int getCount() {
		return 3;
	}

	@Override
	protected CompoundTag writeItems(CompoundTag compound) {
		compound.putInt(WATER, getWater());
		return super.writeItems(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		if (compound.contains(WATER)) {
			setWater(compound.getInt(WATER));
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(WATER, getWater());
	}

	public void setWater(int water) {
		if (getBlockState().getValue(BasePotBlock.WATERLOGGED)) this.water = WATER_BUCKET;
		else this.water = Mth.clamp(water, 0, WATER_BUCKET);
	}

	public int getWater() {
		if (getBlockState().getValue(BasePotBlock.WATERLOGGED)) return WATER_BUCKET;
		else return water;
	}

	public void addWater(int change) {
		setWater(getWater() + change);
	}

	@Override
	protected boolean processCooking(int tick, BasePotRecipe recipe) {
		if (level == null) return false;
		if (getWater() < tick) return false;
		addWater(-tick);
		return super.processCooking(tick, recipe);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return LazyOptional.of(() -> tank).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public TileTooltip getImage(boolean shift, BlockHitResult hit) {
		List<ItemStack> stacks = new ArrayList<>();
		var inv = getInventory();
		for (int i = 0; i < inv.getSlots(); i++) {
			stacks.add(inv.getStackInSlot(i));
		}
		return new TileTooltip(stacks, List.of(), 4, 2);
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		return List.of();
	}
}
