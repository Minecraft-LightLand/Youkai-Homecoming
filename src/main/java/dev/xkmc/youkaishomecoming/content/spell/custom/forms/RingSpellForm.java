package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.RingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import net.minecraft.util.Mth;

@SerialClass
public class RingSpellForm extends ISpellForm<RingSpellFormData> {

	@SerialClass.SerialField
	private RingSpellFormData data;

	@SerialClass.SerialField
	private int step;

	@Override
	public void init(RingSpellFormData data) {
		this.data = data;
		this.step = 0;
	}

	@Override
	public boolean tick(CardHolder holder, CustomItemSpell card) {
		var o = DanmakuHelper.getOrientation(card.dir);
		int n = data.branches();
		var rand = holder.random();
		while (tick >= step * data.delay() && step < data.steps()) {
			double s0 = data.steps() <= 1 ? data.speedFirst() : Mth.lerp(1d * step / (data.steps() - 1), data.speedFirst(), data.speedLast());
			for (int i = 0; i < n; i++) {
				double ax = (i - (n - 1) * 0.5) * data.branchAngle() +
						(step - (data.steps() - 1) * 0.5) * data.stepAngle();
				double ay = (step - (data.steps() - 1) * 0.5) * data.stepVerticalAngle();
				double rx = (rand.nextDouble() * 2 - 1) * data.randomizedAngle();
				double ry = (rand.nextDouble() * 2 - 1) * data.randomizedAngle();
				double s1 = s0 * (1 + (rand.nextDouble() * 2 - 1) * data.randomizedSpeed());
				var dir = o.rotateDegrees(ax + rx, ay + ry);
				var e = data.base().prepare(holder, dir, s1);
				holder.shoot(e);
			}
			step++;
		}
		super.tick(holder, card);
		return step >= data.steps();
	}

}
