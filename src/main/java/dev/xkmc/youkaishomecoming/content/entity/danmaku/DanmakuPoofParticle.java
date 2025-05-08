package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticleBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;

public class DanmakuPoofParticle extends DustParticleBase<DanmakuPoofParticleOptions> {

	protected DanmakuPoofParticle(ClientLevel cl, double x, double y, double z, double dx, double dy, double dz, DanmakuPoofParticleOptions opt, SpriteSet sprite) {
		super(cl, x, y, z, dx, dy, dz, opt, sprite);
		hasPhysics = false;
	}

	public int getLightColor(float pTick) {
		return LightTexture.FULL_BRIGHT;
	}

	public static class Provider implements ParticleProvider<DanmakuPoofParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprite) {
			sprites = sprite;
		}

		public Particle createParticle(DanmakuPoofParticleOptions opt, ClientLevel cl, double x, double y, double z, double dx, double dy, double dz) {
			return new DanmakuPoofParticle(cl, x, y, z, dx, dy, dz, opt, sprites);
		}
	}
}