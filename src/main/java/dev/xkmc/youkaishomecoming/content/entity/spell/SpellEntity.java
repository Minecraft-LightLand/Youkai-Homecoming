package dev.xkmc.youkaishomecoming.content.entity.spell;

import dev.xkmc.l2library.base.BaseEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@SerialClass
public class SpellEntity extends BaseEntity {//TODO

	public SpellEntity(EntityType<? extends SpellEntity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {

	}

}
