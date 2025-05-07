package dev.xkmc.fastprojectileapi.render.virtual;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

class DanmakuClientHandler {

	public static @Nullable Entity create(EntityType<?> type) {
		var level = Minecraft.getInstance().level;
		if (level == null) return null;
		return type.create(level);
	}

	public static void add(SimplifiedProjectile sp) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		ClientDanmakuCache.get(level).add(sp);
	}

	public static void erase(int id) {
		var level = Minecraft.getInstance().level;
		if (level == null) return;
		ClientDanmakuCache.get(level).erase(id);
	}

}
