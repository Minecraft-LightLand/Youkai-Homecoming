package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2core.init.reg.registrate.LegacyHolder;
import dev.xkmc.youkaishomecoming.content.effect.*;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
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

	public static final LegacyHolder<MobEffect> YOUKAIFIED = genEffect("youkaified",
			() -> new YoukaifiedEffect(MobEffectCategory.NEUTRAL, 0xffffff),//TODO color
			"You are a Youkai now");

	public static final LegacyHolder<MobEffect> YOUKAIFYING = genEffect("youkaifying",
			() -> new YoukaifyingEffect(MobEffectCategory.NEUTRAL, 0xffffff),//TODO color
			"You are becoming a Youkai");

	public static final LegacyHolder<MobEffect> NATIVE = genEffect("native_god_bless",
			() -> new NativeGodBlessEffect(MobEffectCategory.BENEFICIAL, -5727850),
			"Increase movement speed and reach");

	public static final LegacyHolder<MobEffect> UNCONSCIOUS = genEffect("unconscious",
			() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, -5522492),
			"You won't be targeted by mobs. Terminates when you attack or open loot chests.");

	public static final LegacyHolder<MobEffect> HIGI = genEffect("higi",
			() -> new HigiEffect(MobEffectCategory.BENEFICIAL, 0x6CA16E),
			"Boost attack damage and movement speed, heal slowly");

	public static final LegacyHolder<MobEffect> FAIRY = genEffect("fairy",
			() -> new FairyEffect(MobEffectCategory.NEUTRAL, 0xd0c3a5),
			"Reduce max HP. Gives speed and healing boost. Enables danmaku");

	private static <T extends MobEffect> LegacyHolder<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
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
		regPotion2("aphrodisiac", APHRODISIAC::get, YHItems.DRIED_MANDRAKE_FLOWER, 3600, 9600, () -> Potions.HEALING);
		regPotion2("hypnosis", HYPNOSIS::get, YHItems.STRIPPED_MANDRAKE_ROOT, 3600, 9600, () -> Potions.WEAKNESS);
	}

}
