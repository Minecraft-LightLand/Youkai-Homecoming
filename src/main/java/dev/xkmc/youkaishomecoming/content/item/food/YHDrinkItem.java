package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class YHDrinkItem extends YHFoodItem {

	public YHDrinkItem(Properties props) {
		super(props, UseAnim.DRINK);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var food = getFoodProperties(stack, player);
		if (food != null && player.hasEffect(YHEffects.SOBER.get())) {
			for (var e : food.getEffects()) {
				if (e.getFirst().getEffect() == YHEffects.SOBER.get()) {
					return InteractionResultHolder.fail(stack);
				}
			}
		}
		return super.use(level, player, hand);
	}

}
