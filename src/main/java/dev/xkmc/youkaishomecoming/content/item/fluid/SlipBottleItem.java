package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlipBottleItem extends YHDrinkItem {

	public SlipBottleItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		if (getFoodProperties(stack, player) == null)
			return InteractionResultHolder.pass(stack);
		return super.use(level, player, hand);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (handler == null) return null;
		var fluid = handler.getFluidInTank(0);
		if (fluid.isEmpty()) return null;
		if (fluid.getFluid() instanceof YHFluid sake && sake.type instanceof YHDrink type) {
			var food = type.item.asStack().getFoodProperties(entity);
			if (food == null) return null;
			var builder = new FoodProperties.Builder();
			if (food.canAlwaysEat()) builder.alwaysEdible();
			for (var e : food.effects()) {
				var ins = e.effect();
				var ans = new EffectBuilder(ins);
				if (ins.getEffect() == YHEffects.DRUNK.get()) {
					int amp = ins.getAmplifier() + 1;
					ans.setDuration(amp * ins.getDuration() / 5);
					ans.setAmplifier(0);
				} else {
					ans.setDuration(ins.getDuration() / 5);
				}
				builder.effect(() -> ans.ins, e.probability());
			}
			return builder.build();
		}
		return null;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (!(handler instanceof SlipFluidWrapper slip)) return stack;
		var fluid = slip.getFluid();
		super.finishUsingItem(stack, level, user);
		slip.getContainer().setCount(1);
		slip.setFluid(fluid);
		slip.drain(50, IFluidHandler.FluidAction.EXECUTE);
		return slip.getContainer();
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 10;
	}

	public static FluidStack getFluid(ItemStack stack) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (handler == null) return FluidStack.EMPTY;
		return handler.getFluidInTank(0);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var fluid = getFluid(stack);
		if (!fluid.isEmpty() && fluid.getFluid() instanceof YHFluid sake && sake.type instanceof YHDrink type) {
			list.add(type.item.get().getDescription());
		}
		super.appendHoverText(stack, level, list, flag);
	}

	public static int color(ItemStack stack, int layer) {
		if (layer != 1) return -1;
		var fluid = getFluid(stack);
		if (fluid.isEmpty()) return -1;
		return FluidColorHelper.getColor(fluid);
	}

	public static float texture(ItemStack stack) {
		var fluid = getFluid(stack);
		if (fluid.isEmpty()) return 0;
		return 1;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !getFluid(stack).isEmpty();
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return 13 * getFluid(stack).getAmount() / 1000;
	}

	public int getBarColor(ItemStack stack) {
		return -1;
	}

}
