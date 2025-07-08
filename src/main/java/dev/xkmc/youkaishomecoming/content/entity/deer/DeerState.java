package dev.xkmc.youkaishomecoming.content.entity.deer;

import dev.xkmc.youkaishomecoming.content.entity.common.MobState;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum DeerState implements MobState<DeerEntity, DeerState, DeerStateMachine> {
	IDLE(0, null),
	RELAX(0, null, e -> e.relaxDur),
	EAT(34, IDLE, e -> e.eat),
	RELAX_START(22, RELAX, e -> e.relaxStart),
	RELAX_EAT(34, RELAX, e -> e.eat),
	RELAX_END(24, IDLE, e -> e.relaxEnd),
	ATTACK(0, IDLE, e -> e.attack),
	SMELL(0, IDLE, e -> e.smell);

	final int tick;
	final @Nullable DeerState next;
	final Function<DeerStateMachine, AnimationState>[] anims;

	DeerState(int tick, @Nullable DeerState next, Function<DeerStateMachine, AnimationState>... anims) {
		this.tick = tick;
		this.next = next;
		this.anims = anims;
	}

	@Override
	public int tick() {
		return tick;
	}

	@Override
	public @Nullable DeerState next() {
		return next;
	}

	@Override
	public Function<DeerStateMachine, AnimationState>[] anims() {
		return anims;
	}

	public boolean mobile() {
		return this == IDLE;
	}

	public boolean isRelaxed() {
		return this == RELAX_START || this == RELAX || this == RELAX_EAT || this == RELAX_END;
	}

}
