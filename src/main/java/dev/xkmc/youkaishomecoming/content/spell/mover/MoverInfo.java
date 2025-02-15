package dev.xkmc.youkaishomecoming.content.spell.mover;

import net.minecraft.world.phys.Vec3;

public record MoverInfo(int tick, Vec3 prevPos, Vec3 prevVel, MoverOwner self) {

	public MoverInfo offsetTime(int i) {
		return new MoverInfo(tick + i, prevPos, prevVel, self);
	}

}
