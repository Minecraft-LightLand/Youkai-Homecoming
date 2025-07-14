package dev.xkmc.youkaishomecoming.compat.food;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.youkaishomecoming.content.item.fluid.*;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public enum FruitsDelightCompatDrink implements IYHFluidHolder {
	MOON_ROCKET(FoodType.BOTTLE_FAST, 0xffffffff, List.of(
			new EffectEntry(() -> MobEffects.JUMP, 100, 1))),
	LEMON_BLACK_TEA(FoodType.BOTTLE_FAST, 0xffffffff, List.of(
			new EffectEntry(YHEffects.TEA::get, 600, 0, 1),
			new EffectEntry(YHEffects.SOBER::get, 600, 0, 1),
			new EffectEntry(YHEffects.THICK::get, 600, 0, 1)
	)),
	;

	public final int color;
	public final String folder;

	public final FluidEntry<YHFluid> fluid;
	public final ItemEntry<Item> item;

	@SafeVarargs
	FruitsDelightCompatDrink(FoodType type, int color, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.color = color;
		String name = name().toLowerCase(Locale.ROOT);
		fluid = BottledFluid.water(name,
						(p, s, f) -> new YHFluidType(p, s, f, this),
						p -> new YHFluid(p, this))
				.defaultLang().register();
		folder = "fruitsdelight";
		item = type.build(p -> new SakeBottleItem(fluid, p),  folder + "/",
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

	@Override
	public @Nullable String bottleTextureFolder() {
		return folder;
	}

	@SuppressWarnings("deprecation")
	public Item getContainer() {
		Item ans = item.get().getCraftingRemainingItem();
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

	public static void register() {

	}


}
