package dev.xkmc.youkaishomecoming.content.item.fluid;

import dev.xkmc.l2library.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.content.item.food.YHFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.food.YHDrink;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class BucketBottleItem extends BlockItem {

	protected final YHDrink fluid;

	public BucketBottleItem(Block block, Properties properties, YHDrink fluid) {
		super(block, properties);
		this.fluid = fluid;
	}

	@Override
	public InteractionResult place(BlockPlaceContext ctx) {
		if (ctx.getPlayer() != null && !ctx.getPlayer().isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		return super.place(ctx);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
		var other = stack.getCount() == 1 ? ItemStack.EMPTY : stack.split(stack.getCount() - 1);
		var handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
		if (handler.isEmpty() || !(handler.get() instanceof SlipFluidWrapper slip)) return stack;
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
		}
		return slip.getContainer();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.PLACE.get());
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			YHFoodItem.getFoodEffects(stack, list);
		super.appendHoverText(stack, level, list, flag);
	}

	@Override
	public boolean isEdible() {
		return true;
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var food = fluid.item.asStack().getFoodProperties(entity);
		assert food != null;
		var builder = new FoodProperties.Builder();
		if (food.canAlwaysEat()) builder.alwaysEat();
		for (var e : food.getEffects()) {
			var ins = e.getFirst();
			var ans = new EffectBuilder(ins);
			if (ins.getEffect() == YHEffects.DRUNK.get()) {
				int amp = ins.getAmplifier() + 1;
				ans.setDuration(amp * ins.getDuration() / 5);
				ans.setAmplifier(0);
			} else {
				ans.setDuration(ins.getDuration() / 5);
			}
			builder.effect(() -> ans.ins, e.getSecond());
		}
		return builder.build();

	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if (this instanceof BucketBottleItem)
			return new SlipFluidWrapper(stack);
		else return super.initCapabilities(stack, nbt);
	}

}
