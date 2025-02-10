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

public record ShooterHolder(
		LivingEntity entity, float damage
) implements CardHolder {

	@Override
	public Vec3 center() {
		return entity.position().add(0, entity.getBbHeight() / 2, 0);
	}

	@Override
	public Vec3 forward() {
		return entity.getForward();
	}

	@Override
	public @Nullable Vec3 target() {
		return null;
	}

	@Override
	public RandomSource random() {
		return entity.level().random;
	}

	@Override
	public ItemDanmakuEntity prepareDanmaku(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), entity, entity.level());
		danmaku.setPos(center());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(damage, life, true, true, vec);
		return danmaku;
	}

	@Override
	public ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, YHDanmaku.Laser type, DyeColor color) {
		ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), entity, entity.level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(damage, life, len, true, vec);
		danmaku.setPos(pos);
		return danmaku;
	}

	@Override
	public void shoot(SimplifiedProjectile danmaku) {
		entity.level().addFreshEntity(danmaku);
	}

	@Override
	public LivingEntity self() {
		return entity;
	}

	@Override
	public @Nullable Vec3 targetVelocity() {
		return null;
	}

}
