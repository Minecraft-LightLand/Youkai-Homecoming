package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.fastprojectileapi.entity.GrazingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class DanmakuGrazeEvent extends PlayerEvent {

	private final GrazingEntity e;

	public DanmakuGrazeEvent(Player player, GrazingEntity e) {
		super(player);
		this.e = e;
	}

	public GrazingEntity getDanmaku() {
		return e;
	}

}
