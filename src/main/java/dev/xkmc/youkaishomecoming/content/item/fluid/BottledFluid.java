package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BottledFluid<T extends SakeBottleItem> implements IYHFluidHolder, ItemLike {

	public static final ResourceLocation WATER_FLOW = new ResourceLocation("block/water_flow");
	public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");

	public static final ResourceLocation SOLID_FLOW = new ResourceLocation(YoukaisHomecoming.MODID, "block/fluid/water_flow");
	public static final ResourceLocation SOLID_STILL = new ResourceLocation(YoukaisHomecoming.MODID, "block/fluid/water_still");

	public static <T extends YHFluid> FluidBuilder<T, L2Registrate> virtualFluid(
			String id, ResourceLocation flow, ResourceLocation still,
			FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
		return YoukaisHomecoming.REGISTRATE.entry(id, (c) -> new VirtualFluidBuilder<>(
				YoukaisHomecoming.REGISTRATE, YoukaisHomecoming.REGISTRATE,
				id, c, still, flow, typeFactory, factory));
	}

	public static <T extends YHFluid> FluidBuilder<T, L2Registrate> water(
			String id, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
		return virtualFluid(id, WATER_FLOW, WATER_STILL, typeFactory, factory);
	}

	private final String id, path;
	private final int color;
	private final Supplier<Item> container;

	public final ItemEntry<T> item;
	public final FluidEntry<YHFluid> fluid;

	protected @Nullable BottledDrinkSet set;

	public BottledFluid(String id, int color, Supplier<Item> container, String path, NonNullBiFunction<Supplier<YHFluid>, Item.Properties, T> factory) {
		this(id, id + "_bottle", SOLID_FLOW, SOLID_STILL, color, container, path, factory);
	}

	public BottledFluid(String id, String itemId, String fluidTex, Supplier<Item> container, String path, NonNullBiFunction<Supplier<YHFluid>, Item.Properties, T> factory) {
		this(id, itemId,
				YoukaisHomecoming.loc("block/fluid/" + fluidTex + "_flow"),
				YoukaisHomecoming.loc("block/fluid/" + fluidTex + "_still"),
				-1, container, path, factory);
	}

	public BottledFluid(String id, String itemId, ResourceLocation flow, ResourceLocation still, int color, Supplier<Item> container, String path, NonNullBiFunction<Supplier<YHFluid>, Item.Properties, T> factory) {
		this.color = color;
		this.container = container;
		this.id = id;
		this.path = path;

		fluid = virtualFluid(id, flow, still, (p, s, f) -> new YHFluidType(p, s, f, this), p -> new YHFluid(p, this))
				.defaultLang().register();

		item = YoukaisHomecoming.REGISTRATE
				.item(itemId, p -> factory.apply(fluid::getSource, p.craftRemainder(container.get())))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/" + path + "/" + ctx.getName())))
				.register();

	}

	public BottledFluid<T> bottle() {
		set = new BottledDrinkSet(this, id + "_flask", path);
		return this;
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
	public Item asItem() {
		return item.asItem();
	}

	@Override
	public YHFluid source() {
		return fluid.getSource();
	}

	public ResourceLocation getId() {
		return item.getId();
	}

	@Nullable
	public String bottleTextureFolder() {
		return path;
	}

	@Override
	public @Nullable BottleTexture bottleSet() {
		return set;
	}

}
