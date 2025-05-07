package dev.xkmc.fastprojectileapi.render.virtual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import dev.xkmc.l2serial.util.Wrappers;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.LinkedList;

public class ClientDanmakuCache {

	private static ClientDanmakuCache CACHE = null;


	public static ClientDanmakuCache get(Level level) {
		if (CACHE == null || CACHE.level != level) {
			CACHE = new ClientDanmakuCache(level);
		}
		return CACHE;
	}

	private final Level level;
	private final LinkedList<SimplifiedProjectile> all = new LinkedList<>();
	private final Int2ObjectOpenHashMap<SimplifiedProjectile> map = new Int2ObjectOpenHashMap<>(2048);


	public ClientDanmakuCache(Level level) {
		this.level = level;
	}

	public void add(SimplifiedProjectile sp) {
		all.add(sp);
		map.put(sp.getId(), sp);
	}

	public void erase(int id) {
		var e = map.get(id);
		e.markErased();
	}

	public void tick() {
		var itr = all.iterator();
		while (itr.hasNext()) {
			var e = itr.next();
			e.setOldPosAndRot();
			++e.tickCount;
			e.tick();
			if (!e.isValid()) {
				itr.remove();
				map.remove(e.getId());
			}
		}
	}

	public void renderAll(Camera cam, Frustum frustum, PoseStack pose, float pTick, MultiBufferSource.BufferSource buffer) {
		Vec3 vec3 = cam.getPosition();
		double d0 = vec3.x();
		double d1 = vec3.y();
		double d2 = vec3.z();
		EntityRenderDispatcher disp = Minecraft.getInstance().getEntityRenderDispatcher();
		var box = disp.shouldRenderHitBoxes() && !Minecraft.getInstance().showOnlyReducedInfo() ?
				buffer.getBuffer(RenderType.lines()) : null;
		for (var e : all) {
			this.maybeRenderEntity(disp, frustum, e, d0, d1, d2, pTick, pose, box);
		}
	}

	private <E extends SimplifiedProjectile> void maybeRenderEntity(
			EntityRenderDispatcher disp, Frustum frustum, E e,
			double camx, double camy, double camz, float pTick,
			PoseStack pose, @Nullable VertexConsumer box
	) {
		EntityRenderer<? super E> er = disp.getRenderer(e);
		if (!er.shouldRender(e, frustum, camx, camy, camz)) return;
		double dx = Mth.lerp(pTick, e.xOld, e.getX());
		double dy = Mth.lerp(pTick, e.yOld, e.getY());
		double dz = Mth.lerp(pTick, e.zOld, e.getZ());
		this.renderEntity(e, er, dx - camx, dy - camy, dz - camz, pTick, pose, box);
	}

	public <E extends SimplifiedProjectile> void renderEntity(
			E e, EntityRenderer<? super E> er,
			double x, double y, double z, float pTick,
			PoseStack pose, @Nullable VertexConsumer box
	) {
		if (!(er instanceof ProjectileRenderer<?> pr)) return;
		ProjectileRenderer<E> r = Wrappers.cast(pr);
		Vec3 vec3 = er.getRenderOffset(e, pTick);
		double dx = x + vec3.x();
		double dy = y + vec3.y();
		double dz = z + vec3.z();
		pose.pushPose();
		pose.translate(dx, dy, dz);
		r.render(e, pTick, pose);
		if (box != null) {
			pose.translate(-vec3.x(), -vec3.y(), -vec3.z());
			renderHitbox(pose, box, e, pTick);
		}
		pose.popPose();
	}

	private static void renderHitbox(PoseStack pose, VertexConsumer vc, Entity e, float pTick) {
		AABB aabb = e.getBoundingBox().move(-e.getX(), -e.getY(), -e.getZ());
		LevelRenderer.renderLineBox(pose, vc, aabb, 1.0F, 1.0F, 1.0F, 1.0F);
		Vec3 vec3 = e.getViewVector(pTick);
		Matrix4f mat4 = pose.last().pose();
		Matrix3f mat3 = pose.last().normal();
		vc.vertex(mat4, 0.0F, e.getEyeHeight(), 0.0F)
				.color(0, 0, 255, 255)
				.normal(mat3, (float) vec3.x, (float) vec3.y, (float) vec3.z)
				.endVertex();
		vc.vertex(mat4,
						(float) (vec3.x * 2.0D),
						(float) (e.getEyeHeight() + vec3.y * 2.0D),
						(float) (vec3.z * 2.0D)
				).color(0, 0, 255, 255)
				.normal(mat3, (float) vec3.x, (float) vec3.y, (float) vec3.z)
				.endVertex();
	}

}
