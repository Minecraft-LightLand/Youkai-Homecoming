package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface CardHolder {

	Vec3 center();

	Vec3 forward();

	@Nullable
	LivingEntity target();

	RandomSource random();

	ItemDanmakuEntity prepare(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color);

	void shoot(ItemDanmakuEntity danmaku);

}
