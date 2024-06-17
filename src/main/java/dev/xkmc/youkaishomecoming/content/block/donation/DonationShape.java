package dev.xkmc.youkaishomecoming.content.block.donation;

import dev.xkmc.l2modularblock.one.EntityInsideBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DonationShape implements ShapeBlockMethod, EntityInsideBlockMethod {

	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 15, 16);

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity item) {
			if (level.getBlockEntity(pos) instanceof DonationBoxBlockEntity be) {
				Player player = item.getOwner() instanceof Player pl ? pl : null;
				be.take(player, item.getItem());
				if (item.getItem().isEmpty())
					item.discard();
			}
		}
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

}
