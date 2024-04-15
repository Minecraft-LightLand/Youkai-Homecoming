package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.entity.BaseDanmaku;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Objects;

@SerialClass
public class YHBaseDanmakuEntity extends BaseDanmaku implements IEntityAdditionalSpawnData, IYHDanmaku {

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

	@Override
	public boolean checkBlockHit() {
		return !bypassWall;
	}

	@Override
	public int lifetime() {
		return life;
	}

	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.put("auto-serial", Objects.requireNonNull(TagCodec.toTag(new CompoundTag(), this)));
	}

	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("auto-serial")) {
			Wrappers.run(() -> TagCodec.fromTag(nbt.getCompound("auto-serial"), getClass(), this, (f) -> true));
		}
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		PacketCodec.to(buffer, this);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		PacketCodec.from(additionalData, getClass(), Wrappers.cast(this));
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!level().isClientSide) {
			discard();
		}
	}

	@Override
	public float damage(Entity target) {
		return damage;
	}

	@Override
	public void onHitEntity(EntityHitResult result) {
		if (level().isClientSide) return;
		hurtTarget(result);
		if (!bypassEntity) {
			discard();
		}
	}

}
