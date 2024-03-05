package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.effect.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class YHEffects {

	public static final RegistryEntry<YoukaifiedEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new YoukaifiedEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are a Youkai now");

	public static final RegistryEntry<YoukaifyingEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new YoukaifyingEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are becoming a Youkai");

	public static final RegistryEntry<CaffeinatedEffect> CAFFEINATED = genEffect("caffeinated",
			() -> new CaffeinatedEffect(MobEffectCategory.BENEFICIAL, -10667225),
			"Boost attack damage");

	public static final RegistryEntry<TeaEffect> TEA = genEffect("tea_polyphenols",
			() -> new TeaEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Boost attack speed, heal every 3 seconds when under sunlight");

	public static final RegistryEntry<SoberEffect> SOBER = genEffect("sober",
			() -> new SoberEffect(MobEffectCategory.NEUTRAL, 0x21c189),
			"Prevents phantom spawn, nausea, youkaization, and sleep. " +
					"You can't have another sobering drink while having this effect.");

	public static final RegistryEntry<EmptyEffect> REFRESHING = genEffect("refreshing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xd0c3a5),
			"Immune to fire, puts down fire");

	public static final RegistryEntry<EmptyEffect> THICK = genEffect("thick",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x534b40),
			"Immune to wither, reduces damage by 1");

	public static final RegistryEntry<EmptyEffect> SMOOTHING = genEffect("smoothing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Immune to poison, improves health regeneration");

	public static final RegistryEntry<NativeGodBlessEffect> NATIVE = genEffect("native_god_bless",
			() -> new NativeGodBlessEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Increase movement speed and reach");

	public static final RegistryEntry<EmptyEffect> UNCONSCIOUS = genEffect("unconscious",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5522492),
			"You won't be targeted by mobs. Terminates when you attack.");

	public static final RegistryEntry<MandrakeEffect> MANDRAKE = genEffect("lurking",
			() -> new MandrakeEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"Sneak to sink into ground");

	public static final RegistryEntry<UdumbaraEffect> UDUMBARA = genEffect("phantom",
			() -> new UdumbaraEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Prevents vibration from you and your projectiles. " +
					"Warps you to the top of the world when falling into the void. " +
					"Heals every 3 seconds under moonlight. " +
					"Reduced damage taken when under full moon.");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaisHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
