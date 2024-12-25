package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;

@SerialClass
public class SunnySpell extends ActualSpellCard {

	private static final DyeColor[] COLORS = {DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.RED};

	@Override
	public void tick(CardHolder holder) {
		var target = holder.forward();
		if (tick % 10 == 0) {
			int col = tick / 10 % 3;
			var o = DanmakuHelper.getOrientation(target);
			double offset = holder.random().nextDouble() * 360;
			int n = 40;
			for (int i = 0; i < n; i++) {
				var dir = o.rotateDegrees(i * 360d / n + offset).scale(0.5 + col * 0.2);
				var e = holder.prepareDanmaku(80, dir, YHDanmaku.Bullet.BALL, COLORS[col]);
				holder.shoot(e);
			}
		}
		super.tick(holder);
	}

}
