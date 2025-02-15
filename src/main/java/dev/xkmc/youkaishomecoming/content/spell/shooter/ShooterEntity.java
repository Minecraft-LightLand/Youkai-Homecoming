package dev.xkmc.youkaishomecoming.content.spell.shooter;

import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleHolder;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.mover.DanmakuMover;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverInfo;
import dev.xkmc.youkaishomecoming.content.spell.mover.MoverOwner;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public class ShooterEntity extends ProjectileHealthEntity implements LivingCardHolder, SpellCircleHolder, MoverOwner {

	@SerialClass.SerialField
	private ShooterData data = ShooterData.EMPTY;

	@Nullable
	@SerialClass.SerialField
	public DanmakuMover mover = null;
	@Nullable
	@SerialClass.SerialField
	private LivingEntity target;
	@Nullable
	@SerialClass.SerialField
	private SpellCard spellCard;

	public ShooterEntity(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	public void setup(@Nullable LivingEntity owner, @Nullable LivingEntity target, ShooterData data, SpellCard card) {
		setOwner(owner);
		this.target = target;
		this.data = data;
		this.spellCard = card;
		var ins = getAttribute(Attributes.MAX_HEALTH);
		if (ins != null) ins.setBaseValue(data.health());
	}

	@Override
	public TraceableEntity asTraceable() {
		return this;
	}

	@Override
	public int lifetime() {
		return data.life();
	}

	@Override
	public void serverAiStep() {
		if (spellCard != null && isAlive()) {
			spellCard.tick(this);
		}
	}

	@Override
	protected ProjectileMovement updateVelocity(Vec3 vec, Vec3 pos) {
		if (mover != null) {
			return mover.move(new MoverInfo(tickCount, pos, vec, this));
		}
		return super.updateVelocity(vec, pos);
	}

	// spell

	@Override
	public boolean shouldShowSpellCircle() {
		return true;
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		return data.circle();
	}

	@Override
	public float getCircleSize(float pTick) {
		if (tickCount < 20) {
			return Mth.clamp((tickCount + pTick) / 20f, 0, 1);
		}
		if (deathTime > 0) {
			return Mth.clamp((20 - deathTime + pTick) / 20f, 0, 1);
		}
		return 1;
	}


	// card holder

	@Override
	public LivingEntity self() {
		return this;
	}

	@Override
	public LivingEntity shooter() {
		return getOwner() instanceof LivingEntity le ? le : this;
	}

	@Override
	public @Nullable LivingEntity targetEntity() {
		return target;
	}

	@Override
	public double getDamage() {
		return data.damage();
	}

	// data

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
