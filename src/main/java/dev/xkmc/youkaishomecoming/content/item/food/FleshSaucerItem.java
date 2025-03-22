package dev.xkmc.youkaishomecoming.content.item.food;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FleshSaucerItem extends FoodSaucerItem implements IFleshFoodItem {

	public FleshSaucerItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		return getFleshFoodProps(super.getFoodProperties(stack, entity), entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		appendFleshText(stack, level, list, flag);
	}

	@Override
	public Component getName(ItemStack pStack) {
		return getFleshName(pStack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack ans = super.finishUsingItem(stack, worldIn, consumer);
		if (!(consumer instanceof Player player)) return ans;
		consume(player);
		return ans;
	}

}
