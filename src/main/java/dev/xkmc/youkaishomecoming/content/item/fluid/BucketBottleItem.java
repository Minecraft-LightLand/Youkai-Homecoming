package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.item.food.TooltipUtil;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class BucketBottleItem extends BlockItem {

	protected final IYHFluidHolder fluid;

	public BucketBottleItem(Block block, Properties properties, IYHFluidHolder fluid) {
		super(block, properties);
		this.fluid = fluid;
		SlipFluidWrapper.add(this);
	}

	@Override
	public InteractionResult place(BlockPlaceContext ctx) {
		if (ctx.getPlayer() != null && !ctx.getPlayer().isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		return super.place(ctx);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		var stack = player.getItemInHand(hand);
		var food = getFoodProperties(stack, player);
		if (food == null || food == SlipBottleItem.NONE || food.effects().isEmpty())
			return InteractionResultHolder.pass(stack);
		return super.use(level, player, hand);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		var other = stack.getCount() == 1 ? ItemStack.EMPTY : stack.split(stack.getCount() - 1);
		var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (!(handler instanceof SlipFluidWrapper slip)) return stack;
		var fluid = slip.getFluid();
		super.finishUsingItem(stack, level, user);
		slip.getContainer().setCount(1);
		slip.setFluid(fluid);
		slip.drain(50, IFluidHandler.FluidAction.EXECUTE);
		if (!other.isEmpty()) {
			if (user instanceof Player player) {
				player.getInventory().placeItemBackInInventory(other);
			} else {
				user.spawnAtLocation(other);
			}
			stack.setCount(0);
		}
		return slip.getContainer();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.PLACE.get());
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			TooltipUtil.getFoodEffects(stack, list);
		super.appendHoverText(stack, level, list, flag);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var food = fluid.asStack(1).getFoodProperties(entity);
		if (food == null || food == SlipBottleItem.NONE) return SlipBottleItem.NONE;
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

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 10;
	}

}
