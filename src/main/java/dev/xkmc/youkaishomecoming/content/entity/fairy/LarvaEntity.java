package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

@SerialClass
public class LarvaEntity extends FairyEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 60)
				.add(Attributes.ATTACK_DAMAGE, 8);
	}

	public LarvaEntity(EntityType<? extends LarvaEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
	}

	public void initSpellCard() {
		TouhouSpellCards.setLarva(this);
	}

}
