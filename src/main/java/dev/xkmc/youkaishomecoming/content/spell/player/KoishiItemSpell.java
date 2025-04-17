package dev.xkmc.youkaishomecoming.content.spell.player;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.content.spell.mover.AttachedMover;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class KoishiItemSpell extends ItemSpell {

	@Override
	public void start(Player player, @Nullable LivingEntity target) {
		super.start(player, target);
		addTicker(new Lasers());
	}

	@SerialClass
	public static class Lasers extends Ticker<KoishiItemSpell> {

		@Override
		public boolean tick(CardHolder holder, KoishiItemSpell spell) {
			var o0 = DanmakuHelper.getOrientation(new Vec3(0, 0, 1));
			var e = holder.self();
			var forward = RayTraceUtil.getRayTerm(Vec3.ZERO, e.getXRot(), e.getYRot(), 1);
			int n = 8;
			int step = 40;
			int life = 40;
			double angle = Math.PI * 2 / n / step;
			double ver = Mth.atan2(forward.y(), forward.horizontalDistance());
			Vec3 pos = holder.center();
			for (int i = 0; i < n; i++) {
				var laser = holder.prepareLaser(life, pos,
						o0.rotate(angle * tick + Math.PI * 2 / n * i, ver), 40, YHDanmaku.Laser.LASER,
						i % 2 == 0 ? DyeColor.RED : DyeColor.BLUE);
				laser.setupTime(5, 10, step, 10);
				laser.mover = new AttachedMover();
				holder.shoot(laser);
			}
			super.tick(holder, spell);
			return tick > step;
		}
	}

}
