package dev.xkmc.youkaishomecoming.content.spell.mover;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.phys.Vec3;

public interface MoverOwner {

	Entity self();

	Vec3 rot();

	TraceableEntity asTraceable();

}
