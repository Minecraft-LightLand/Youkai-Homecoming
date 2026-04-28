package dev.xkmc.youkaishomecoming.content.item.food;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

public class YHFoodItem extends Item {

	private final UseAnim anim;

	public YHFoodItem(Properties props, UseAnim anim) {
		super(props);
		this.anim = anim;
	}

	public YHFoodItem(Properties props) {
		this(props, UseAnim.EAT);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return anim;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
		if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get())
			TooltipUtil.getFoodEffects(stack, list);
	}

}
