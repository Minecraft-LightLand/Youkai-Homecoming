package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SerialClass
public class LunaEntity extends FairyEntity {

	public LunaEntity(EntityType<? extends LunaEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
	}

	public void initSpellCard() {
		TouhouSpellCards.setLuna(this);
	}

}
