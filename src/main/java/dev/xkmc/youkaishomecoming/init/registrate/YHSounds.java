package dev.xkmc.youkaishomecoming.init.registrate;

import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class YHSounds {

	public static final SimpleEntry<SoundEvent> DEER_AMBIENT = reg("deer_ambient");
	public static final SimpleEntry<SoundEvent> DEER_HURT = reg("deer_hurt");
	public static final SimpleEntry<SoundEvent> DEER_DEATH = reg("deer_death");

	private static SimpleEntry<SoundEvent> reg(String id) {
		ResourceLocation rl = YoukaisHomecoming.loc(id);
		return new SimpleEntry<>(YoukaisHomecoming.REGISTRATE.simple(id, Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(rl)));
	}

	public static void register() {
	}

}
