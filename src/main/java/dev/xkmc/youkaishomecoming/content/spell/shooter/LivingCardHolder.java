package dev.xkmc.youkaishomecoming.content.spell.shooter;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface LivingCardHolder extends CardHolder {

	LivingEntity self();

	LivingEntity shooter();

	@Nullable
	LivingEntity targetEntity();

	double getDamage();

	@Override
	default Vec3 center() {
		return self().position().add(0, self().getBbHeight() / 2, 0);
	}

	@Override
	default Vec3 forward() {
		var target = target();
		if (target != null) {
			return target.subtract(center()).normalize();
		}
		return self().getForward();
	}

	@Override
	default @Nullable Vec3 target() {
		var le = targetEntity();
		if (le == null) return null;
		return le.position().add(0, le.getBbHeight() / 2, 0);
	}

	@Override
	default @Nullable Vec3 targetVelocity() {
		var le = targetEntity();
		if (le == null) return null;
		return le.getDeltaMovement();
	}

	@Override
	default RandomSource random() {
		return self().getRandom();
	}

	@Override
	default ItemDanmakuEntity prepareDanmaku(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), shooter(), self().level());
		danmaku.setPos(center());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup((float) getDamage(),
				life, true, true, vec);
		return danmaku;
	}

	@Override
	default ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, YHDanmaku.Laser type, DyeColor color) {
		ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), shooter(), self().level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup((float) getDamage(),
				life, len, true, vec);
		danmaku.setPos(pos);
		return danmaku;
	}

	@Override
	default void shoot(SimplifiedProjectile danmaku) {
		self().level().addFreshEntity(danmaku);
	}


}
