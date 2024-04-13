package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import dev.xkmc.danmaku.entity.BaseLaser;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Objects;

@SerialClass
public class YHBaseLaserEntity extends BaseLaser implements IEntityAdditionalSpawnData {

	public YHBaseLaserEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
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
		PacketCodec.to(buffer,this);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		PacketCodec.from(additionalData,  getClass(), Wrappers.cast(this));
	}

}
