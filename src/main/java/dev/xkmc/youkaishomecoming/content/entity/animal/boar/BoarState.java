package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.MobState;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum BoarState implements MobState<BoarEntity, BoarState, BoarStateMachine> {
	IDLE(0, null),
	SLEEP(0, null, e -> e.sleepDur),
	EAT(34, IDLE, e -> e.eat),
	SLEEP_START(22, SLEEP, e -> e.sleepStart),
	SLEEP_END(24, IDLE, e -> e.sleepEnd),
	ATTACK(0, IDLE, e -> e.attack),
	SMELL(0, IDLE, e -> e.smell);

	final int tick;
	final @Nullable BoarState next;
	final Function<BoarStateMachine, AnimationState>[] anims;

	BoarState(int tick, @Nullable BoarState next, Function<BoarStateMachine, AnimationState>... anims) {
		this.tick = tick;
		this.next = next;
		this.anims = anims;
	}

	@Override
	public int tick() {
		return tick;
	}

	@Override
	public @Nullable BoarState next() {
		return next;
	}

	@Override
	public Function<BoarStateMachine, AnimationState>[] anims() {
		return anims;
	}

	public boolean mobile() {
		return this == IDLE;
	}

	public boolean isSleeping() {
		return this == SLEEP_START || this == SLEEP || this == SLEEP_END;
	}

}
