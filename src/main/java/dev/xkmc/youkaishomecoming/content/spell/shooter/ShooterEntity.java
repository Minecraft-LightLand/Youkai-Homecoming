package dev.xkmc.youkaishomecoming.content.spell.shooter;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ShooterEntity extends BaseHealthEntity {

	private ShooterMotion motion;
	private ShooterData data;

	private UUID ownerId;
	private Entity owner;

	private LivingEntity target;

	protected ShooterEntity(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

}
