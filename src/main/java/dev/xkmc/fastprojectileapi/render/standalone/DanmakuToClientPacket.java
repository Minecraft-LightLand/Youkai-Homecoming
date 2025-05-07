package dev.xkmc.fastprojectileapi.render.standalone;

import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.UUID;

@SerialClass
public class DanmakuToClientPacket extends SerialPacketBase {

	@SerialClass.SerialField
	private int typeId;
	@SerialClass.SerialField
	private int entityId;
	@SerialClass.SerialField
	private UUID uuid;
	@SerialClass.SerialField
	private double posX, posY, posZ;
	@SerialClass.SerialField
	private float pitch, yaw;
	@SerialClass.SerialField
	private double velX, velY, velZ;
	@SerialClass.SerialField
	private byte[] data;

	public DanmakuToClientPacket() {

	}

	public DanmakuToClientPacket(SimplifiedProjectile e) {
		this.typeId = BuiltInRegistries.ENTITY_TYPE.getId(e.getType());
		this.entityId = e.getId();
		this.uuid = e.getUUID();
		this.posX = e.getX();
		this.posY = e.getY();
		this.posZ = e.getZ();
		this.pitch = e.getXRot();
		this.yaw = e.getYRot();
		Vec3 vec3d = e.getDeltaMovement();
		this.velX = vec3d.x;
		this.velY = vec3d.y;
		this.velZ = vec3d.z;
		var data = new FriendlyByteBuf(Unpooled.buffer());
		e.writeSpawnData(data);
		this.data = Arrays.copyOfRange(data.array(), 0, data.writerIndex());
		data.release();
	}

	@Override
	public void handle(NetworkEvent.Context ctx) {
		var type = BuiltInRegistries.ENTITY_TYPE.getHolder(typeId);
		if (type.isEmpty()) return;
		Entity e = DanmakuClientHandler.create(type.get().value());
		if (!(e instanceof SimplifiedProjectile sp)) return;
		e.syncPacketPositionCodec(posX, posY, posZ);
		e.absMoveTo(posX, posY, posZ, yaw, pitch);
		e.setId(entityId);
		e.setUUID(uuid);
		e.lerpMotion(velX, velY, velZ);
		var buffer = Unpooled.wrappedBuffer(data);
		sp.readSpawnData(new FriendlyByteBuf(buffer));
		buffer.release();
		DanmakuClientHandler.add(sp);
	}

}
