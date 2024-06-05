package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@SerialClass
public class YoukaiTargetContainer {

	private final YoukaiEntity youkai;
	private final int maxSize;

	@SerialClass.SerialField
	private final LinkedHashSet<UUID> list = new LinkedHashSet<>();

	public YoukaiTargetContainer(YoukaiEntity youkai, int maxSize) {
		this.youkai = youkai;
		this.maxSize = maxSize;
	}

	public void tick() {
		if (youkai.level().isClientSide()) return;
		LivingEntity le = youkai.getLastHurtByMob();
		if (le != null && le.isAlive() && le.canBeSeenAsEnemy() && !list.contains(le.getUUID())) {
			list.add(le.getUUID());
		} else {
			le = youkai.getTarget();
			if (le != null && le.isAlive() && le.canBeSeenAsEnemy()) {
				list.add(le.getUUID());
			}
		}
		list.removeIf(e -> !isValid(e));
		if (list.size() > maxSize) {
			var tmp = new ArrayList<>(list);
			list.clear();
			list.addAll(tmp.subList(list.size() - maxSize, list.size()));
		}
	}

	private boolean isValid(UUID id) {
		Entity e = ((ServerLevel) youkai.level()).getEntity(id);
		if (e instanceof LivingEntity le && le.isAlive()) {
			return le.canBeSeenAsEnemy();
		}
		return false;
	}

	public boolean contains(LivingEntity e) {
		return youkai.getTarget() == e || list.contains(e.getUUID());
	}

	public void checkTarget() {
		if (!(youkai.level() instanceof ServerLevel sl)) return;
		var last = youkai.getLastHurtByMob();
		if ((last == null || !last.isAlive() || !last.canBeSeenAsEnemy()) && !list.isEmpty()) {
			var tmp = new ArrayList<>(list);
			var id = tmp.get(tmp.size() - 1);
			Entity e = sl.getEntity(id);
			if (e instanceof LivingEntity le && le.isAlive() && le.canBeSeenAsEnemy())
				youkai.setLastHurtByMob(le);
		}
	}

	public List<LivingEntity> getTargets() {
		List<LivingEntity> ans = new ArrayList<>();
		if (!(youkai.level() instanceof ServerLevel sl)) return ans;
		for (var id : list) {
			Entity e = sl.getEntity(id);
			if (e instanceof LivingEntity le && le.isAlive() && le.canBeSeenAsEnemy())
				ans.add(le);
		}
		return ans;
	}
}
