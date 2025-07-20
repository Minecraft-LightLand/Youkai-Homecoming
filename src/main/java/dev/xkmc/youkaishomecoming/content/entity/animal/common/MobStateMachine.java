package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;

public abstract class MobStateMachine<
		E extends Mob & StateMachineMob,
		S extends Enum<S> & MobState<E, S, M>,
		M extends MobStateMachine<E, S, M>
		> {

	public static final int OFFSET = 80;

	protected final Class<S> cls;
	protected final S[] allStates;
	protected final E mob;

	private S state;
	private int tick;

	public MobStateMachine(E entity, Class<S> cls, S[] values) {
		this.cls = cls;
		mob = entity;
		allStates = values;
		state = allStates[0];
	}

	public M self() {
		return Wrappers.cast(this);
	}

	public S state() {
		return state;
	}

	public void tick() {
		if (tick > 0) {
			tick--;
		} else if (state.next() != null) {
			transitionTo(state.next());
		}
	}

	public boolean transitionTo(byte id) {
		if (id >= OFFSET && id - OFFSET < allStates.length) {
			transitionTo(allStates[id - OFFSET]);
			return true;
		}
		return false;
	}

	public void transitionTo(S data) {
		transitionTo(data, data.tick());
	}

	protected void transitionTo(S data, int tickRemain) {
		if (!mob.level().isClientSide()) {
			mob.level().broadcastEntityEvent(mob, data.id());
		} else {
			var old = state.anims(self());
			var next = data.anims(self());
			var discard = new ArrayList<>(old);
			discard.removeAll(next);
			var add = new ArrayList<>(next);
			add.removeAll(old);
			for (var e : discard) e.stop();
			for (var e : add) e.startIfStopped(mob.tickCount - (data.tick() - tickRemain));
		}
		state = data;
		tick = tickRemain;
	}

	public void write(CompoundTag tag) {
		tag.putString("MobState", state.name());
		tag.putInt("MobStateTick", tick);
	}

	public void read(CompoundTag tag) {
		String str = tag.getString("MobState");
		try {
			state = Enum.valueOf(cls, str);
		} catch (Exception ignored) {
		}
		tick = tag.getInt("MobStateTick");
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(state.ordinal());
		buffer.writeInt(tick);
	}

	public void read(FriendlyByteBuf data) {
		state = allStates[data.readInt()];
		transitionTo(state, data.readInt());
	}

}
