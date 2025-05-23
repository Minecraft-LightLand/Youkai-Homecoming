package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.spell;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.DanmakuHelper;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;

@SerialClass
public class SmallFairySpell extends ActualSpellCard {

	@SerialClass.SerialField
	protected DyeColor color = DyeColor.RED;

	public SmallFairySpell init(DyeColor col) {
		this.color = col;
		return this;
	}

	@Override
	public void tick(CardHolder holder) {
		if (tick % 20 == 0) {
			int step = tick / 20;
			if (step % 5 < 3) {
				int n = YHModConfig.COMMON.smallFairyStrength.get();
				var r = holder.random();
				var o = DanmakuHelper.getOrientation(holder.forward());
				for (int i = -n; i <= n; i++) {
					var dir = o.rotateDegrees(i * 15);
					for (int j = 0; j <= n; j++) {
						double v = 0.5 + j * 0.2 - n * 0.1;
						var vel = dir.scale(v);
						int life = (int) (40 / v * (1 + r.nextDouble() * 0.5));
						var e = holder.prepareDanmaku(life, vel,
								YHDanmaku.Bullet.BALL, color);
						holder.shoot(e);
					}
				}
			}
		}
		super.tick(holder);
	}

}
