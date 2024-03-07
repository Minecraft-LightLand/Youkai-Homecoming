package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class FrogSyncPacket extends SerialPacketBase {

	@SerialClass.SerialField
	public CompoundTag tag;

	@SerialClass.SerialField
	public int id;

	@Deprecated
	public FrogSyncPacket() {
	}

	public FrogSyncPacket(LivingEntity entity, FrogGodCapability cap) {
		this.id = entity.getId();
		this.tag = TagCodec.toTag(new CompoundTag(), FrogGodCapability.class, cap, SerialClass.SerialField::toClient);
	}

	public void handle(NetworkEvent.Context context) {
		ClientCapHandler.handle(this);
	}
}
