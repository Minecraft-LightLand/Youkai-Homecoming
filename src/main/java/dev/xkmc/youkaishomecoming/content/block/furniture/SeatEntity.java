package dev.xkmc.youkaishomecoming.content.block.furniture;

import dev.xkmc.fastprojectileapi.entity.SimplifiedEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class SeatEntity extends SimplifiedEntity {

	public SeatEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	public SeatEntity(Level level, BlockPos pos) {
		this(YHEntities.SEAT.get(), level);
		this.noPhysics = true;
	}

	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
		AABB bb = this.getBoundingBox();
		Vec3 diff = new Vec3(x, y, z).subtract(bb.getCenter());
		this.setBoundingBox(bb.move(diff));
	}

	public void setDeltaMovement(Vec3 vec) {
	}

	public void tick() {
		if (this.level().isClientSide) return;
		boolean blockPresent = this.level().getBlockState(this.blockPosition())
				.getBlock() instanceof WoodSeatBlock;
		if (!this.isVehicle() || !blockPresent) {
			this.discard();
		}
	}

	protected boolean canRide(Entity e) {
		return e instanceof AbstractVillager ||
				e instanceof SeatableEntity ||
				e instanceof Player && !(e instanceof FakePlayer);
	}

	public Vec3 getDismountLocationForPassenger(LivingEntity le) {
		return super.getDismountLocationForPassenger(le).add(0.0, 0.75, 0.0);
	}

	protected void defineSynchedData() {
	}

	protected void readAdditionalSaveData(CompoundTag tag) {
	}

	protected void addAdditionalSaveData(CompoundTag tag) {
	}

}
