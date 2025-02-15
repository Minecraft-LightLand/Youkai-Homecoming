package dev.xkmc.youkaishomecoming.content.spell.shooter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.UUID;

public class BaseHealthEntity extends LivingEntity implements TraceableEntity, IEntityAdditionalSpawnData {

	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	protected BaseHealthEntity(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}


	public void setOwner(@Nullable Entity pOwner) {
		if (pOwner != null) {
			ownerUUID = pOwner.getUUID();
			cachedOwner = pOwner;
		}
	}

	@Nullable
	public Entity getOwner() {
		if (cachedOwner != null && !cachedOwner.isRemoved()) {
			return cachedOwner;
		} else if (ownerUUID != null && level() instanceof ServerLevel) {
			cachedOwner = ((ServerLevel) level()).getEntity(ownerUUID);
			return cachedOwner;
		} else {
			return null;
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void addAdditionalSaveData(CompoundTag nbt) {
		if (ownerUUID != null) {
			nbt.putUUID("Owner", ownerUUID);
		}
		nbt.putInt("Age", tickCount);
	}

	@OverridingMethodsMustInvokeSuper
	public void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.hasUUID("Owner")) {
			ownerUUID = nbt.getUUID("Owner");
			cachedOwner = null;
		}
		tickCount = nbt.getInt("Age");
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeLong(level().getGameTime() - tickCount);
		var owner = getOwner();
		buffer.writeInt(owner == null ? -1 : owner.getId());
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		xOld = xo = position().x;
		yOld = yo = position().y;
		zOld = zo = position().z;
		tickCount = (int) (level().getGameTime() - additionalData.readLong());
		int id = additionalData.readInt();
		if (id >= 0) {
			var e = level().getEntity(id);
			if (e != null) {
				setOwner(e);
			}
		}
	}

	// living simplify

	@Override
	public void tick() {
		super.baseTick();
		if (!this.isRemoved()) {
			this.aiStep();
		}
	}

	public void aiStep() {
		if (!isImmobile() && isEffectiveAi()) {
			this.serverAiStep();
		}
	}

	@Override
	protected boolean isAffectedByFluids() {
		return false;
	}

	@Override
	public boolean shouldDropExperience() {
		return false;
	}

	@Override
	protected boolean shouldDropLoot() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	protected void doPush(Entity e) {
	}

	// entity simplify

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

	public boolean mayInteract(Level pLevel, BlockPos pPos) {
		return false;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return LazyOptional.empty();
	}

	// living

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return List.of();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

}
