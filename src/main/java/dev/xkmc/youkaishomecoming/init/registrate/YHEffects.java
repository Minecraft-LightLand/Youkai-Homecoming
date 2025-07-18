package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.content.effect.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class YHEffects {

	public static final RegistryEntry<MobEffect> TEA = genEffect("mellow",
			() -> new TeaEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Boost attack speed, heal every 3 seconds when under sunlight");

	public static final RegistryEntry<MobEffect> SOBER = genEffect("sober",
			() -> new SoberEffect(MobEffectCategory.NEUTRAL, 0x21c189),
			"Prevents phantom spawn, nausea, youkaization, and sleep. " +
					"You can't have another alcohol or sobering drink while having this effect.");

	public static final RegistryEntry<MobEffect> DRUNK = genEffect("drunk",
			() -> new DrunkEffect(MobEffectCategory.NEUTRAL, 0xF9E6CD),
			"Increase attack damage, but you feel drunk. " +
					"You can't have sobering drink while having this effect. " +
					"Drunk effect can stack up to 5 layers.");

	public static final RegistryEntry<MobEffect> REFRESHING = genEffect("refreshing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xd0c3a5),
			"Immune to fire, puts down fire");

	public static final RegistryEntry<MobEffect> THICK = genEffect("thick",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x534b40),
			"Reduce damage by 1");

	public static final RegistryEntry<MobEffect> MEDITATION = genEffect("meditation",
			() -> new MeditationEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Gain experience gradually when not moving");

	public static final RegistryEntry<MobEffect> BREATHING = genEffect("breathing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"improves health regeneration");

	public static final RegistryEntry<MobEffect> SMOOTHING = genEffect("smoothing",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x949537),
			"Immune to poison and wither");

	public static final RegistryEntry<MobEffect> UDUMBARA = genEffect("phantom",
			() -> new UdumbaraEffect(MobEffectCategory.BENEFICIAL, 0xFBDCFD),
			"Prevents vibration from you and your projectiles. " +
					"Warps you to the top of the world when falling into the void. " +
					"Heals every 3 seconds under moonlight. " +
					"Reduced damage taken when under full moon.");

	public static final RegistryEntry<MobEffect> HIGI = genEffect("higi",
			() -> new HigiEffect(MobEffectCategory.BENEFICIAL, 0x6CA16E),
			"Boost attack damage and movement speed, heal slowly");

	public static final RegistryEntry<MobEffect> CRABY = genEffect("craby",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Makes your horizontal movement faster");//TODO

	public static final RegistryEntry<MobEffect> CONFIDENT = genEffect("confident",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Increase invulnerability time after being attacked");

	public static final RegistryEntry<MobEffect> ENJOYABLE = genEffect("enjoyable",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Increase beneficial food effect duration");

	public static final RegistryEntry<MobEffect> MATURE = genEffect("mature",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Consume Nourishment effect duration to reduce incoming damage");

	public static final RegistryEntry<MobEffect> EARTHY = genEffect("earthy",
			() -> new EarthyEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Digest and heal when player is full");

	public static final RegistryEntry<MobEffect> FLAMING = genEffect("flaming",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
					.addAttributeModifier(L2DamageTracker.FIRE_FACTOR.get(),
							MathHelper.getUUIDFromString(YoukaisHomecoming.MODID + ":flaming").toString(),
							0.25, AttributeModifier.Operation.ADDITION),
			"Increase fire damage you dealt");

	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return YoukaisHomecoming.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static final List<RegistryEntry<? extends Potion>> POTION_LIST = new ArrayList<>();

	private static final List<Runnable> TEMP = new ArrayList<>();

	public static void registerBrewingRecipe() {
		TEMP.forEach(Runnable::run);
	}

	private static <T extends Potion> RegistryEntry<T> genPotion(String name, NonNullSupplier<T> sup) {
		RegistryEntry<T> ans = YoukaisHomecoming.REGISTRATE.entry(name, (cb) -> new NoConfigBuilder<>(YoukaisHomecoming.REGISTRATE, YoukaisHomecoming.REGISTRATE, name, cb, ForgeRegistries.Keys.POTIONS, sup)).register();
		POTION_LIST.add(ans);
		return ans;
	}

	private static void regPotion2(String id, Supplier<MobEffect> sup, Supplier<Item> item, int dur, int durLong, Supplier<Potion> base) {
		var potion = genPotion(id, () -> new Potion(new MobEffectInstance(sup.get(), dur)));
		var longPotion = genPotion("long_" + id, () -> new Potion(new MobEffectInstance(sup.get(), durLong)));
		TEMP.add(() -> {
			PotionBrewing.addMix(base.get(), item.get(), potion.get());
			PotionBrewing.addMix(potion.get(), Items.REDSTONE, longPotion.get());
		});
	}

	public static void register() {
	}

}
