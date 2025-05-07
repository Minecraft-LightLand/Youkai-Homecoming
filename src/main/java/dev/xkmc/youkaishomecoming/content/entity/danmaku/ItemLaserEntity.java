package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.content.spell.mover.DanmakuMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverInfo;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverOwner;
import dev.xkmc.youkaishomecoming.events.DanmakuGrazeEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

@SerialClass
public class ItemLaserEntity extends YHBaseLaserEntity implements ItemSupplier, MoverOwner {

	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ItemLaserEntity.class, EntityDataSerializers.ITEM_STACK);

	@SerialClass.SerialField
	public DanmakuMover mover;

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		super(pEntityType, pShooter, pLevel);
	}

	@Override
	public TraceableEntity asTraceable() {
		return this;
	}

	@Override
	protected void danmakuMove() {
		ProjectileMovement movement = updateVelocity(getDeltaMovement(), position());
		setDeltaMovement(movement.vec());
		updateRotation(movement.rot());
		double d2 = getX() + movement.vec().x;
		double d0 = getY() + movement.vec().y;
		double d1 = getZ() + movement.vec().z;
		setPos(d2, d0, d1);
	}

	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		if (mover != null) {
			return mover.move(new MoverInfo(tickCount, pos, vec, this));
		}
		return new ProjectileMovement(vec, rot());
	}

	public void setItem(ItemStack pStack) {
		this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
		refreshDimensions();
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (DATA_ITEM_STACK == key) {
			refreshDimensions();
		}
	}

	private ItemStack stackCache = null;

	protected ItemStack getItemRaw() {
		if (stackCache == null || stackCache.isEmpty()) {
			stackCache = this.getEntityData().get(DATA_ITEM_STACK);
		}
		return stackCache;
	}

	public ItemStack getItem() {
		return this.getItemRaw();
	}

	protected void defineSynchedData() {
		this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
	}

	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		ItemStack itemstack = this.getItemRaw();
		if (!itemstack.isEmpty()) {
			nbt.put("Item", itemstack.save(new CompoundTag()));
		}

	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		ItemStack itemstack = ItemStack.of(nbt.getCompound("Item"));
		this.setItem(itemstack);
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return super.getDimensions(pPose).scale(scale());
	}

	public boolean fullBright() {
		return true;
	}

	private Float sizeCache = null;

	public float scale() {
		if (sizeCache == null) {
			if (getItem().getItem() instanceof LaserItem item) {
				sizeCache = item.size;
			}
		}
		return sizeCache == null ? 1 : sizeCache;
	}

	private boolean isErased = false;

	public void markErased() {
		if (!isErased)
			discard();
		isErased = true;
	}

	public void erase(LivingEntity user) {
		if (getOwner() == user) return;
		if (!isErased)
			discard();
		isErased = true;
	}

	@Override
	public boolean isValid() {
		return !isErased && super.isValid();
	}

	private int lastGraze = 0;

	@Override
	public float grazeRange() {
		return 1.5f;
	}

	@Override
	public void doGraze(Player entity) {
		if (tickCount < lastGraze) return;
		lastGraze = tickCount + 5;
		MinecraftForge.EVENT_BUS.post(new DanmakuGrazeEvent(entity, this));
	}

}