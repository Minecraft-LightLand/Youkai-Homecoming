package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.effect.MobEffect;

public class FDEffects {

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaiHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
