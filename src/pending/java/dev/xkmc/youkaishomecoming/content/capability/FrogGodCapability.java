package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
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
			YoukaisHomecoming.loc("frog_god"), CAPABILITY,
			FrogGodCapability.class, FrogGodCapability::new, Frog.class, e -> true
	);

	@SerialClass.SerialField(toClient = true)
	public boolean hasHat;

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
		int noSight = YHModConfig.COMMON.frogEatRaiderVillagerNoSightRange.get();
		int sight = YHModConfig.COMMON.frogEatRaiderVillagerSightRange.get();
		AABB aabb = frog.getBoundingBox().inflate(noSight);
		var list = frog.level().getEntities(EntityTypeTest.forClass(Villager.class), aabb, e ->
				e.hasLineOfSight(frog) || e.hasLineOfSight(other) || e.distanceTo(frog) < sight);
		if (list.isEmpty()) return;
		eaten.add(type);
		if (eaten.size() >= YHModConfig.COMMON.frogEatCountForHat.get()) {
			eaten.clear();
			hasHat = false;
			syncToClient(frog);
			frog.spawnAtLocation(YHItems.SUWAKO_HAT.get());
		}
	}

	public static boolean canEatSpecial(Frog frog, LivingEntity target) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			return cap.hasHat && target.getType().is(EntityTypeTags.RAIDERS);
		}
		return false;
	}

	public static boolean hasHat(Frog frog) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			return cap.hasHat;
		}
		return false;
	}

	public static void onEatTarget(Frog frog, Entity instance) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			cap.eat(frog, instance);
		}
	}

	public static void startTracking(Frog frog, Player entity) {
		if (HOLDER.isProper(frog)) {
			FrogGodCapability cap = HOLDER.get(frog);
			YoukaisHomecoming.HANDLER.toClientPlayer(new FrogSyncPacket(frog, cap), (ServerPlayer) entity);
		}
	}

}
