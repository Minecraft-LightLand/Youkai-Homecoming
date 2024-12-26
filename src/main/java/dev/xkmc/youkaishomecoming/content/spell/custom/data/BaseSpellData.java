package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgRange;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public record BaseSpellData(
		@ArgRange
		YHDanmaku.Bullet bullet,
		@ArgRange
		DyeColor color,
		@ArgRange(base = 4, factor = 32)
		double range,
		@ArgRange
		double randomizedRange
) {

	public static final BaseSpellData DEF = new BaseSpellData(
			YHDanmaku.Bullet.CIRCLE, DyeColor.RED, 80, 0.1
	);

	public ItemDanmakuEntity prepare(CardHolder holder, Vec3 dir, double speed) {
		double r = range * (1 + (holder.random().nextDouble() * 2 - 1) * randomizedRange);
		int time = Math.min(100, speed < 0.01 ? 80 : (int) Math.ceil(r / speed));
		return holder.prepareDanmaku(time, dir.scale(speed), bullet, color);
	}

}
