package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterData;
import dev.xkmc.youkaishomecoming.content.spell.shooter.ShooterEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface CardHolder {

	Vec3 center();

	Vec3 forward();

	@Nullable
	Vec3 target();

	RandomSource random();

	ItemDanmakuEntity prepareDanmaku(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color);

	ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, YHDanmaku.Laser type, DyeColor color);

	void shoot(Entity danmaku);

	LivingEntity self();

	default DamageSource getDanmakuDamageSource(IYHDanmaku danmaku) {
		return YHDamageTypes.danmaku(danmaku);
	}

	@Nullable
	Vec3 targetVelocity();

	ShooterEntity prepareShooter(ShooterData data, SpellCard spell);

	@Nullable
	LivingEntity targetEntity();

	float getDamage(YHDanmaku.IDanmakuType type);

}
