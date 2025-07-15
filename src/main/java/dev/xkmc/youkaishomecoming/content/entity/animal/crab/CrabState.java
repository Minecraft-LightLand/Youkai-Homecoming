package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.MobState;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum CrabState implements MobState<CrabEntity, CrabState, CrabStateMachine> {
	IDLE(0, null),
	FLIP(0, null, e -> e.flip),
	HIDE(0, null, e -> e.hide),
	SWING(20, IDLE, e -> e.swing),
	DIG(64, IDLE, e -> e.dig),
	HIDE_START(41, HIDE, e -> e.hideStart),
	HIDE_END(24, IDLE, e -> e.hideEnd),
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

	public boolean isHiding() {
		return this == HIDE || this == HIDE_START || this == HIDE_END;
	}

}
