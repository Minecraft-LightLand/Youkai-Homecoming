package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.content.spell.mover.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class ItemLaserEntity extends YHBaseLaserEntity implements ItemSupplier, MoverOwner {

	@SerialClass.SerialField
	public DanmakuMover mover;
	@SerialClass.SerialField
	public ItemStack stack = ItemStack.EMPTY;

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public ItemLaserEntity(EntityType<? extends ItemLaserEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		super(pEntityType, pShooter, pLevel);
	}

	public void setDelayedMover(float v0, float v1, int prepare, int setup) {
		var dir = getForward();
		var pos = position;
		var m = new CompositeMover();
		m.add(prepare, new ZeroMover(dir, dir, prepare));
		m.add(setup, new RectMover(pos, dir.scale(v0), Vec3.ZERO));
		m.add(life, new RectMover(pos.add(dir.scale(v0 * setup)), dir.scale(v1), Vec3.ZERO));
		this.mover = m;
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
		stack = pStack;
		refreshDimensions();
	}

	public ItemStack getItem() {
		return stack;
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		refreshDimensions();
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

	public void markErased(boolean kill) {
		if (!isErased)
			super.markErased(kill);
		isErased = true;
	}

	@Override
	public boolean isValid() {
		return !isErased && super.isValid();
	}

	private int lastGraze = 0;

	@Override
	public void doGraze(Player entity) {
		if (tickCount < lastGraze) return;
		lastGraze = tickCount + 5;
		GrazeHelper.graze(entity, this);
	}

	@Override
	public void poof() {
		if (!level().isClientSide()) return;
		if (!(getItem().getItem() instanceof LaserItem item)) return;
		int col = item.color.getTextColor();
		var pos = position().add(0, getBbHeight() / 2, 0);
		DanmakuParticleHelper.line(level(), pos, getForward(), col, length, getBbWidth() / 2, random);
	}

}