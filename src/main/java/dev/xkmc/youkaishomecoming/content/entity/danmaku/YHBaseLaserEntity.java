package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.collision.LaserHitHelper;
import dev.xkmc.danmaku.entity.BaseLaser;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Objects;

@SerialClass
public class YHBaseLaserEntity extends BaseLaser implements IEntityAdditionalSpawnData, IYHDanmaku {

	@SerialClass.SerialField
	private int life = 0, prepare, start, end;
	@SerialClass.SerialField
	private boolean bypassWall = false;
	@SerialClass.SerialField
	public float damage = 0, length = 0;

	public double earlyTerminate = -1;

	protected YHBaseLaserEntity(EntityType<? extends YHBaseLaserEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected YHBaseLaserEntity(EntityType<? extends YHBaseLaserEntity> pEntityType, double pX, double pY, double pZ, Level pLevel) {
		this(pEntityType, pLevel);
		this.setPos(pX, pY, pZ);
	}

	protected YHBaseLaserEntity(EntityType<? extends YHBaseLaserEntity> pEntityType, LivingEntity pShooter, Level pLevel) {
		this(pEntityType, pShooter.getX(), pShooter.getEyeY() - (double) 0.1F, pShooter.getZ(), pLevel);
		this.setOwner(pShooter);
	}

	public void setup(float damage, int life, float length, boolean bypassWall, Vec3 vec3) {
		double d0 = vec3.horizontalDistance();
		setup(damage, life, length, bypassWall,
				(float) (-Mth.atan2(vec3.x, vec3.z) * Mth.RAD_TO_DEG),
				(float) (-Mth.atan2(vec3.y, d0) * Mth.RAD_TO_DEG));
	}

	public void setup(float damage, int life, float length, boolean bypassWall, float rY, float rX) {
		this.damage = damage;
		this.bypassWall = bypassWall;
		this.length = length;
		this.prepare = 20;
		this.start = 40;
		this.end = life + 40;
		this.life = life + 60;
		setYRot(rY);
		setXRot(rX);
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public boolean checkBlockHit() {
		return !bypassWall;
	}

	@Override
	public float getEffectiveHitRadius() {
		return getBbWidth() / 4f;
	}

	@Override
	public boolean checkEntityHit() {
		return tickCount > start && tickCount < end;
	}

	@Override
	public float damage(Entity target) {
		return damage;
	}

	public float percentOpen(float pTick) {
		pTick += tickCount;
		if (pTick < prepare) return 0.1f;
		else if (pTick < start)
			return (pTick - prepare) / (start - prepare) * 0.9f + 0.1f;
		else if (pTick < end) return 1;
		else if (pTick < life)
			return (pTick - end) / (life - end) * -0.9f + 1f;
		else return 0;
	}

	public float effectiveLength() {
		return (float) (earlyTerminate >= 0 ? earlyTerminate : length);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide() && tickCount > life) {
			discard();
		}
	}

	@Override
	public boolean canHitEntity(Entity target) {
		return super.canHitEntity(target) && shouldHurt(getOwner(), target);
	}

	@Override
	protected void onHit(LaserHitHelper.LaserHitResult hit) {
		if (level().isClientSide()) {
			if (hit.bhit() != null && hit.bhit().getType() != HitResult.Type.MISS) {
				earlyTerminate = position().add(0, getBbHeight() / 2f, 0).distanceTo(hit.bhit().getLocation());
			} else earlyTerminate = -1;
		}
		for (var e : hit.ehit()) {
			hurtTarget(e);
		}
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		var src = position().add(0, getBbHeight() / 2f, 0);
		return new AABB(src, src.add(getForward().scale(length))).inflate(getBbWidth() / 2f);
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
		super.writeSpawnData(buffer);
		PacketCodec.to(buffer, this);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		PacketCodec.from(additionalData, getClass(), Wrappers.cast(this));
	}

}
