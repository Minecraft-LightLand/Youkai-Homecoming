package dev.xkmc.youkaishomecoming.compat.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import dev.xkmc.youkaishomecoming.content.item.fluid.IFluidPostEffect;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.HotCocoaItem;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

public class CreateFluidHandler {

	public static @Nullable Object of(FluidStack stack) {
		if (stack.getFluid() == AllFluids.HONEY.getSource()) {
			return new Honey(Items.HONEY_BOTTLE.getDefaultInstance(), 0xFFFED32E);
		}
		if (stack.getFluid() == AllFluids.CHOCOLATE.getSource()) {
			return new Choco(AllFluids.CHOCOLATE.getBucket().get().getDefaultInstance(), 0xFFF98965);
		}
		if (stack.getFluid() == AllFluids.POTION.getSource()) {
			var ans = PotionFluidHandler.fillBottle(Items.GLASS_BOTTLE.getDefaultInstance(), stack);
			var potion = ans.get(DataComponents.POTION_CONTENTS);
			return new Potion(ans, potion == null ? -1 : potion.getColor());
		}
		return null;
	}

	public static class Honey extends YHFluidHandler.BottleFluid implements IFluidPostEffect {

		public Honey(ItemStack stack, int color) {
			super(stack, color);
		}

		@Override
		public void onConsumeSlip(LivingEntity user) {
			user.removeEffect(MobEffects.POISON);
		}

		@Override
		public @Nullable FoodProperties buildFoodProperties() {
			return new FoodProperties.Builder().alwaysEdible().build();
		}

	}

	public static class Choco extends YHFluidHandler.BucketFluid implements IFluidPostEffect {

		public Choco(ItemStack stack, int color) {
			super(stack, color);
		}

		@Override
		public void onConsumeSlip(LivingEntity user) {
			((HotCocoaItem) ModItems.HOT_COCOA.get()).affectConsumer(
					ModItems.HOT_COCOA.get().getDefaultInstance(),
					user.level(), user);
		}

		@Override
		public @Nullable FoodProperties buildFoodProperties() {
			return new FoodProperties.Builder().alwaysEat().build();
		}

		@Override
		public void appendHoverText(List<Component> list) {
			list.add(Component.translatable("farmersdelight.tooltip.hot_cocoa").withStyle(ChatFormatting.BLUE));
		}

	}

	public static class Potion extends YHFluidHandler.BottleFluid implements IFluidPostEffect {

		public Potion(ItemStack stack, int color) {
			super(stack, color);
		}

		@Override
		public void onConsumeSlip(LivingEntity user) {
		}

		@Override
		public @Nullable FoodProperties buildFoodProperties() {
			var potion = stack.get(DataComponents.POTION_CONTENTS);
			if (potion == null) return null;
			var ans = new FoodProperties.Builder().alwaysEdible();
			for (var e : potion.getAllEffects()) {
				ans.effect(() -> e, 1);
			}
			return ans.build();
		}

	}

}
