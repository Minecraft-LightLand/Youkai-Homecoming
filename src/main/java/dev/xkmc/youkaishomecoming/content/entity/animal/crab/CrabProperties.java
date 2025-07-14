package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.effect.MobEffects;

public class CrabProperties {

	private final CrabEntity e;

	public CrabProperties(CrabEntity e) {
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

	public void setFromBucket(boolean b) {
		setFlag(CrabEntity.FLAGS, 0, b);
	}

	public boolean isFromBucket() {
		return getFlag(CrabEntity.FLAGS, 0);
	}

	public void setVariant(CrabVariant variant) {
		e.entityData().set(CrabEntity.VARIANT, variant.ordinal());
	}

	public CrabVariant getVariant() {
		int ans = e.entityData().get(CrabEntity.VARIANT);
		CrabVariant[] all = CrabVariant.values();
		if (ans < 0 || ans >= all.length) return all[0];
		return all[ans];
	}

	public int digWillingness() {
		var ins = e.getEffect(MobEffects.DIG_SPEED);
		if (ins == null) return 1000;
		return Math.max((int) (1000 / (ins.getAmplifier() * 0.3 + 1.3)), 50);
	}

	public int flipPower() {
		return 100;
	}

	public static float getOnLandChance() {
		return 0.2f;
	}

}
