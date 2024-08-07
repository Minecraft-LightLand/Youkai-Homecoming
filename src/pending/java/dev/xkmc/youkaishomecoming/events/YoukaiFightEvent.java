package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class YoukaiFightEvent extends Event {

	public final YoukaiEntity youkai;
	public final LivingEntity target;

	public YoukaiFightEvent(YoukaiEntity youkai, LivingEntity target) {
		this.youkai = youkai;
		this.target = target;
	}

}
