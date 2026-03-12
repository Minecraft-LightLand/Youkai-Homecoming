package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.item.food.YHDrinkItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
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

	public static final FoodProperties NONE = new FoodProperties.Builder().build();

	public static boolean isSlipContainer(ItemStack stack) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		return handler instanceof SlipFluidWrapper;
	}

	public static ItemStack drain(ItemStack stack) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (!(handler instanceof SlipFluidWrapper slip)) return stack;
		slip.drain(50, IFluidHandler.FluidAction.EXECUTE);
		return slip.getContainer();
	}

	public static ItemStack getContentStack(ItemStack stack) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (!(handler instanceof SlipFluidWrapper slip)) return ItemStack.EMPTY;
		if (YHFluidHandler.of(slip.getFluid()) instanceof IYHFluidItem fluid) {
			return fluid.asStack(slip.getFluid().getAmount() / 50);
		}
		return ItemStack.EMPTY;
	}

	public SlipBottleItem(Properties builder) {
		super(builder);
		SlipFluidWrapper.add(this);
	}

	@Override
	public Component getName(ItemStack stack) {
		var fluid = getFluid(stack);
		if (fluid.isEmpty()) return super.getName(stack);
		return YHLangData.FLASK_OF.get(fluid.getHoverName());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		var food = getFoodProperties(stack, player);
		if (food == null || food == NONE || food.effects().isEmpty())
			return InteractionResultHolder.pass(stack);
		return super.use(level, player, hand);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (handler == null) return NONE;
		var fluid = handler.getFluidInTank(0);
		if (fluid.isEmpty()) return NONE;
		if (YHFluidHandler.of(fluid) instanceof IYHFluidItem sake) {
			FoodProperties food;
			if (sake instanceof IFluidPostEffect eff) {
				food = eff.buildFoodProperties();
			} else food = sake.toStack(fluid).getFoodProperties(entity);
			if (food == null) return NONE;
			var builder = new FoodProperties.Builder();
			if (food.canAlwaysEat()) builder.alwaysEdible();
			if (food.nutrition() > 0) {
				int nut = Math.max(1, food.nutrition() / 5);
				builder.nutrition(nut);
				builder.saturationModifier(food.saturation() / nut / 2f);
			}
			for (var e : food.effects()) {
				var ins = e.effect();
				var ans = new EffectBuilder(new MobEffectInstance(ins));
				var inst = ins.getEffect().value().isInstantenous();
				if (ins.getEffect() == YHEffects.DRUNK.get()) {
					int amp = ins.getAmplifier() + 1;
					ans.setDuration(amp * ins.getDuration() / 5);
					ans.setAmplifier(0);
				} else if (!inst) {
					ans.setDuration(ins.getDuration() / 5);
				}
				builder.effect(() -> ans.ins, (inst ? 0.2f : 1) * e.probability());
			}
			return builder.build();
		}
		return NONE;
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
		if (YHFluidHandler.of(fluid) instanceof IFluidPostEffect eff) {
			eff.onConsumeSlip(user);
		}
		return slip.getContainer();
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 10;
	}

	public static FluidStack getFluid(ItemStack stack) {
		var cap = stack.getCapability(Capabilities.FluidHandler.ITEM);
		return cap == null ? FluidStack.EMPTY : cap.getFluidInTank(0);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var fluid = getFluid(stack);
		if (!fluid.isEmpty()) {
			list.add(YHLangData.FLASK_CONTENT.get(fluid.getHoverName().copy().withStyle(ChatFormatting.WHITE)));
			int amount = fluid.getAmount();
			if (amount % 50 == 0 && amount > 0 && amount < 1000)
				list.add(YHLangData.FLASK_USE.get(amount / 50, 20));
			if (getFoodProperties(stack, null) != NONE) {
				list.add(YHLangData.FLASK_INFO_DRINK.get());
			} else {
				list.add(YHLangData.FLASK_INFO_SAUCE.get());
			}
		} else {
			list.add(YHLangData.FLASK_INFO_DRINK.get());
			list.add(YHLangData.FLASK_INFO_SAUCE.get());
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
