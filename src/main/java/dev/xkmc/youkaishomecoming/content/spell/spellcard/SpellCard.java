package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuCommander;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class SpellCard implements DanmakuCommander {

	@SerialClass.SerialField
	public int tick;

	public void tick(CardHolder holder) {
		tick++;
	}

	@Override
	public DanmakuMovement move(int code, int tickCount, Vec3 vec) {
		return DanmakuMovement.of(vec);
	}

	public void reset() {
		tick = 0;
	}

}
