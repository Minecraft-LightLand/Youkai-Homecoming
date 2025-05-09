package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DanmakuParticleHelper {

	public static void ball(Level level, Vec3 pos, int col, double radius, RandomSource rand) {
		int count = (int) (100 * radius * radius);
		for (int i = 0; i < count; i++) {
			var dir = new Vec3(
					rand.nextGaussian(),
					rand.nextGaussian(),
					rand.nextGaussian()
			).normalize();
			var vec = dir.scale(radius).add(pos);
			var vel = dir.scale(0.3 * radius);
			level.addAlwaysVisibleParticle(
					new DanmakuPoofParticleOptions(Vec3.fromRGB24(col).toVector3f(), 1),
					vec.x, vec.y, vec.z, vel.x, vel.y, vel.z);
		}
	}

	public static void line(Level level, Vec3 pos, Vec3 forward, int col, double len, double radius, RandomSource rand) {
		int count = (int) (5 * len);
		for (int i = 0; i < count; i++) {
			var vec = pos.add(forward.scale(len * i / count));
			level.addParticle(
					new DanmakuPoofParticleOptions(Vec3.fromRGB24(col).toVector3f(), 1),
					vec.x, vec.y, vec.z, 0, 0, 0);
		}
	}

}
