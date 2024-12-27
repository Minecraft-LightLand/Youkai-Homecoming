package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.RingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.item.PlayerHolder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class RingSpellForm extends ISpellForm<RingSpellFormData> {

	@SerialClass.SerialField
	private RingSpellFormData data;

	@SerialClass.SerialField
	private int step, tick;

	@Override
	public void init(RingSpellFormData data) {
		this.data = data;
	}

	@Override
	public boolean tick(Player player) {
		if (holder == null)
			holder = new PlayerHolder(player, dir, this, null);
		var o = DanmakuHelper.getOrientation(dir);
		var form = data.form();
		int n = form.branches();
		var rand = holder.random();
		while (tick >= step * form.delay() && step < form.steps()) {
			double s0 = form.steps() <= 1 ? data.speedFirst() : Mth.lerp(1d * step / (form.steps() - 1), data.speedFirst(), data.speedLast());
			for (int i = 0; i < n; i++) {
				double ax = (i - (n - 1) * 0.5) * form.branchAngle() +
						(step - (form.steps() - 1) * 0.5) * form.stepAngle();
				double ay = (step - (form.steps() - 1) * 0.5) * form.stepVerticalAngle();
				double rx = (rand.nextDouble() * 2 - 1) * form.randomizedAngle();
				double ry = (rand.nextDouble() * 2 - 1) * form.randomizedAngle();
				double s1 = s0 * (1 + (rand.nextDouble() * 2 - 1) * data.randomizedSpeed());
				var dir = o.rotateDegrees(ax + rx, ay + ry);
				var e = data.base().prepare(holder, dir, s1);
				holder.shoot(e);
			}
			step++;
		}
		tick++;
		return step >= form.steps() & super.tick(player);
	}

}
