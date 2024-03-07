package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.LinkedHashSet;

@SerialClass
public class FrogGodCapability extends GeneralCapabilityTemplate<Frog, FrogGodCapability> {

	public static final Capability<FrogGodCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final GeneralCapabilityHolder<Frog, FrogGodCapability> HOLDER = new GeneralCapabilityHolder<>(
			new ResourceLocation(YoukaisHomecoming.MODID, "frog_god"), CAPABILITY,
			FrogGodCapability.class, FrogGodCapability::new, Frog.class, e -> true
	);

	public static final int COUNT = 3;

	@SerialClass.SerialField(toClient = true)
	private boolean hasHat;

	@SerialClass.SerialField
	private final LinkedHashSet<EntityType<?>> eaten = new LinkedHashSet<>();

	public static void register() {
	}

	public void syncToClient(LivingEntity entity) {
		YoukaisHomecoming.HANDLER.toTrackingPlayers(new FrogSyncPacket(entity, this), entity);
	}

	public void eat(Frog frog, Entity other) {
		if (!hasHat) return;
		var type = other.getType();
		if (!type.is(EntityTypeTags.RAIDERS)) return;
		AABB aabb = frog.getBoundingBox().inflate(20);
		var list = frog.level().getEntities(EntityTypeTest.forClass(Villager.class), aabb, e -> e.hasLineOfSight(frog));
		if (list.isEmpty()) return;
		eaten.add(type);
		if (eaten.size() >= COUNT) {
			eaten.clear();
			hasHat = false;
			frog.spawnAtLocation(YHItems.SUWAKO_HAT.get());
			syncToClient(frog);
		}
	}

	public static boolean canEatSpecial(Frog frog, LivingEntity target) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			return cap.hasHat && target.getType().is(EntityTypeTags.RAIDERS);
		}
		return false;
	}

	public static void onEatTarget(Frog frog, Entity instance) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			cap.eat(frog, instance);
		}
	}

}
