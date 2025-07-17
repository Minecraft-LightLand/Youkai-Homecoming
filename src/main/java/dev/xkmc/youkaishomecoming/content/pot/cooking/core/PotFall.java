package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2modularblock.mult.FallOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record PotFall() implements FallOnBlockMethod {

	@Override
	public boolean fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float v) {
		if (entity instanceof ItemEntity item && level.getBlockEntity(pos) instanceof CookingBlockEntity be) {
			while (be.tryAddItem(item.getItem(), level.isClientSide())) {
				if (level.isClientSide()) return true;
				item.getItem().shrink(1);
				if (item.getItem().isEmpty()) {
					item.discard();
					return true;
				}
			}
			return true;
		}
		return false;
	}
}
