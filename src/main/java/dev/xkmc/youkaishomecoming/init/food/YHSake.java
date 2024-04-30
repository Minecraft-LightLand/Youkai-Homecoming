package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluidType;
import dev.xkmc.youkaishomecoming.content.item.fluid.VirtualFluidBuilder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.List;
import java.util.Locale;

public enum YHSake {
	MIO(FoodType.BOTTLE, 0xffffffff),
	MEAD(FoodType.BOTTLE, 0xffffffff),
	SPARROW_SAKE(FoodType.BAMBOO, 0xffffffff),
	DAIGINJO(FoodType.BOWL, 0xffffffff),
	DASSAI(FoodType.BOWL, 0xffffffff),
	TENGU_TANGO(FoodType.BOWL, 0xffffffff),
	FULL_MOONS_EVE(FoodType.BOWL, 0xffffffff);


	public final int color;

	public final FluidEntry<SakeFluid> fluid;
	public final ItemEntry<Item> item;

	@SafeVarargs
	YHSake(FoodType type, int color, TagKey<Item>... tags) {
		this(type, color, List.of(), tags);
	}

	@SafeVarargs
	YHSake(FoodType type, int color, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.color = color;
		String name = name().toLowerCase(Locale.ROOT);
		fluid = virtualFluid(name, (p, s, f) -> new SakeFluidType(p, s, f, this), p -> new SakeFluid(p, this))
				.defaultLang().register();
		item = type.build("sake/", name, 0, 0, tags, effs, true);
	}

	@SuppressWarnings("deprecation")
	public Item getContainer() {
		Item ans = item.get().getCraftingRemainingItem();
		if (ans == null) return Items.AIR;
		return ans;
	}

	private static <T extends SakeFluid> FluidBuilder<T, L2Registrate> virtualFluid(
			String id, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.entry(id, (c) -> new VirtualFluidBuilder<>(
				YoukaisHomecoming.REGISTRATE, YoukaisHomecoming.REGISTRATE,
				id, c, typeFactory, factory));
	}

	public static void register() {

	}

}
