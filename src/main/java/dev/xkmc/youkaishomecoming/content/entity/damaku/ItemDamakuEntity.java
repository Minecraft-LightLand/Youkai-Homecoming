
package dev.xkmc.youkaishomecoming.content.entity.damaku;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ItemDamakuEntity extends BaseDamakuEntity implements ItemSupplier {

	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ItemDamakuEntity.class, EntityDataSerializers.ITEM_STACK);

	public ItemDamakuEntity(EntityType<? extends ItemDamakuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ItemDamakuEntity(EntityType<? extends ItemDamakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public ItemDamakuEntity(EntityType<? extends ItemDamakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		super(pEntityType, pShooter, pLevel);
	}

	public void setItem(ItemStack pStack) {
		if (!pStack.is(this.getDefaultItem()) || pStack.hasTag()) {
			this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
		}
	}

	protected abstract Item getDefaultItem();

	protected ItemStack getItemRaw() {
		return this.getEntityData().get(DATA_ITEM_STACK);
	}

	public ItemStack getItem() {
		ItemStack itemstack = this.getItemRaw();
		return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
	}

	protected void defineSynchedData() {
		this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		ItemStack itemstack = this.getItemRaw();
		if (!itemstack.isEmpty()) {
			pCompound.put("Item", itemstack.save(new CompoundTag()));
		}

	}

	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		ItemStack itemstack = ItemStack.of(pCompound.getCompound("Item"));
		this.setItem(itemstack);
	}

}