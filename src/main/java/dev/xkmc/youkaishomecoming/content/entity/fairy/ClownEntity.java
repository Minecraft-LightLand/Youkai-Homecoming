package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@SerialClass
public class ClownEntity extends BossYoukaiEntity {

	public ClownEntity(EntityType<? extends ClownEntity> type, Level level) {
		super(type, level);
	}

	public void initSpellCard() {
		TouhouSpellCards.setClown(this);
	}

	public boolean isLunatic() {
		return getFlag(4) || isChaotic() || combatProgress.progress < combatProgress.maxProgress / 2;
	}

}
