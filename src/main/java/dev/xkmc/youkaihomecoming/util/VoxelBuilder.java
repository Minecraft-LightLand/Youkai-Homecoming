package dev.xkmc.youkaihomecoming.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelBuilder {

	private final Vec3 lo;
	private final Vec3 hi;

	public VoxelBuilder(int x0, int y0, int z0, int x1, int y1, int z1) {
		lo = new Vec3(x0, y0, z0).subtract(8, 8, 8);
		hi = new Vec3(x1, y1, z1).subtract(8, 8, 8);
	}

	public VoxelShape rotateFromNorth(Direction dir) {
		float angle = (float) ((180 - dir.toYRot()) / 180 * Math.PI);
		var xlo = lo.yRot(angle).add(8, 8, 8);
		var xhi = hi.yRot(angle).add(8, 8, 8);
		var bound = new AABB(xlo, xhi);
		return Block.box(bound.minX, bound.minY, bound.minZ, bound.maxX, bound.maxY, bound.maxZ);
	}

}
