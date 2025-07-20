package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static dev.xkmc.youkaishomecoming.content.entity.animal.common.MobStateMachine.OFFSET;

public interface MobState<
		E extends Mob & StateMachineMob,
		S extends Enum<S> & MobState<E, S, T>,
		T extends MobStateMachine<E, S, T>
		> {

	static <T> void nop(T e) {
	}

	int ordinal();

	String name();

	int tick();

	@Nullable S next();

	Function<T, AnimationState>[] anims();

	default List<AnimationState> anims(T e) {
		List<AnimationState> list = new ArrayList<>();
		for (var anim : anims()) {
			list.add(anim.apply(e));
		}
		return list;
	}

	default byte id() {
		return (byte) (OFFSET + ordinal());
	}

}
