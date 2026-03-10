package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.simibubi.create.Create;
import dev.xkmc.youkaishomecoming.compat.create.CreateFluidHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.registry.ModItems;

public class YHFluidHandler {

	@Nullable
	public static Object of(FluidStack stack) {
		var fluid = stack.getFluid();
		if (fluid instanceof YHFluid f) {
			return f.type;
		}
		if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
			return new BucketFluid(Items.LAVA_BUCKET.getDefaultInstance(), 0xffff0000);
		}
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

}
