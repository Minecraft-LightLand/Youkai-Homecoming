package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

@SerialClass
public class CompositeMover extends DanmakuMover {

	@SerialClass.SerialField
	private final ArrayList<Entry> list = new ArrayList<>();

	private int total = 0, index = 0;

	public CompositeMover() {

	}

	public CompositeMover add(int duration, DanmakuMover mover) {
		list.add(new Entry(total, mover));
		total += duration;
		return this;
	}

	@Override
	public DanmakuMovement move(int tick, Vec3 prevPos, Vec3 prevVel) {
		if (index < list.size() - 1) {
			if (list.get(index + 1).subtract <= tick) {
				index++;
			}
		}
		var ent = list.get(index);
		return ent.mover.move(tick - ent.subtract, prevPos, prevVel);
	}

	private record Entry(int subtract, DanmakuMover mover) {

	}

}
