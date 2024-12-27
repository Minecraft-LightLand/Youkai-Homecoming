package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.HomingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.item.PlayerHolder;
import dev.xkmc.youkaishomecoming.content.spell.mover.CompositeMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.RectMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class HomingSpellForm extends ISpellForm<HomingSpellFormData> {

	@SerialClass.SerialField
	private HomingSpellFormData data;

	@SerialClass.SerialField
	private int step, tick;

	@Override
	public void init(HomingSpellFormData data) {
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
			for (int i = 0; i < n; i++) {
				double ax = (i - (n - 1) * 0.5) * form.branchAngle() +
						(step - (form.steps() - 1) * 0.5) * form.stepAngle();
				double ay = (step - (form.steps() - 1) * 0.5) * form.stepVerticalAngle();
				double rx = (rand.nextDouble() * 2 - 1) * form.randomizedAngle();
				double ry = (rand.nextDouble() * 2 - 1) * form.randomizedAngle();
				var dir = o.rotateDegrees(ax + rx, ay + ry);
				int time = data.turnTime();
				var e = data.base().prepare(holder, dir, data.speed(), time);
				e.mover = new RectMover(holder.center(),
						dir.scale(data.speed() * 2),
						dir.scale(-data.speed() * 2 / time));
				holder.shoot(e);
				addTicker(new Stage().setup(holder.center().add(dir.scale(data.speed() * time)), time));
			}
			step++;
		}
		tick++;
		return step >= form.steps() & super.tick(player);
	}

	@SerialClass
	public static class Stage extends Ticker<HomingSpellForm> {

		@SerialClass.SerialField
		private Vec3 pos = Vec3.ZERO;

		private Stage setup(Vec3 pos, int time) {
			this.pos = pos;
			this.tick -= time;
			return this;
		}

		@Override
		public boolean tick(CardHolder holder, HomingSpellForm card) {
			super.tick(holder, card);
			if (tick <= 0) return false;
			var data = card.data;
			var dir = card.targetPos.subtract(pos).normalize();
			var e = data.base().prepare(holder, dir, data.speed());
			e.setPos(pos);
			var mover = new CompositeMover();
			int time = data.turnTime();
			mover.add(time, new RectMover(pos, Vec3.ZERO, dir.scale(data.speed() * 2 / time)));
			mover.add(20, new RectMover(pos.add(dir.scale(data.speed() * time)), dir.scale(data.speed()), Vec3.ZERO));
			e.mover = mover;
			holder.shoot(e);
			return true;
		}

	}

}
