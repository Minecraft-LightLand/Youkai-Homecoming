package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class YHSounds {

	public static final RegistryEntry<SoundEvent> KOISHI_RING = reg("koishi_ring");

	private static RegistryEntry<SoundEvent> reg(String id) {
		ResourceLocation rl = YoukaisHomecoming.loc(id);
		return YoukaisHomecoming.REGISTRATE.simple(id, Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(rl));
	}

	public static void register() {
	}

}
