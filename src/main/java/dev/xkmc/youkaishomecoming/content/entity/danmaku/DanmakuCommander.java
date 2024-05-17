package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import net.minecraft.world.phys.Vec3;

public interface DanmakuCommander {

	ProjectileMovement move(int code, int tickCount, Vec3 vec);

}
