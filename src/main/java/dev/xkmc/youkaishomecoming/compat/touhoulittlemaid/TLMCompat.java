package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import dev.xkmc.youkaishomecoming.content.entity.boss.MystiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.boss.RemiliaEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TLMCompat {

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof GeneralYoukaiEntity e && event.getEntity().isCreative()) {
			if (event.getItemStack().getItem() instanceof ItemGarageKit) {
				if (!event.getTarget().level().isClientSide()) {
					String id = ItemGarageKit.getMaidData(event.getItemStack()).getString("ModelId");
					TouhouSpellCards.setSpell(e, id);
				}
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
		if (event.getTarget() instanceof Bat bat && event.getItemStack().is(YHFood.SCARLET_DEVIL_CAKE.item.get())) {
			if (!event.getTarget().level().isClientSide()) {
				var remilia = new RemiliaEntity(YHEntities.REMILIA.get(), bat.level());
				remilia.moveTo(bat.position());
				remilia.initSpellCard();
				bat.level().addFreshEntity(remilia);
				bat.discard();
			}
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
		if (event.getTarget() instanceof Parrot parrot && event.getItemStack().is(YHFood.RAW_LAMPREY.item.get())) {
			if (!event.getTarget().level().isClientSide()) {
				var mystia = new MystiaEntity(YHEntities.MYSTIA.get(), parrot.level());
				mystia.moveTo(parrot.position());
				mystia.initSpellCard();
				parrot.level().addFreshEntity(mystia);
				parrot.discard();
			}
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}

}
