package dev.xkmc.youkaishomecoming.content.spell.game.yukari;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCard;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class LightHole extends SpellCard {

	@SerialClass.SerialField
	private Vec3 pos, forward;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 100;
		if (tick < interval) return;
		if (tick % interval == 0 || forward == null || pos == null) {
			forward = holder.forward();
			forward = forward.multiply(1, 0.5, 1).normalize();
			pos = holder.center();
		}
		int step = tick % interval;
		if (step > 60) return;
		var ori = DanmakuHelper.getOrientation(forward);
		var r = holder.random();
		if (step == 0) {
			holder.shoot(holder.prepareLaser(100, pos, ori.rotateDegrees(-45), 40, YHDanmaku.Laser.LASER, DyeColor.RED));
			holder.shoot(holder.prepareLaser(100, pos, ori.rotateDegrees(45), 40, YHDanmaku.Laser.LASER, DyeColor.BLUE));
		}
		addLaserBeams(holder, pos, ori.rotateDegrees(-45), 1 + step * 0.5, r.nextDouble(), DyeColor.RED);
		addLaserBeams(holder, pos, ori.rotateDegrees(45), 1 + step * 0.5, r.nextDouble(), DyeColor.BLUE);
		if (step == 20) {
			shootGroup(holder, DyeColor.RED);
		}
		if (step == 40) {
			shootGroup(holder, DyeColor.BLUE);
		}
	}

	private void shootGroup(CardHolder holder, DyeColor color) {
		double speed = 1;
		double dv = 0.5;
		double dev = 30;
		int n0 = 5;
		int n1 = 50;
		int life = 60;
		int dl = 20;

		var forward = holder.forward();
		var rand = holder.random();
		var ori = DanmakuHelper.getOrientation(forward);

		for (int i = 0; i < n0; i++) {
			double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
			double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
			double sp = speed - dv / n0 * i;
			var vec = ori.rotateDegrees(d0, d1).scale(sp);
			int lf = life + rand.nextInt(dl);
			holder.shoot(holder.prepareDanmaku(lf, vec, YHDanmaku.Bullet.BUBBLE, color));
		}
		for (int i = 0; i < n1; i++) {
			double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
			double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
			double sp = speed - dv / n1 * i;
			var vec = ori.rotateDegrees(d0, d1).scale(sp);
			int lf = life + rand.nextInt(dl);
			holder.shoot(holder.prepareDanmaku(lf, vec, YHDanmaku.Bullet.MENTOS, color));
		}
	}

	private void addLaserBeams(CardHolder holder, Vec3 pos, Vec3 dir, double step, double r, DyeColor color) {
		Vec3 p = pos.add(dir.scale(step));
		var ori = DanmakuHelper.getOrientation(dir).rotate(Math.PI / 2, r * Math.PI * 2);
		holder.shoot(holder.prepareLaser(100, p, ori, 40, YHDanmaku.Laser.LASER, color));
	}

}
