package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.collision.DanmakuHitHelper;
import dev.xkmc.danmaku.entity.BaseDanmaku;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.entity.PartEntity;

import java.util.Objects;

@SerialClass
public class YHBaseDanmakuEntity extends BaseDanmaku implements IEntityAdditionalSpawnData {

	@SerialClass.SerialField
	private int life = 0;
	@SerialClass.SerialField
	private boolean bypassWall = false, bypassEntity = false;
	@SerialClass.SerialField
	public float damage = 0;

	protected YHBaseDanmakuEntity(EntityType<? extends YHBaseDanmakuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected YHBaseDanmakuEntity(EntityType<? extends YHBaseDanmakuEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	protected YHBaseDanmakuEntity(EntityType<? extends YHBaseDanmakuEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
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

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		PacketCodec.to(buffer,this);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		PacketCodec.from(additionalData,  getClass(), Wrappers.cast(this));
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
		HitResult hitresult = DanmakuHitHelper.getHitResultOnMoveVector(this, !bypassWall);
		if (hitresult != null) {
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
			}
			if (hitresult.getType() != HitResult.Type.MISS && !flag) {
				this.onHit(hitresult);
			}
		}
		danmakuMove();
		life--;
		if (life <= 0) {
			if (!level().isClientSide()) {
				discard();
			}
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!level().isClientSide) {
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (level().isClientSide) return;
		var e = result.getEntity();
		if (bypassEntity && e instanceof LivingEntity le) {
			if (le.invulnerableTime > 0) {
				DamageSource source = le.getLastDamageSource();
				if (source != null && source.getDirectEntity() == this) {
					return;
				}
			}
		}
		if (!e.hurt(YHDamageTypes.danmaku(this), damage)) return;
		LivingEntity target = null;
		while (e instanceof PartEntity<?> pe) {
			e = pe.getParent();
		}
		if (e instanceof LivingEntity le) target = le;

		if (target != null) {
			if (getOwner() instanceof YoukaiEntity youkai) {
				youkai.onDanmakuHit(target, this);
			}
		}
		if (!bypassEntity) {
			discard();
		}
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
