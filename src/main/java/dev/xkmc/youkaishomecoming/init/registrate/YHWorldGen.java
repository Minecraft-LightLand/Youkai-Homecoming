package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.world.NoisePlacement;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class YHWorldGen {

	public static final RegistryEntry<PlacementModifierType<NoisePlacement>> NOISE;

	static {
		NOISE = YoukaisHomecoming.REGISTRATE.simple("noise", Registries.PLACEMENT_MODIFIER_TYPE,
				() -> () -> NoisePlacement.CODEC);
	}

	public static void register() {

	}

}
