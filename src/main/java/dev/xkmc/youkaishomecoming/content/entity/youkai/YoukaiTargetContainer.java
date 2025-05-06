package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.fastprojectileapi.collision.EntityStorageHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

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

	public void tick(@Nullable LivingEntity vanillaTarget) {
		if (youkai.level().isClientSide()) return;
		LivingEntity le = youkai.getLastHurtByMob();
		if (le != null && isValid(le) && !list.contains(le.getUUID())) {
			list.add(le.getUUID());
		} else {
			le = vanillaTarget;
			if (le != null)
				if (isValid(le))
					list.add(le.getUUID());
				else youkai.setTarget(null);
		}
		list.removeIf(e -> !isValid(e));
		if (list.size() > maxSize) {
			var tmp = new ArrayList<>(list);
			list.clear();
			list.addAll(tmp.subList(tmp.size() - maxSize, tmp.size()));
		}
	}

	public void add(LivingEntity le) {
		if (isValid(le)) {
			list.add(le.getUUID());
		}
	}

	public void remove(UUID id) {
		list.remove(id);
	}

	private boolean isValid(LivingEntity le) {
		return le.isAlive() && le != youkai && le.canBeSeenAsEnemy() &&
				le.isAddedToWorld() && le.level() == youkai.level() &&
				EntityStorageHelper.isPresent(le) && youkai.canAttack(le);
	}

	private boolean isValid(UUID id) {
		Entity e = ((ServerLevel) youkai.level()).getEntity(id);
		if (e instanceof LivingEntity le) {
			return isValid(le);
		}
		return false;
	}

	public boolean contains(LivingEntity e) {
		return youkai.getTarget() == e || list.contains(e.getUUID());
	}

	public void checkTarget() {
		if (!(youkai.level() instanceof ServerLevel sl)) return;
		var last = youkai.getLastHurtByMob();
		if ((last == null || !isValid(last)) && !list.isEmpty()) {
			var tmp = new ArrayList<>(list);
			var id = tmp.get(tmp.size() - 1);
			Entity e = sl.getEntity(id);
			if (e instanceof LivingEntity le && isValid(le))
				youkai.setLastHurtByMob(le);
		}
	}

	public List<LivingEntity> getTargets() {
		List<LivingEntity> ans = new ArrayList<>();
		if (!(youkai.level() instanceof ServerLevel sl)) return ans;
		for (var id : list) {
			Entity e = sl.getEntity(id);
			if (e instanceof LivingEntity le && isValid(le))
				ans.add(le);
		}
		return ans;
	}
}
