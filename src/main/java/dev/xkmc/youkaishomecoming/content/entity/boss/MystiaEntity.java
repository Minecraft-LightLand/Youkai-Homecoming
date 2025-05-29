package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class MystiaEntity extends BossYoukaiEntity {

	public MystiaEntity(EntityType<? extends BossYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setMystia(this);
	}

	@Override
	protected int getCD(DamageSource source) {
		int ans = super.getCD(source);
		return ans <= 10 ? ans : 10 + (ans - 10) / 2;
	}

	@Override
	protected int damageLimit() {
		return 5;
	}

	@Override
	protected int nonDanmakuReduction() {
		return 2;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
		if (!(e instanceof Player)) return;
		super.onDanmakuHit(e, danmaku);
	}

}
