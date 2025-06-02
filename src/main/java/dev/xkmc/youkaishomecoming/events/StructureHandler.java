package dev.xkmc.youkaishomecoming.events;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class StructureHandler {

	private static final float[] KERNEL = new float[11 * 11 * 3 + 12];
	private static final float OFFSET = (float) Math.exp(-(12 * 12 + 0.25) / 64d);

	private static float get(int x, int y, int z) {
		int r = x * x + y * y + z * z + y;
		if (KERNEL[r] == 0) {
			KERNEL[r] = (float) Math.max(0, Math.exp(-(r + 0.25) / 64d) - OFFSET) + 1e-3f;
		}
		return KERNEL[r];
	}


	public static double getCustomBeard(int x, int y, int z, int h) {
		if (x >= 0 && x < 12 && y >= 0 && y < 12 && z >= 0 && z < 12) {
			double d0 = (double) h + 0.5D;
			double d1 = Mth.lengthSquared(x, d0, z);
			double d2 = -d0 * Mth.fastInvSqrt(d1) * 4;
			return d2 * get(x, y, z);
		}
		return 0;
	}

	public interface BoxTagger {

		void youkaihomecoming$setTag(String str);

		@Nullable
		String youkaihomecoming$getTag();

	}

}
