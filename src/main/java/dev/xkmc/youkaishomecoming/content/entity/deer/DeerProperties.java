package dev.xkmc.youkaishomecoming.content.entity.deer;

import net.minecraft.network.syncher.EntityDataAccessor;

public class DeerProperties {

	private final DeerEntity e;

	public DeerProperties(DeerEntity e) {
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
		setFlag(DeerEntity.TRANSIENT_DATA, 0, b);
	}

	public boolean isPanic() {
		return getFlag(DeerEntity.TRANSIENT_DATA, 0);
	}

	public void setMale(boolean b) {
		setFlag(DeerEntity.FLAGS, 0, b);
	}

	public boolean isMale() {
		return getFlag(DeerEntity.FLAGS, 0);
	}

	public void setHorned(boolean b) {
		setFlag(DeerEntity.FLAGS, 1, b);
	}

	public boolean isHorned() {
		return getFlag(DeerEntity.FLAGS, 1);
	}

	public void setEatAge(int stage) {
		e.entityData().set(DeerEntity.EAT_STAGE, stage);
	}

	public int getEatAge() {
		return e.entityData().get(DeerEntity.EAT_STAGE);
	}

	public void setVariant(DeerVariant variant) {
		e.entityData().set(DeerEntity.VARIANT, variant.ordinal());
	}

	public DeerVariant getVariant() {
		int ans = e.entityData().get(DeerEntity.VARIANT);
		DeerVariant[] all = DeerVariant.values();
		if (ans < 0 || ans >= all.length) return all[0];
		return all[ans];
	}

	public int eatWillingness() {
		return e.isBaby() ? 50 : isMale() && !isHorned() ? 300 : 1000;
	}

	public int relaxWillingness() {
		return 500;
	}

	public int relaxTime() {
		return 1000;
	}

	public boolean eatConsume() {
		if (e.isBaby() || isMale() && !isHorned()) return true;
		if (e.getHealth() < e.getMaxHealth()) return true;
		return e.getRandom().nextFloat() < 0.2f;
	}

}
