package dev.xkmc.youkaishomecoming.content.spell.item;

import dev.xkmc.youkaishomecoming.content.entity.danmaku.ItemDanmakuEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.LivingCardHolder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record PlayerHolder(
		Player player, Vec3 dir, ItemSpell spell, @Nullable LivingEntity targeted
) implements LivingCardHolder {

	@Override
	public LivingEntity self() {
		return player;
	}

	@Override
	public LivingEntity shooter() {
		return player;
	}

	@Override
	public @Nullable LivingEntity targetEntity() {
		return targeted;
	}

	@Override
	public float getDamage(YHDanmaku.IDanmakuType type) {
		return type.damage();
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
	public void shoot(Entity danmaku) {
		if (danmaku instanceof ItemDanmakuEntity e) {
			if (e.afterExpiry != null) {
				e.afterExpiry.setup(this);
			}
		}
		LivingCardHolder.super.shoot(danmaku);
	}
}
