package dev.xkmc.youkaihomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaihomecoming.content.effect.CaffeinatedEffect;
import dev.xkmc.youkaihomecoming.content.effect.EmptyEffect;
import dev.xkmc.youkaihomecoming.content.effect.YoukaifiedEffect;
import dev.xkmc.youkaihomecoming.content.effect.YoukaifyingEffect;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class YHEffects {

	public static final RegistryEntry<YoukaifiedEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new YoukaifiedEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are a Youkai now");

	public static final RegistryEntry<YoukaifyingEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new YoukaifyingEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"You are becoming a Youkai");

	public static final RegistryEntry<CaffeinatedEffect> CAFFEINATED = genEffect("caffeinated",
			() -> new CaffeinatedEffect(MobEffectCategory.NEUTRAL, -10667225),
			"Prevents phantom spawn, prevents sleep, prevents youkaization, and boost attack damage");

	public static final RegistryEntry<EmptyEffect> NATIVE = genEffect("native_god_bless",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"TODO");//TODO

	public static final RegistryEntry<EmptyEffect> UNCONSCIOUS = genEffect("unconscious",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5522492),
			"TODO");//TODO

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaiHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
