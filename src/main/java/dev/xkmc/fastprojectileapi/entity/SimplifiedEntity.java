package dev.xkmc.fastprojectileapi.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class SimplifiedEntity extends Entity {

	public SimplifiedEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public void baseTick() {
		this.level().getProfiler().push("entityBaseTick");
		if (this.boardingCooldown > 0) {
			--this.boardingCooldown;
		}
		this.walkDistO = this.walkDist;
		this.xRotO = this.getXRot();
		this.yRotO = this.getYRot();
		this.handleNetherPortal();
		this.checkBelowWorld();
		this.firstTick = false;
		this.level().getProfiler().pop();
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
	public void setSecondsOnFire(int pSeconds) {

	}

	@Override
	public int getRemainingFireTicks() {
		return 0;
	}

	@Override
	public void setRemainingFireTicks(int pRemainingFireTicks) {
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	public void clearFire() {
	}

	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	public final Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public boolean mayInteract(Level pLevel, BlockPos pPos) {
		return false;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
		return LazyOptional.empty();
	}

}
