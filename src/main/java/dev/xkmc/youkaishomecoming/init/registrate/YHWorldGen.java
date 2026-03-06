package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.content.world.NoisePlacement;
import dev.xkmc.youkaishomecoming.content.world.NoiseQuadCondition;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class YHWorldGen {

	public static final Val<PlacementModifierType<NoisePlacement>> NOISE;

	static {
		NOISE = SR.of(YoukaisHomecoming.REG, Registries.PLACEMENT_MODIFIER_TYPE)
				.reg("noise", () -> () -> NoisePlacement.CODEC);
		SR.of(YoukaisHomecoming.REG, Registries.MATERIAL_CONDITION).reg("noise",
				() -> NoiseQuadCondition.CODEC.codec());
	}

	public static void register() {

	}

}
