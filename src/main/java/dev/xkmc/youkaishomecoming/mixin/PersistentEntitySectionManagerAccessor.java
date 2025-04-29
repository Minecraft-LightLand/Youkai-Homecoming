package dev.xkmc.youkaishomecoming.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;
import java.util.UUID;

@Mixin(PersistentEntitySectionManager.class)
public interface PersistentEntitySectionManagerAccessor<T extends Entity> {

	@Accessor
	EntitySectionStorage<T> getSectionStorage();

	@Accessor
	Set<UUID> getKnownUuids();

	@Invoker
	void callStopTicking(EntityAccess e);

	@Invoker
	void callStopTracking(EntityAccess e);

}
