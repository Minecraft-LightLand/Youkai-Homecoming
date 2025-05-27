package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class ClownEntity extends BossYoukaiEntity {

	public ClownEntity(EntityType<? extends ClownEntity> type, Level level) {
		super(type, level);
	}

	public void initSpellCard() {
		TouhouSpellCards.setClown(this);
	}

	@Override
	public boolean shouldIgnore(LivingEntity e) {
		return super.shouldIgnore(e) || !(e instanceof Player) && e.getMobType() == MobType.UNDEAD;
	}

	public boolean isLunatic() {
		return getFlag(4) || isChaotic() || combatProgress.progress < combatProgress.maxProgress / 2;
	}

}
