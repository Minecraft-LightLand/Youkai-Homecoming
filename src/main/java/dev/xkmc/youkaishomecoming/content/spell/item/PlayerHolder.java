package dev.xkmc.youkaishomecoming.content.spell.item;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemLaserEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

record PlayerHolder(
		Player player, Vec3 dir, ItemSpell spell, @Nullable LivingEntity targeted
) implements CardHolder {

	@Override
	public LivingEntity self() {
		return player;
	}

	@Override
	public Vec3 center() {
		return player.position().add(0, player.getBbHeight() / 2, 0);
	}

	@Override
	public Vec3 forward() {
		var target = target();
		if (target == null) return spell.dir;
		return target.subtract(center()).normalize();
	}

	@Override
	public @Nullable Vec3 target() {
		return spell.targetPos;
	}

	@Override
	public @Nullable Vec3 targetVelocity() {
		var le = targeted;
		if (le == null) return null;
		return le.getDeltaMovement();
	}

	@Override
	public RandomSource random() {
		return player.getRandom();
	}

	@Override
	public ItemDanmakuEntity prepareDanmaku(int life, Vec3 vec, YHDanmaku.Bullet type, DyeColor color) {
		ItemDanmakuEntity danmaku = new ItemDanmakuEntity(YHEntities.ITEM_DANMAKU.get(), player, player.level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(type.damage(), life, false, type.bypass(), vec);
		return danmaku;
	}

	@Override
	public ItemLaserEntity prepareLaser(int life, Vec3 pos, Vec3 vec, int len, YHDanmaku.Laser type, DyeColor color) {
		ItemLaserEntity danmaku = new ItemLaserEntity(YHEntities.ITEM_LASER.get(), player, player.level());
		danmaku.setItem(type.get(color).asStack());
		danmaku.setup(type.damage(), life, len, false, vec);
		danmaku.setPos(pos);
		return danmaku;
	}

	@Override
	public void shoot(SimplifiedProjectile danmaku) {
		player.level().addFreshEntity(danmaku);
	}

}
