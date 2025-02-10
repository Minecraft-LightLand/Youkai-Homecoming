package dev.xkmc.youkaishomecoming.content.spell.mover;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;

import java.util.ArrayList;

@SerialClass
public class CompositeMover extends DanmakuMover {

	@SerialClass.SerialField
	private final ArrayList<Entry> list = new ArrayList<>();

	@SerialClass.SerialField
	private int total = 0, index = 0;

	public CompositeMover() {

	}

	public CompositeMover add(int duration, DanmakuMover mover) {
		list.add(new Entry(total, mover));
		total += duration;
		return this;
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		if (index < list.size() - 1) {
			if (list.get(index + 1).subtract <= info.tick()) {
				index++;
			}
		}
		var ent = list.get(index);
		return ent.mover.move(info.offsetTime(-ent.subtract));
	}

	public void addEnd() {
		var mover = list.get(list.size() - 1);
		if (mover.mover instanceof RectMover rect) {
			add(20, rect.toStatic(total - mover.subtract()));
		}
	}

	public record Entry(int subtract, DanmakuMover mover) {

	}

}
