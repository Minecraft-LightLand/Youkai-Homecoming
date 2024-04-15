package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SerialClass
public class ItemLaserEntity extends YHBaseLaserEntity implements ItemSupplier {

	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ItemLaserEntity.class, EntityDataSerializers.ITEM_STACK);

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		super(pEntityType, pShooter, pLevel);
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

	protected ItemStack getItemRaw() {
		return this.getEntityData().get(DATA_ITEM_STACK);
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

}