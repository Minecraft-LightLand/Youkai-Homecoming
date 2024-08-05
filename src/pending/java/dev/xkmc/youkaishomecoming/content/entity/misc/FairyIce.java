package dev.xkmc.youkaishomecoming.content.entity.misc;

import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FairyIce extends ThrowableItemProjectile {
	public static final double SPLASH_RANGE = 4.0D;
	private static final double SPLASH_RANGE_SQ = 16.0D;

	public FairyIce(EntityType<? extends FairyIce> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public FairyIce(Level pLevel, LivingEntity pShooter) {
		super(YHEntities.FAIRY_ICE.get(), pShooter, pLevel);
	}

	public FairyIce(Level pLevel, double pX, double pY, double pZ) {
		super(YHEntities.FAIRY_ICE.get(), pX, pY, pZ, pLevel);
	}

	protected Item getDefaultItem() {
		return YHItems.FAIRY_ICE_CRYSTAL.get();
	}

	protected void onHitBlock(BlockHitResult hit) {
		super.onHitBlock(hit);
		if (level().isClientSide) return;
		Direction dir = hit.getDirection();
		BlockPos pos = hit.getBlockPos();
		BlockPos rel = pos.relative(dir);
		dowseFire(rel);
		dowseFire(rel.relative(dir.getOpposite()));
		for (Direction hor : Direction.Plane.HORIZONTAL) {
			dowseFire(rel.relative(hor));
		}
	}

	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (level().isClientSide) return;
		areaDamage();
		level().levelEvent(2002, blockPosition(), 0xFFCFDDF4);
		discard();
	}

	private void areaDamage() {
		AABB aabb = getBoundingBox().inflate(SPLASH_RANGE, 2.0D, SPLASH_RANGE);
		for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, aabb)) {
			if (e instanceof CirnoEntity) continue;
			double d0 = distanceToSqr(e);
			if (d0 >= SPLASH_RANGE_SQ) continue;
			float dmg = e.isSensitiveToWater() ? 18 : 9;
			e.hurt(damageSources().indirectMagic(this, getOwner()), dmg);
			if (e.isOnFire() && e.isAlive()) {
				e.extinguishFire();
			}
			if (e.canFreeze()) {
				e.setTicksFrozen(360);
			}
		}
	}

	private void dowseFire(BlockPos pos) {
		BlockState state = level().getBlockState(pos);
		if (state.is(BlockTags.FIRE)) {
			level().removeBlock(pos, false);
		} else if (AbstractCandleBlock.isLit(state)) {
			AbstractCandleBlock.extinguish(null, state, level(), pos);
		} else if (CampfireBlock.isLitCampfire(state)) {
			level().levelEvent(null, 1009, pos, 0);
			CampfireBlock.dowse(getOwner(), level(), pos, state);
			level().setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, Boolean.FALSE));
		}
	}

}