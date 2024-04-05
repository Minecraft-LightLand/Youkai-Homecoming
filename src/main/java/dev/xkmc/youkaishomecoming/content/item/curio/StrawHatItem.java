package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

}
