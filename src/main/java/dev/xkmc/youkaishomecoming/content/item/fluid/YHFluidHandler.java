package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.simibubi.create.Create;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.compat.create.CreateFluidHandler;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.LinkedHashMap;

public class YHFluidHandler {

	@Nullable
	public static Object of(FluidStack stack) {
		var fluid = stack.getFluid();
		if (fluid instanceof YHFluid f) {
			return f.type;
		}
		var ans = YoukaisHomecoming.FLUID_MAP.getMerged().simpleFluidItems.get(stack.getFluid());
		if (ans != null) return ans;
		if (fluid.is(Tags.Fluids.MILK)) {
			return new Milk(ModItems.MILK_BOTTLE.get().getDefaultInstance(), -1);
		}
		if (ModList.get().isLoaded(Create.ID)) {
			return CreateFluidHandler.of(stack);
		}
		return null;
	}

	public static class BottleFluid implements IYHFluidItem {

		protected final ItemStack stack;
		private final int color;

		public BottleFluid(ItemStack stack, int color) {
			this.stack = stack;
			this.color = color;
		}

		@Override
		public int getColor() {
			return color;
		}

		@Override
		public Item getContainer() {
			return Items.GLASS_BOTTLE;
		}

		@Override
		public ItemStack asStack(int count) {
			return stack.copyWithCount(count);
		}

	}

	public static class BucketFluid implements IYHFluidItem {
		private final ItemStack stack;
		private final int color;

		public BucketFluid(ItemStack stack, int color) {
			this.stack = stack;
			this.color = color;
		}

		@Override
		public int getColor() {
			return color;
		}

		@Override
		public int count() {
			return 1;
		}

		@Override
		public int amount() {
			return 1000;
		}

		@Override
		public Item getContainer() {
			return Items.BUCKET;
		}

		@Override
		public ItemStack asStack(int count) {
			return stack.copyWithCount(count);
		}

	}

	public static class Milk extends BottleFluid implements IFluidPostEffect {

		public Milk(ItemStack stack, int color) {
			super(stack, color);
		}

		@Override
		public void onConsumeSlip(LivingEntity user) {
			((MilkBottleItem) ModItems.MILK_BOTTLE.get()).affectConsumer(
					ModItems.MILK_BOTTLE.get().getDefaultInstance(),
					user.level(), user);
		}

		@Override
		public @Nullable FoodProperties buildFoodProperties() {
			return new FoodProperties.Builder().alwaysEat().build();
		}

	}

	@SerialClass
	public static class Config extends BaseConfig {

		@SerialClass.SerialField
		@ConfigCollect(CollectType.MAP_OVERWRITE)
		public final LinkedHashMap<Fluid, FluidItem> simpleFluidItems = new LinkedHashMap<>();

		public record FluidItem(
				Item item, int amount, int color
		) implements IYHFluidItem, IFluidPostEffect {

			@Override
			public int getColor() {
				return color;
			}

			@Override
			public Item getContainer() {
				return item.getCraftingRemainingItem(item.getDefaultInstance()).getItem();
			}

			@Override
			public ItemStack asStack(int count) {
				return new ItemStack(item, count);
			}

			@Override
			public void onConsumeSlip(LivingEntity user) {

			}

			@Override
			public @Nullable FoodProperties buildFoodProperties() {
				return item.getFoodProperties(item.getDefaultInstance(), null);
			}

			@Override
			public int count() {
				return 1000 / amount;
			}

		}

	}

}
