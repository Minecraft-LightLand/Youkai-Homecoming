
package dev.xkmc.youkaishomecoming.content.entity.misc;

import dev.xkmc.youkaishomecoming.content.item.misc.FrozenFrogItem;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FrozenFrog extends ThrowableItemProjectile {

	public FrozenFrog(EntityType<? extends FrozenFrog> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public FrozenFrog(Level pLevel, LivingEntity pShooter) {
		super(YHEntities.FROZEN_FROG.get(), pShooter, pLevel);
	}

	public FrozenFrog(Level pLevel, double pX, double pY, double pZ) {
		super(YHEntities.FROZEN_FROG.get(), pX, pY, pZ, pLevel);
	}

	/**
	 * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
	 */
	public void handleEntityEvent(byte pId) {
		if (pId == EntityEvent.DEATH) {
			double d0 = 0.08D;
			for (int i = 0; i < 8; ++i) {
				level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, getItem()),
						getX(), getY(), getZ(),
						(random.nextFloat() - 0.5D) * d0,
						(random.nextFloat() - 0.5D) * d0,
						(random.nextFloat() - 0.5D) * d0);
			}
		}

	}

	/**
	 * Called when the arrow hits an entity
	 */
	protected void onHitEntity(EntityHitResult hit) {
		super.onHitEntity(hit);
		hit.getEntity().hurt(damageSources().thrown(this, getOwner()), 1);
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!level().isClientSide) {
			Frog frog = EntityType.FROG.create(level());
			if (frog != null) {
				if (getItem().getItem() instanceof FrozenFrogItem item)
					frog.setVariant(item.variant);
				frog.moveTo(getX(), getY(), getZ(), getYRot(), 0);
				frog.setHealth(1);
				level().addFreshEntity(frog);
				frog.setTicksFrozen(120);
			}
			level().broadcastEntityEvent(this, EntityEvent.DEATH);
			discard();
		}

	}

	protected Item getDefaultItem() {
		return YHItems.FROZEN_FROG_TEMPERATE.asItem();
	}

}