package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.item.CaffeinatedEffect;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CoffeeEffects {

	public static final RegistryEntry<MobEffect> CAFFEINATED = genEffect("caffeinated",
			() -> new CaffeinatedEffect(MobEffectCategory.NEUTRAL, 0x21c189),
			"Prevents phantom spawn, nausea, and sleep. ");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaisHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {
	}

}
