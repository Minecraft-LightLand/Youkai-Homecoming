package dev.xkmc.youkaishomecoming.content.entity.crab;

import dev.xkmc.youkaishomecoming.content.entity.common.MobState;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum CrabState implements MobState<CrabEntity, CrabState, CrabStateMachine> {
	IDLE(0, null),
	FLIP(0, null, e -> e.flip),
	SWING(20, IDLE, e -> e.swing),
	DIG(64, IDLE, e -> e.dig),
	;

	final int tick;
	final @Nullable CrabState next;
	final Function<CrabStateMachine, AnimationState>[] anims;

	CrabState(int tick, @Nullable CrabState next, Function<CrabStateMachine, AnimationState>... anims) {
		this.tick = tick;
		this.next = next;
		this.anims = anims;
	}

	@Override
	public int tick() {
		return tick;
	}

	@Override
	public @Nullable CrabState next() {
		return next;
	}

	@Override
	public Function<CrabStateMachine, AnimationState>[] anims() {
		return anims;
	}

}
