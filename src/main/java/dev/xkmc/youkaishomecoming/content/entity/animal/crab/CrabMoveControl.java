package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrabMoveControl extends MoveControl {

	public CrabMoveControl(Mob mob) {
		super(mob);
	}

	public void tick() {
		if (operation == MoveControl.Operation.MOVE_TO) {
			operation = MoveControl.Operation.WAIT;
			double dx = wantedX - mob.getX();
			double dz = wantedZ - mob.getZ();
			double dy = wantedY - mob.getY();
			double hr2 = dx * dx + dz * dz;
			double r2 = dx * dx + dy * dy + dz * dz;
			YoukaisHomecoming.LOGGER.info("R2 = "+r2);
			if (r2 < (double) 2.5E-7F) {
				mob.setZza(0);
				mob.setXxa(0);
				return;
			}
			float yrot = (float) (Mth.atan2(dz, dx) * 180 / Math.PI);
			mob.setYRot(rotlerp(mob.getYRot(), yrot, 10f));
			mob.setSpeed((float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
			BlockPos pos = mob.blockPosition();
			BlockState state = mob.level().getBlockState(pos);
			VoxelShape shape = state.getCollisionShape(mob.level(), pos);
			if (dy > mob.getStepHeight() && hr2 < Math.max(1.0F, mob.getBbWidth()) ||
					!shape.isEmpty() && mob.getY() < shape.max(Direction.Axis.Y) + pos.getY() &&
							!state.is(BlockTags.DOORS) && !state.is(BlockTags.FENCES)) {
				mob.getJumpControl().jump();
				operation = MoveControl.Operation.JUMPING;
			}
			return;
		}
		super.tick();
	}

}
