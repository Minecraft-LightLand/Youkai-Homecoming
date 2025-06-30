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
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum YHDrink implements IYHFluidHolder {
	GREEN_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 1, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1)
	)),
	WHITE_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.REFRESHING::get, 1200, 0, 1)
	)),
	BLACK_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1)
	)),
	OOLONG_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SMOOTHING::get, 600, 0, 1)
	)),
	CORNFLOWER_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.REGENERATION, 200, 0, 1)
	)),
	TEA_MOCHA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	)),
	SAIDI_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 0, 1)
	)),
	SAKURA_HONEY_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 400, 0, 1),
			new EffectEntry(() -> MobEffects.REGENERATION, 400, 0, 1)
	)),
	GENMAI_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 1, 1),
			new EffectEntry(YHEffects.SOBER::get, 1200, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 1200, 0, 1)
	)),
	SCARLET_TEA(FoodType.BOTTLE, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1),
			new EffectEntry(YHEffects.YOUKAIFYING::get, 1200, 0, 1)
	), YHTagGen.FLESH_FOOD),
	GREEN_WATER(FoodType.BOTTLE, 0xffffffff, List.of(new EffectEntry(YHEffects.TEA::get, 600, 0, 0.1f))),

	MIO(FoodType.BOTTLE, 0xff7890e5, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1))),
	MEAD(FoodType.BOTTLE, 0xfffbe8a6, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 0, 1))),
	SPARROW_SAKE(FoodType.BAMBOO, 0xfffffacd, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.LUCK, 1200, 0, 1))),
	KIKU(FoodType.SAKE, 0xffd5d6b8, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1))),
	HAKUTSURU(FoodType.SAKE, 0xfff1ddbd, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1))),
	KAPPA_VILLAGE(FoodType.SAKE, 0xffd5d6b8, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.WATER_BREATHING, 1200, 0, 0.5f))),
	SUIGEI(FoodType.SAKE, 0xffe3f9fb, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(() -> MobEffects.WATER_BREATHING, 1200, 0, 1))),
	DAIGINJO(FoodType.SAKE, 0xffebc78b, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.DAMAGE_BOOST, 1200, 2, 1))),
	DASSAI(FoodType.SAKE, 0xffa86f64, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.DIG_SPEED, 1200, 2, 1))),
	TENGU_TANGO(FoodType.SAKE, 0xffad6843, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 1, 1),
			new EffectEntry(() -> MobEffects.MOVEMENT_SPEED, 1200, 2, 1))),
	FULL_MOONS_EVE(FoodType.SAKE, 0xfff3fafb, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(YHEffects.UDUMBARA::get, 1200, 1, 1))),
	SCARLET_MIST(FoodType.BOTTLE, 0xFFEA6B88, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 1, 1),
			new EffectEntry(YHEffects.YOUKAIFIED::get, 1200, 0, 1)
	), YHTagGen.FLESH_FOOD),
	WIND_PRIESTESSES(FoodType.BOTTLE, 0xFF79E1CA, List.of(
			new EffectEntry(YHEffects.DRUNK::get, 1200, 0, 1),
			new EffectEntry(YHEffects.NATIVE::get, 600, 0, 1)
	)),

	BLACK_GRAPE_JUICE(FoodType.BOTTLE, 0xff54263c, List.of()),
	RED_GRAPE_JUICE(FoodType.BOTTLE, 0xff9e2359, List.of()),
	WHITE_GRAPE_JUICE(FoodType.BOTTLE, 0xffa7a772, List.of()),
	RED_WINE(FoodType.BOTTLE, 0xff932c39, List.of(), YHTagGen.WINE),
	WHITE_WINE(FoodType.BOTTLE, 0xffe6dbb9, List.of(), YHTagGen.WINE),
	BURGUNDY(FoodType.BOTTLE, 0xff7a1c2c, List.of(), YHTagGen.WINE),
	CHAMPAGNE(FoodType.BOTTLE, 0xffe2d7b1, List.of(), YHTagGen.WINE),
	VAN_ALLEN(FoodType.BOTTLE, 0xff991a5e, List.of(), YHTagGen.WINE),
	;

	public final int color;

	public final FluidEntry<YHFluid> fluid;
	public final ItemEntry<Item> item;

	@SafeVarargs
	YHDrink(FoodType type, int color, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.color = color;
		String name = name().toLowerCase(Locale.ROOT);
		fluid = BottledFluid.water(name,
						(p, s, f) -> new YHFluidType(p, s, f, this),
						p -> new YHFluid(p, this))
				.defaultLang().register();
		String folder = name.contains("tea") || name.contains("water") ? "drink" :
				name.contains("juice") || tags.length > 0 && tags[0] == YHTagGen.WINE ? "wine" : "sake";
		item = type.build(p -> new SakeBottleItem(fluid, p), "food/" + folder + "/",
				name, 0, 0, tags, effs);
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
	public FluidEntry<? extends YHFluid> fluid() {
		return fluid;
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
