package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

public class TLMCompat {


	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof GeneralYoukaiEntity e && event.getEntity().isCreative()) {
			if (event.getItemStack().getItem() instanceof ItemGarageKit) {
				if (!event.getTarget().level().isClientSide()) {
					String id = ItemGarageKit.getMaidData(event.getItemStack()).getString("ModelId");
					e.spellCard = new SpellCardWrapper();
					e.spellCard.modelId = id;
					TouhouSpellCards.setSpell(e, id);
					e.syncModel();
				}
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
		var state = event.getLevel().getBlockState(event.getPos());
		var te = event.getLevel().getBlockEntity(event.getPos());
		if (state.is(InitBlocks.GARAGE_KIT.get()) && event.getEntity().isCreative()) {
			if (event.getItemStack().getItem() instanceof DanmakuItem) {
				if (event.getLevel() instanceof ServerLevel sl && te instanceof TileEntityGarageKit kit) {
					BlockPos pos = event.getPos();
					BlockPos blockpos1;
					Direction dir = event.getHitVec().getDirection();
					if (state.getCollisionShape(sl, pos).isEmpty()) {
						blockpos1 = pos;
					} else {
						blockpos1 = pos.relative(dir);
					}
					var entitytype = YHEntities.GENERAL_YOUKAI.get();
					var e = entitytype.create(sl, null, null, event.getPos(),
							MobSpawnType.SPAWN_EGG, true,
							!Objects.equals(pos, blockpos1) && dir == Direction.UP);
					if (e == null) return;
					String id = kit.getExtraData().getString("ModelId");
					e.spellCard = new SpellCardWrapper();
					e.spellCard.modelId = id;
					TouhouSpellCards.setSpell(e, id);
					sl.addFreshEntity(e);
					sl.gameEvent(event.getEntity(), GameEvent.ENTITY_PLACE, pos);
				}
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

}
