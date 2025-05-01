package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.function.Supplier;

public class BottledFluid<T extends SakeBottleItem> implements IYHFluidHolder {

	public static final ResourceLocation WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
	public static final ResourceLocation WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");

	public static final ResourceLocation SOLID_FLOW = YoukaisHomecoming.loc("block/water_flow");
	public static final ResourceLocation SOLID_STILL = YoukaisHomecoming.loc("block/water_still");

	public static <T extends YHFluid> FluidBuilder<T, L2Registrate> virtualFluid(
			L2Registrate reg,
			String id, ResourceLocation flow, ResourceLocation still,
			FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return reg.entry(id, (c) -> new VirtualFluidBuilder<>(reg, reg, id, c, still, flow, typeFactory, factory));
	}

	public static <T extends YHFluid> FluidBuilder<T, L2Registrate> water(
			L2Registrate reg, String id, FluidBuilder.FluidTypeFactory typeFactory,
			NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return virtualFluid(reg, id, WATER_FLOW, WATER_STILL, typeFactory, factory);
	}

	public static <T extends YHFluid> FluidBuilder<T, L2Registrate> solid(
			L2Registrate reg, String id, FluidBuilder.FluidTypeFactory typeFactory,
			NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return virtualFluid(reg, id, SOLID_FLOW, SOLID_STILL, typeFactory, factory);
	}

	private final int color;
	private final Supplier<Item> container;

	public final ItemEntry<T> item;
	public final FluidEntry<YHFluid> fluid;

	public BottledFluid(L2Registrate reg, String id, int color, Supplier<Item> container, String path, NonNullBiFunction<Supplier<YHFluid>, Item.Properties, T> factory) {
		this.color = color;
		this.container = container;

		fluid = solid(reg, id, (p, s, f) -> new YHFluidType(p, s, f, this), p -> new YHFluid(p, this))
				.defaultLang().register();

		item = reg.item(id + "_bottle", p -> factory.apply(fluid, p.craftRemainder(container.get())))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/" + path + "/" + ctx.getName())))
				.register();

	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public Item getContainer() {
		return container.get();
	}

	@Override
	public ItemStack asStack(int count) {
		return item.asStack(count);
	}

	@Override
	public ItemEntry<?> item() {
		return item;
	}

	@Override
	public FluidEntry<? extends YHFluid> fluid() {
		return fluid;
	}

}
