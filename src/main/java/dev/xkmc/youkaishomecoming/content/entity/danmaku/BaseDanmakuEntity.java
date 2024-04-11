package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

@SerialClass
public class BaseDanmakuEntity extends Projectile {

	@SerialClass.SerialField
	private int life = 0;
	@SerialClass.SerialField
	private boolean bypassWall = false, bypassEntity = false;
	@SerialClass.SerialField
	public float damage = 0;

	protected BaseDanmakuEntity(EntityType<? extends BaseDanmakuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected BaseDanmakuEntity(EntityType<? extends BaseDanmakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	protected BaseDanmakuEntity(EntityType<? extends BaseDanmakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		this(pEntityType, pShooter.getX(), pShooter.getEyeY() - (double) 0.1F, pShooter.getZ(), pLevel);
		this.setOwner(pShooter);
	}

	public void setup(float damage, int life, boolean bypassWall, boolean bypassEntity, Vec3 initVec) {
		this.damage = damage;
		this.life = life;
		this.bypassWall = bypassWall;
		this.bypassEntity = bypassEntity;
		setDeltaMovement(initVec);
	}


	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}
	}


	public boolean shouldRenderAtSqrDistance(double pDistance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}

		d0 *= 64.0D;
		return pDistance < d0 * d0;
	}

	public void tick() {
		super.tick();
		HitResult hitresult = DanmakuHitHelper.getHitResultOnMoveVector(this, this::canHitEntity);
		boolean flag = false;
		if (hitresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
			BlockState blockstate = this.level().getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				BlockEntity blockentity = this.level().getBlockEntity(blockpos);
				if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
					TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity) blockentity);
				}
				flag = true;
			}
			if (!flag && bypassWall) {
				if (!level().isClientSide()) life--;
				flag = true;
			}
		}
		if (hitresult.getType() != HitResult.Type.MISS && !flag &&
				!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
			this.onHit(hitresult);
		}
		this.checkInsideBlocks();
		danmakuMove();

		if (!level().isClientSide()) {
			life--;
			if (life <= 0) {
				level().broadcastEntityEvent(this, EntityEvent.DEATH);
				discard();
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!level().isClientSide) {
			level().broadcastEntityEvent(this, EntityEvent.DEATH);
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (level().isClientSide) return;
		var e = result.getEntity();
		boolean skip = bypassEntity && e instanceof LivingEntity le && le.invulnerableTime > 0;
		if (!skip && e.hurt(YHDamageTypes.danmaku(this), damage)) {
			if (e instanceof LivingEntity le) {
				if (getOwner() instanceof YoukaiEntity youkai) {
					youkai.onDanmakuHit(le, this);
				}
			}
			if (bypassEntity) life--;
			else {
				level().broadcastEntityEvent(this, EntityEvent.DEATH);
				discard();
			}
		}
	}

	public void handleEntityEvent(byte pId) {
		if (pId == EntityEvent.DEATH) {
			clientDeathParticle();
		}
	}

	protected void clientDeathParticle() {

	}

	protected void danmakuMove() {
		Vec3 vec3 = updateVelocity(getDeltaMovement());
		setDeltaMovement(vec3);
		this.updateRotation();
		double d2 = this.getX() + vec3.x;
		double d0 = this.getY() + vec3.y;
		double d1 = this.getZ() + vec3.z;
		this.setPos(d2, d0, d1);
	}

	protected Vec3 updateVelocity(Vec3 vec) {
		return vec;
	}

	@Override
	protected void defineSynchedData() {

	}

}
