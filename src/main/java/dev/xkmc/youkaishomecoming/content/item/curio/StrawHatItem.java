package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrawHatItem extends Item {

	public StrawHatItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!(target instanceof Frog frog))
			return InteractionResult.PASS;
		if (!player.hasEffect(YHEffects.YOUKAIFYING.get()) && !player.hasEffect(YHEffects.YOUKAIFIED.get()))
			return InteractionResult.FAIL;
		if (!FrogGodCapability.HOLDER.isProper(frog))
			return InteractionResult.FAIL;
		var cap = FrogGodCapability.HOLDER.get(frog);
		if (cap.hasHat) {
			return InteractionResult.FAIL;
		} else {
			if (!player.level().isClientSide()) {
				cap.hasHat = true;
				cap.syncToClient(frog);
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		Player player = FleshFoodItem.getPlayer();
		if (player == null) return;
		boolean obtain = false;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			obtain = true;
		} else if (player.hasEffect(YHEffects.YOUKAIFYING.get())) {
			obtain = true;
		}
		Component obt;
		if (obtain) {
			var fying = Component.translatable(YHEffects.YOUKAIFYING.get().getDescriptionId());
			var fied = Component.translatable(YHEffects.YOUKAIFIED.get().getDescriptionId());
			obt = YHLangData.USAGE_STRAW_HAT.get(fying, fied);
		} else {
			obt = YHLangData.UNKNOWN.get();
		}
		list.add(YHLangData.USAGE.get().append(obt));
	}

}
