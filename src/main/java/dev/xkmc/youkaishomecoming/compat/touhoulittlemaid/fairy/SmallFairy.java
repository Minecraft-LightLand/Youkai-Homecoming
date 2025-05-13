package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

@SerialClass
public class SmallFairy extends FairyEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 10)
				.add(Attributes.ATTACK_DAMAGE, 2);
	}

	public SmallFairy(EntityType<? extends FairyEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public float percentageDamage(LivingEntity le) {
		return super.percentageDamage(le) / 2;
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setSpell(this, "fairy:" + random().nextInt(18));
	}

	@Override
	public double getStopRange() {
		return 4;
	}

}
