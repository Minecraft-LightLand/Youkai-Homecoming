package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.effect.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class YHEffects {

	public static final SimpleEntry<MobEffect> TEA = genEffect("mellow",
			() -> new TeaEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Boost attack speed, heal every 3 seconds when under sunlight");

	public static final SimpleEntry<MobEffect> SOBER = genEffect("sober",
			() -> new SoberEffect(MobEffectCategory.NEUTRAL, 0x21c189),
			"Prevents phantom spawn, nausea, youkaization, and sleep. " +
					"You can't have another alcohol or sobering drink while having this effect.");

	public static final SimpleEntry<MobEffect> DRUNK = genEffect("drunk",
			() -> new DrunkEffect(MobEffectCategory.NEUTRAL, 0xF9E6CD),
			"Increase attack damage, but you feel drunk. " +
					"You can't have sobering drink while having this effect. " +
					"Drunk effect can stack up to 5 layers.");

	public static final SimpleEntry<MobEffect> REFRESHING = genEffect("refreshing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xd0c3a5),
			"Immune to fire, puts down fire");

	public static final SimpleEntry<MobEffect> THICK = genEffect("thick",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x534b40),
			"Reduce damage by 1");

	public static final SimpleEntry<MobEffect> MEDITATION = genEffect("meditation",
			() -> new MeditationEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Gain experience gradually when not moving");

	public static final SimpleEntry<MobEffect> BREATHING = genEffect("breathing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"improves health regeneration");

	public static final SimpleEntry<MobEffect> SMOOTHING = genEffect("smoothing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Immune to poison and wither");

	public static final SimpleEntry<MobEffect> UDUMBARA = genEffect("phantom",
			() -> new UdumbaraEffect(MobEffectCategory.BENEFICIAL, 0xFBDCFD),
			"Prevents vibration from you and your projectiles. " +
					"Warps you to the top of the world when falling into the void. " +
					"Heals every 3 seconds under moonlight. " +
					"Reduced damage taken when under full moon.");

	public static final SimpleEntry<MobEffect> HIGI = genEffect("higi",
			() -> new HigiEffect(MobEffectCategory.BENEFICIAL, 0x6CA16E),
			"Boost attack damage and movement speed, heal slowly");

	public static final SimpleEntry<MobEffect> CRABY = genEffect("craby",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Makes your horizontal movement faster");//TODO

	public static final SimpleEntry<MobEffect> CONFIDENT = genEffect("confident",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Increase invulnerability time after being attacked");

	public static final SimpleEntry<MobEffect> ENJOYABLE = genEffect("enjoyable",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Increase beneficial food effect duration");

	public static final SimpleEntry<MobEffect> MATURE = genEffect("mature",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Consume Nourishment effect duration to reduce incoming damage");

	public static final SimpleEntry<MobEffect> EARTHY = genEffect("earthy",
			() -> new EarthyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Digest and heal when player is full");

	private static <T extends MobEffect> SimpleEntry<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(YoukaisHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {
	}

}
