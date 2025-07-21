package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.content.item.fluid.*;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHDrink implements IYHFluidHolder {
	GREEN_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA, 1200, 1, 1),
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	WHITE_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(YHEffects.REFRESHING, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	BLACK_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	OOLONG_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(YHEffects.MEDITATION, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	DARK_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(YHEffects.SMOOTHING, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	YELLOW_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(YHEffects.BREATHING, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	CORNFLOWER_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.REGENERATION, 200, 0, 1)
	), YHTagGen.TEA_DRINK),
	TEA_MOCHA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	SAIDI_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	SAKURA_HONEY_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 400, 0, 1),
			new EffectEntry(() -> MobEffects.REGENERATION, 400, 0, 1)
	), YHTagGen.TEA_DRINK),
	GENMAI_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER, 1200, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	), YHTagGen.TEA_DRINK),
	SCARLET_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.THICK, 1200, 0, 1),
			new EffectEntry(YHEffects.YOUKAIFYING, 1200, 0, 1)
	), YHTagGen.TEA_DRINK, YHTagGen.FLESH_FOOD),
	GREEN_WATER(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA, 600, 0, 0.1f)
	), YHTagGen.TEA_DRINK),

	MIO(FoodType.BOTTLE, 0xff7890e5, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1)
	), YHTagGen.SAKE),
	MEAD(FoodType.BOTTLE, 0xfffbe8a6, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 0, 1)
	), YHTagGen.SAKE),
	SPARROW_SAKE(FoodType.BAMBOO, 0xfffffacd, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.LUCK, 1200, 0, 1)
	), YHTagGen.SAKE),
	KIKU(FoodType.SAKE, 0xffd5d6b8, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1)
	), YHTagGen.SAKE),
	HAKUTSURU(FoodType.SAKE, 0xfff1ddbd, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1)
	), YHTagGen.SAKE),
	KAPPA_VILLAGE(FoodType.SAKE, 0xffd5d6b8, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.WATER_BREATHING, 1200, 0, 0.5f)
	), YHTagGen.SAKE),
	SUIGEI(FoodType.SAKE, 0xffe3f9fb, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.WATER_BREATHING, 1200, 0, 1)
	), YHTagGen.SAKE),
	DAIGINJO(FoodType.SAKE, 0xffebc78b, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 1200, 2, 1)
	), YHTagGen.SAKE),
	DASSAI(FoodType.SAKE, 0xffa86f64, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.DIG_SPEED, 1200, 2, 1)
	), YHTagGen.SAKE),
	TENGU_TANGO(FoodType.SAKE, 0xffad6843, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 2, 1)
	), YHTagGen.SAKE),
	FULL_MOONS_EVE(FoodType.SAKE, 0xfff3fafb, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA, 1200, 1, 1)
	), YHTagGen.SAKE),
	SCARLET_MIST(FoodType.BOTTLE, 0xFFEA6B88, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 1, 1),
			new EffectEntry(YHEffects.YOUKAIFIED, 1200, 0, 1)
	), YHTagGen.FLESH_FOOD),
	WIND_PRIESTESSES(FoodType.BOTTLE, 0xFF79E1CA, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(YHEffects.NATIVE, 600, 0, 1)
	), YHTagGen.SAKE),

	BLACK_GRAPE_JUICE(FoodType.BOTTLE, 0xff54263c, List.of()),
	RED_GRAPE_JUICE(FoodType.BOTTLE, 0xff9e2359, List.of()),
	WHITE_GRAPE_JUICE(FoodType.BOTTLE, 0xffa7a772, List.of()),
	RED_WINE(FoodType.BOTTLE, 0xff932c39, List.of(
			new EffectEntry(YHEffects.DRUNK, 2000, 0, 1),
			new EffectEntry(YHEffects.EARTHY, 1000, 0, 1)
	), YHTagGen.WINE),
	WHITE_WINE(FoodType.BOTTLE, 0xffe6dbb9, List.of(
			new EffectEntry(YHEffects.DRUNK, 2000, 0, 1),
			new EffectEntry(YHEffects.ENJOYABLE, 1000, 0, 1)
	), YHTagGen.WINE),
	BURGUNDY(FoodType.BOTTLE, 0xff7a1c2c, List.of(
			new EffectEntry(YHEffects.DRUNK, 3000, 0, 1),
			new EffectEntry(YHEffects.EARTHY, 1500, 1, 1)
	), YHTagGen.WINE),
	CHAMPAGNE(FoodType.BOTTLE, 0xffe2d7b1, List.of(
			new EffectEntry(YHEffects.DRUNK, 3000, 0, 1),
			new EffectEntry(YHEffects.ENJOYABLE, 3000, 0, 1)
	), YHTagGen.WINE),
	VAN_ALLEN(FoodType.BOTTLE, 0xff991a5e, List.of(
			new EffectEntry(YHEffects.DRUNK, 3000, 0, 1),
			new EffectEntry(YHEffects.EARTHY, 3000, 0, 1)
	), YHTagGen.WINE),
	;

	public final int color;
	public final String folder;

	public final FluidEntry<YHFluid> fluid;
	public final ItemEntry<Item> item;

	@Nullable
	public final BottledDrinkSet set;

	@SafeVarargs
	YHDrink(FoodType type, int color, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.color = color;
		String name = name().toLowerCase(Locale.ROOT);
		fluid = BottledFluid.water(name,
						(p, s, f) -> new YHFluidType(p, s, f, this),
						p -> new YHFluid(p, this))
				.defaultLang().register();
		folder = name.contains("tea") || name.contains("water") ? "drink" :
				name.contains("juice") || tags.length > 0 && tags[0] == YHTagGen.WINE ? "wine" : "sake";
		item = type.build(p -> new SakeBottleItem(fluid::getSource, p), "food/" + folder + "/",
				name, 0, 0, tags, effs);

		if (tags.length > 0 && tags[0] == YHTagGen.WINE) {
			set = new BottledDrinkSet(this);
		} else set = null;//TODO
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public ItemEntry<?> item() {
		return item;
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

	@Override
	public YHFluid source() {
		return fluid.getSource();
	}

	@Override
	public @Nullable String bottleTextureFolder() {
		return folder;
	}

	@SuppressWarnings("deprecation")
	public Item getContainer() {
		Item ans = item.get().getCraftingRemainingItem();
		if (ans == Items.BAMBOO) {
			ans = ForgeRegistries.ITEMS.getValue(new ResourceLocation("bamboo"));
		}
		if (ans == null) return Items.AIR;
		return ans;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public ItemStack asStack(int count) {
		return item.asStack(count);
	}

	public boolean isFlesh() {
		return this == SCARLET_MIST || this == SCARLET_TEA;
	}

	public static void register() {

	}


}
