package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class YukariEntity extends BossYoukaiEntity {

	public YukariEntity(EntityType<? extends BossYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setYukari(this);
	}

	@Override
	public void onDanmakuImmune(LivingEntity e, IYHDanmaku danmaku, DamageSource source) {
		if (e.tickCount - e.getLastHurtByMobTimestamp() < 20)
			return;
		if (e instanceof Player player && player.getAbilities().instabuild)
			return;
		double rate = e instanceof Player ?
				YHModConfig.COMMON.danmakuPlayerPHPDamage.get() :
				YHModConfig.COMMON.danmakuMinPHPDamage.get();
		double dmg = Math.max(rate * Math.max(e.getHealth(), e.getMaxHealth()), danmaku.damage(e));
		e.setHealth(e.getHealth() - (float) dmg);
		if (e.isDeadOrDying()) {
			e.die(YHDamageTypes.abyssal(danmaku));
		}
	}

}
