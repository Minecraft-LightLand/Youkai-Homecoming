package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class AttachedMover extends TargetPosMover {

	@Override
	public Vec3 pos(MoverInfo info) {
		var e = info.self();
		if (e.self().getOwner() instanceof CardHolder holder) {
			return holder.center();
		}
		return info.prevPos();
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		return new ProjectileMovement(pos(info).subtract(info.prevPos()), info.self().self().rot());
	}
}
