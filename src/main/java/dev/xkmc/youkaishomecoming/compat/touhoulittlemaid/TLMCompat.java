package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.InteractionResult;
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
	}

}
