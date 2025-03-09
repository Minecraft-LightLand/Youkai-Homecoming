package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SteamerBlockEntity extends BaseBlockEntity
		implements TickableBlockEntity, BaseContainerListener, HeatableBlockEntity {

	@SerialClass.SerialField
	public final List<RackData> racks = new ArrayList<>();

	public SteamerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) return;
		boolean shouldUpdate = checkRack();
		if (getBlockState().is(YHBlocks.STEAMER_POT.get())) {
			shouldUpdate |= cook(level);
		}
		if (shouldUpdate) notifyTile();
	}

	@Override
	public void notifyTile() {
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
			if (steamers.size() >= 5) break;
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
		// TODO check cap
		last.getSecond().downwardHeat = last.getSecond().upwardHeat;
		for (int i = allRacks.size() - 1; i > 0; i--) {
			var hi = allRacks.get(i);
			var lo = allRacks.get(i - 1);
			hi.getSecond().upwardHeat = lo.getSecond().upwardHeat;
		}
		var first = allRacks.get(0);
		if (isHeated(level, getBlockPos())) {
			first.getSecond().upwardHeat = 20;
		}
		for (var e : allRacks) {
			e.getSecond().tick(e.getFirst(), level);
		}
		return true;
	}

	public void removeRack(Level level, BlockPos pos, int height) {
		var rack = racks.get(racks.size() - 1);
		rack.popItems(level, pos, height);
		sync();
	}

}
