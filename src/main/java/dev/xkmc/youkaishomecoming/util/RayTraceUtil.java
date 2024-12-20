package dev.xkmc.youkaishomecoming.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RayTraceUtil {

	public static BlockHitResult rayTraceBlock(Level worldIn, Player player, double reach) {
		float xRot = player.getXRot();
		float yRot = player.getYRot();
		Vec3 Vector3d = new Vec3(player.getX(), player.getEyeY(), player.getZ());
		Vec3 Vector3d1 = getRayTerm(Vector3d, xRot, yRot, reach);
		return worldIn.clip(new ClipContext(Vector3d, Vector3d1, ClipContext.Block.OUTLINE,
				ClipContext.Fluid.NONE, player));
	}

	public static Vec3 getRayTerm(Vec3 pos, float xRot, float yRot, double reach) {
		float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
		float f5 = Mth.sin(-xRot * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		return pos.add(f6 * reach, f5 * reach, f7 * reach);
	}

}
