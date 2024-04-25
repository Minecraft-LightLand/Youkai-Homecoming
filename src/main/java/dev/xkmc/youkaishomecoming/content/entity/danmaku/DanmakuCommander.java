package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import net.minecraft.world.phys.Vec3;

public interface DanmakuCommander {

	DanmakuMovement move(int code, int tickCount, Vec3 vec);

}
