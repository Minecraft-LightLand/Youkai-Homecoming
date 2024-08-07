package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.l2core.base.effects.EffectBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
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
		if (food != null) {
			var drunk = player.getEffect(YHEffects.DRUNK);
			boolean isSober = player.hasEffect(YHEffects.SOBER);
			boolean isDrunk = drunk != null;
			boolean banSober = isSober || isDrunk;
			boolean banDrunk = isSober || isDrunk && drunk.getAmplifier() >= 4;
			if (banDrunk || banSober) {
				for (var e : food.effects()) {
					if (e.effect().is(YHEffects.SOBER)) {
						return InteractionResultHolder.fail(stack);
					}
					if (banDrunk && e.effect().is(YHEffects.DRUNK)) {
						return InteractionResultHolder.fail(stack);
					}
				}
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		var food = getFoodProperties(stack, consumer);
		MobEffectInstance ins = consumer.getEffect(YHEffects.DRUNK);
		if (food != null && ins != null) {
			for (var e : food.effects()) {
				if (e.effect().is(YHEffects.DRUNK)) {
					var eff = new EffectBuilder(new MobEffectInstance(e.effect()));
					eff.setDuration(eff.ins.getDuration() + ins.getDuration());
					int old = ins.getAmplifier();
					int amp = eff.ins.getAmplifier() + 1;
					eff.setAmplifier(Math.max(old, Math.min(4, amp + old)));
					consumer.addEffect(eff.ins);
				}
			}
		}
		return super.finishUsingItem(stack, worldIn, consumer);
	}
}
