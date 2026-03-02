package dev.xkmc.youkaishomecoming.content.block.furniture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;

public abstract class SimplifiedEntity extends Entity {

	public SimplifiedEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public void tick() {
		baseTick();
	}

	@Override
	public void baseTick() {
		this.walkDistO = this.walkDist;
		this.xRotO = this.getXRot();
		this.yRotO = this.getYRot();
		this.checkBelowWorld();
		this.firstTick = false;
	}

	protected boolean updateInWaterStateAndDoFluidPushing() {
		return false;
	}

	protected void doWaterSplashEffect() {
	}

	@Override
	public boolean canSpawnSprintParticle() {
		return false;
	}

	@Override
	protected void tryCheckInsideBlocks() {
	}

	@Override
	protected void checkInsideBlocks() {
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	public int getRemainingFireTicks() {
		return 0;
	}

	@Override
	public void setRemainingFireTicks(int pRemainingFireTicks) {
	}

	@Override
	public void clearFire() {
	}

	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	public boolean mayInteract(Level pLevel, BlockPos pPos) {
		return false;
	}

	private int typeId = -1;

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	public int getTypeId() {
		if (typeId < 0) {
			typeId = BuiltInRegistries.ENTITY_TYPE.getId(getType());
		}
		return typeId;
	}
}
