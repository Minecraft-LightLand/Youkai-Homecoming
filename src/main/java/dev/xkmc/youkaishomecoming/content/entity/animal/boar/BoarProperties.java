package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import net.minecraft.network.syncher.EntityDataAccessor;

public class BoarProperties {

	private final BoarEntity e;

	public BoarProperties(BoarEntity e) {
		this.e = e;
	}

	private void setFlag(EntityDataAccessor<Integer> data, int slot, boolean b) {
		int b0 = e.entityData().get(data);
		int mask = 1 << slot;
		e.entityData().set(data, b ? (b0 | mask) : (b0 & ~mask));
	}

	private boolean getFlag(EntityDataAccessor<Integer> data, int slot) {
		int mask = 1 << slot;
		return (e.entityData().get(data) & mask) != 0;
	}

	public void setPanic(boolean b) {
		setFlag(BoarEntity.TRANSIENT_DATA, 0, b);
	}

	public boolean isPanic() {
		return getFlag(BoarEntity.TRANSIENT_DATA, 0);
	}

	public void setVariant(BoarVariant variant) {
		e.entityData().set(BoarEntity.VARIANT, variant.ordinal());
	}

	public BoarVariant getVariant() {
		int ans = e.entityData().get(BoarEntity.VARIANT);
		BoarVariant[] all = BoarVariant.values();
		if (ans < 0 || ans >= all.length) return all[0];
		return all[ans];
	}

	public int eatWillingness() {
		return e.isBaby() ? 50 : 1000;
	}

	public int sleepiness() {
		return e.level().isNight() ? 100 : 500;
	}

	public int sleepTime() {
		return 1000;
	}

	public boolean eatConsume() {
		if (e.isBaby()) return true;
		if (e.getHealth() < e.getMaxHealth()) return true;
		return e.getRandom().nextFloat() < 0.2f;
	}

}
