package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;

@SerialClass
public class RemiliaEntity extends BossYoukaiEntity {

	public RemiliaEntity(EntityType<? extends BossYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public boolean shouldIgnore(LivingEntity e) {
		return super.shouldIgnore(e) || e.getMobType() == MobType.UNDEAD || e.getMobType() == MobType.ILLAGER;
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setRemilia(this);
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (targets.contains(e)) {
			double heal = YHModConfig.COMMON.danmakuHealOnHitTarget.get();
			heal(getMaxHealth() * (float) heal);
		}
	}

}
