package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.capability.GrazeHelper;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.spell.mover.DanmakuMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverInfo;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverOwner;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.TrailAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class ItemDanmakuEntity extends YHBaseDanmakuEntity implements ItemSupplier, MoverOwner {

	@SerialClass.SerialField
	public int controlCode = 0;
	@SerialClass.SerialField
	public DanmakuMover mover = null;
	@SerialClass.SerialField
	public TrailAction afterExpiry = null;
	@SerialClass.SerialField
	public ItemStack stack = ItemStack.EMPTY;

	private boolean isErased = false;

	public ItemDanmakuEntity(EntityType<? extends ItemDanmakuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ItemDanmakuEntity(EntityType<? extends ItemDanmakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public ItemDanmakuEntity(EntityType<? extends ItemDanmakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		super(pEntityType, pShooter, pLevel);
	}

	public void setItem(ItemStack pStack) {
		stack = pStack.copyWithCount(1);
		refreshDimensions();
	}

	public void setControlCode(int code) {
		this.controlCode = code;
	}

	@Override
	public TraceableEntity asTraceable() {
		return this;
	}

	@Override
	protected void terminate() {
		if (afterExpiry == null) return;
		CardHolder holder = null;
		Entity e = getOwner();
		if (e instanceof CardHolder h) holder = h;
		if (holder == null) return;
		afterExpiry.execute(holder, position(), getDeltaMovement());
	}

	@Override
	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		if (mover != null) {
			return mover.move(new MoverInfo(tickCount, pos, vec, this));
		}
		if (controlCode > 0 && getOwner() instanceof DanmakuCommander commander)
			return commander.move(controlCode, tickCount, vec);
		return super.updateVelocity(vec, pos);
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

	public void markErased() {
		if (!isErased)
			super.markErased();
		isErased = true;
	}

	@Override
	public boolean isValid() {
		return !isErased && super.isValid();
	}

	private Float sizeCache = null;

	public float scale() {
		if (sizeCache == null) {
			if (getItem().getItem() instanceof DanmakuItem item) {
				sizeCache = item.size;
			}
		}
		return sizeCache == null ? 1 : sizeCache;
	}

	private int lastGraze = 0;

	@Override
	public float grazeRange() {
		return 1.5f;
	}

	@Override
	public void doGraze(Player entity) {
		if (tickCount < lastGraze) return;
		lastGraze = tickCount + 20;
		GrazeHelper.graze(entity, this);
	}

}