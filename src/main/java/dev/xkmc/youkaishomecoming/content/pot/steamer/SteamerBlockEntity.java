package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SteamerBlockEntity extends BaseBlockEntity
		implements BlockContainer, TickableBlockEntity, BaseContainerListener, HeatableBlockEntity, InfoTile {

	private static final int MAX_STACK = 5;

	@SerialClass.SerialField
	public final List<RackData> racks = new ArrayList<>();

	public SteamerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			animationTick(level);
			return;
		}
		boolean shouldUpdate = checkRack();
		if (getBlockState().is(YHBlocks.STEAMER_POT.get())) {
			shouldUpdate |= cook(level);
		}
		if (shouldUpdate) notifyTile();
	}

	@Override
	public void notifyTile() {
		selfHandler = null;
		sync();
		setChanged();
	}

	private boolean checkRack() {
		RackInfo info = RackInfo.getRackInfo(getBlockState());
		if (racks.size() == info.racks()) return false;
		while (racks.size() > info.racks()) {
			racks.remove(racks.size() - 1);
		}
		while (racks.size() < info.racks()) {
			racks.add(new RackData());
		}
		return true;
	}

	private boolean cook(Level level) {
		List<SteamerBlockEntity> steamers = new ArrayList<>();
		steamers.add(this);
		BlockPos pos = getBlockPos().above();
		while (level.getBlockEntity(pos) instanceof SteamerBlockEntity be && be.getBlockState().is(YHBlocks.STEAMER_RACK.get())) {
			steamers.add(be);
			pos = pos.above();
			if (level.isOutsideBuildHeight(pos)) break;
			if (steamers.size() >= MAX_STACK) break;
		}
		List<Pair<SteamerBlockEntity, RackData>> allRacks = new ArrayList<>();
		for (var e : steamers) {
			for (var r : e.racks) {
				allRacks.add(Pair.of(e, r));
			}
		}
		if (allRacks.isEmpty()) return false;
		for (int i = 0; i < allRacks.size() - 1; i++) {
			var lo = allRacks.get(i);
			var hi = allRacks.get(i + 1);
			lo.getSecond().downwardHeat = hi.getSecond().downwardHeat;
		}
		var last = allRacks.get(allRacks.size() - 1);
		if (RackInfo.isCapped(level, last.getFirst().getBlockPos()))
			last.getSecond().downwardHeat = last.getSecond().upwardHeat;
		for (int i = allRacks.size() - 1; i > 0; i--) {
			var hi = allRacks.get(i);
			var lo = allRacks.get(i - 1);
			hi.getSecond().upwardHeat = lo.getSecond().upwardHeat;
		}
		var first = allRacks.get(0);
		if (getBlockState().getValue(SteamerStates.WATER) && isHeated(level, getBlockPos())) {
			first.getSecond().upwardHeat = 20;
		}
		for (var e : allRacks) {
			e.getSecond().tick(e.getFirst(), level);
		}
		return true;
	}

	private void animationTick(Level level) {
		BlockPos pos = getBlockPos();
		BlockState state = getBlockState();
		boolean pot = state.is(YHBlocks.STEAMER_POT.get()) && state.getValue(SteamerStates.WATER) && isHeated(level, pos);
		boolean steam = pot;
		for (var r : racks) {
			if (r.upwardHeat > 0) {
				steam = true;
				break;
			}
		}
		double x, y, z;
		RandomSource random = level.random;
		if (pot && random.nextFloat() < 0.5F || steam && random.nextFloat() < 0.2F) {
			x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
			z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
			double motionY = random.nextBoolean() ? 0.015 : 0.005;
			level.addParticle(ModParticleTypes.STEAM.get(), x, pos.getY() + 0.5, z, 0.0, motionY, 0.0);

		}
		if (!pot) return;
		if (random.nextFloat() < 0.2F) {
			x = (double) pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
			y = (double) pos.getY() + 0.7;
			z = (double) pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
			level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
		}
	}

	public void removeRack(Level level, BlockPos pos, int height) {
		selfHandler = null;
		var rack = racks.remove(racks.size() - 1);
		rack.popItems(level, pos, height);
		sync();
	}

	private IItemHandlerModifiable selfHandler;

	private IItemHandlerModifiable getSelfHandler() {
		if (level == null) return new EmptyHandler();
		if (selfHandler != null) return selfHandler;
		SteamerItemHandler[] handlers = new SteamerItemHandler[racks.size()];
		for (int i = 0; i < racks.size(); i++)
			handlers[i] = new SteamerItemHandler(this, level, racks.get(i));
		selfHandler = new CombinedInvWrapper(handlers);
		return selfHandler;
	}

	private IItemHandlerModifiable getCombinedHandler() {
		if (level == null) return new EmptyHandler();
		if (getBlockState().is(YHBlocks.STEAMER_POT.get())) {
			List<IItemHandlerModifiable> steamers = new ArrayList<>();
			steamers.add(getSelfHandler());
			BlockPos pos = getBlockPos().above();
			while (level.getBlockEntity(pos) instanceof SteamerBlockEntity be &&
					be.getBlockState().is(YHBlocks.STEAMER_RACK.get())) {
				steamers.add(be.getSelfHandler());
				pos = pos.above();
				if (level.isOutsideBuildHeight(pos)) break;
				if (steamers.size() >= MAX_STACK) break;
			}
			return new CombinedInvWrapper(steamers.toArray(IItemHandlerModifiable[]::new));
		}
		var pos = getBlockPos();
		for (int y = 1; y < MAX_STACK; y++) {
			var npos = pos.below(y);
			if (level.isOutsideBuildHeight(npos)) break;
			if (!(level.getBlockEntity(npos) instanceof SteamerBlockEntity be)) break;
			if (be.getBlockState().is(YHBlocks.STEAMER_POT.get())) {
				return be.getCombinedHandler();
			}
		}
		return getSelfHandler();
	}

	@Override
	public List<Container> getContainers() {
		List<ItemStack> stacks = new ArrayList<>();
		for (var r : racks) {
			for (var e : r.list) {
				if (e != null && !e.stack.isEmpty()) {
					stacks.add(e.stack);
				}
			}
		}
		return List.of(new SimpleContainer(stacks.toArray(ItemStack[]::new)));
	}

	@Override
	public TileTooltip getImage(boolean shift, BlockHitResult hit) {
		if (shift) {
			int h = racks.size();
			List<ItemStack> list = new ArrayList<>();
			for (var r : racks) {
				for (var e : r.list) {
					if (e == null) list.add(ItemStack.EMPTY);
					else list.add(e.stack);
				}
			}
			return new TileTooltip(list, List.of(), 4, h);
		}
		int h = RackInfo.ofY(hit);
		if (getBlockState().is(YHBlocks.STEAMER_POT.get())) h -= 2;
		List<ItemStack> list = new ArrayList<>();
		if (h >= 0 && h < racks.size()) {
			var r = racks.get(h);
			for (var e : r.list) {
				if (e != null && !e.stack.isEmpty()) {
					list.add(e.stack);
				}
			}
		}
		return new TileTooltip(list, List.of(), 2, 2);
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		return List.of();
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return LazyOptional.of(this::getCombinedHandler).cast();
		}
		return super.getCapability(cap, side);
	}

}
