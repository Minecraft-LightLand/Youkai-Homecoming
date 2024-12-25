package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;

@SerialClass
public class LunaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		var target = holder.forward();
		if (tick % 10 == 0 && tick / 10 % 6 < 4) {
			var o = DanmakuHelper.getOrientation(target);
			int offset = holder.random().nextInt(120);
			for (int i = 0; i < 120; i++) {
				if (i / 4 % 2 == 0) continue;
				var dir = o.rotateDegrees((i + offset) * 3);
				var e = holder.prepareDanmaku(40, dir.scale(0.8),
						YHDanmaku.Bullet.BALL, DyeColor.YELLOW);
				holder.shoot(e);
			}
		}
		super.tick(holder);
	}

}
