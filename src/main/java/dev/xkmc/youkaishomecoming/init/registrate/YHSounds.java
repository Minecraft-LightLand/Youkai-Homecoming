package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class YHSounds {

	public static final RegistryEntry<SoundEvent> KOISHI_RING = reg("koishi_ring");
	public static final RegistryEntry<SoundEvent> GRAZE = reg("graze");
	public static final RegistryEntry<SoundEvent> MISS = reg("miss");
	public static final RegistryEntry<SoundEvent> DEER_AMBIENT = reg("deer_ambient");
	public static final RegistryEntry<SoundEvent> DEER_HURT = reg("deer_hurt");
	public static final RegistryEntry<SoundEvent> DEER_DEATH = reg("deer_death");

	private static RegistryEntry<SoundEvent> reg(String id) {
		ResourceLocation rl = YoukaisHomecoming.loc(id);
		return YoukaisHomecoming.REGISTRATE.simple(id, Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(rl));
	}

	public static void register() {
	}

}
