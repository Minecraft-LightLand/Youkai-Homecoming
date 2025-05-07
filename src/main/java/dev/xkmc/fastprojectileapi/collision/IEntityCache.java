package dev.xkmc.fastprojectileapi.collision;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public interface IEntityCache {

	List<Entity> foreach(AABB aabb, Predicate<Entity> filter);

}
