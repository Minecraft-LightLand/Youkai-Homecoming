package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgRange;

public record RingFormData(
		@ArgRange(low = 1, high = 16)
		int branches,
		@ArgRange(low = 1, high = 64)
		int steps,
		@ArgRange(high = 40)
		int delay,
		@ArgRange(base = 30, factor = 6)
		double branchAngle,
		@ArgRange(base = 30, factor = 6)
		double stepAngle,
		@ArgRange(base = 15, factor = 6)
		double stepVerticalAngle,
		@ArgRange(base = 10, factor = 6)
		double randomizedAngle
) {

	public int getDuration() {
		return (steps - 1) * delay;
	}

}
