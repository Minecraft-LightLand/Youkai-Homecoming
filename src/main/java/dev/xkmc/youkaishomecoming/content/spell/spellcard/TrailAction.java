package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class TrailAction {

	private CardHolder cached;

	public void execute(CardHolder holder, Vec3 pos, Vec3 dir) {

	}

	public void execute(Vec3 pos, Vec3 dir) {
		if (cached != null) {
			execute(cached, pos, dir);
		}
	}

	public void setup(CardHolder holder) {
		cached = holder;
	}

}
