package dev.xkmc.youkaishomecoming.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class DanmakuLastHitEvent extends PlayerEvent {

	private final LivingEntity e;

	public DanmakuLastHitEvent(Player player, LivingEntity e) {
		super(player);
		this.e = e;
	}

	public LivingEntity getAttacker() {
		return e;
	}

}
