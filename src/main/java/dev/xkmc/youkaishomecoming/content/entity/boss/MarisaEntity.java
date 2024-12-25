package dev.xkmc.youkaishomecoming.content.entity.boss;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@SerialClass
public class MarisaEntity extends MaidenEntity {

	public MarisaEntity(EntityType<? extends MaidenEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setMarisa(this);
	}

}
