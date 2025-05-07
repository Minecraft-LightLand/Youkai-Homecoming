package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;
import java.util.function.Consumer;

public record DoubleLayerLaserType(ResourceLocation inner, ResourceLocation outer, int color)
		implements RenderableProjectileType<DoubleLayerLaserType, DoubleLayerLaserType.Ins> {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void start(MultiBufferSource buffer, List<Ins> list) {
		boolean additive = YHModConfig.CLIENT.laserRenderAdditive.get();
		boolean invert = YHModConfig.CLIENT.laserRenderInverted.get();
		double tran = YHModConfig.CLIENT.laserTransparency.get();
		int col = (color & 0xffffff) | (((int) (tran * 255.9)) << 24);
		int add = (int) ((color & 0xff) * tran) |
				(int) ((color >> 8 & 0xff) * tran) << 8 |
				(int) ((color >> 16 & 0xff) * tran) << 16 | 0xff000000;
		int n = list.size() * 4;
		BulkDataWriter vc;
		vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(inner, DisplayType.SOLID)), n);
		for (var e : list) {
			e.texInner(vc, -1);
		}
		vc.flush();
		if (additive) {
			vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.ADDITIVE)), n);
			for (var e : list) {
				e.texOuter(false, vc, add);
			}
			vc.flush();
		}
		if (invert) {
			vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.TRANSPARENT)), n);
			for (var e : list) {
				e.texOuter(true, vc, col);
			}
			vc.flush();
		}
		if (!additive && !invert) {
			vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.TRANSPARENT)), n);
			for (var e : list) {
				e.texOuter(false, vc, col);
			}
			vc.flush();
		}
	}

	@Override
	public void create(Consumer<Ins> holder, ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		holder.accept(new Ins(Cache.vertex(pose.last().pose(), 0.167f, 0.5f)));
	}

	public record Ins(Cache cache) {

		public void texInner(BulkDataWriter vc, int color) {
			renderPart(false, vc, color, cache.r0);
		}

		public void texOuter(boolean invert, BulkDataWriter vc, int color) {
			renderPart(invert, vc, color, cache.r1);
		}

		private void renderPart(boolean invert, BulkDataWriter vc, int color, float[][] arr) {
			renderQuad(invert, vc, color, arr, 0, 2);
			renderQuad(invert, vc, color, arr, 3, 1);
			renderQuad(invert, vc, color, arr, 2, 3);
			renderQuad(invert, vc, color, arr, 1, 0);
		}

		private void renderQuad(boolean invert, BulkDataWriter vc, int col, float[][] arr, int i0, int i1) {
			if (invert) {
				addVertex(vc, col, arr[i1 + 4], 0, 0);
				addVertex(vc, col, arr[i1], 0, 1);
				addVertex(vc, col, arr[i0], 1, 1);
				addVertex(vc, col, arr[i0 + 4], 1, 0);
			} else {
				addVertex(vc, col, arr[i0 + 4], 1, 0);
				addVertex(vc, col, arr[i0], 1, 1);
				addVertex(vc, col, arr[i1], 0, 1);
				addVertex(vc, col, arr[i1 + 4], 0, 0);
			}
		}

		private void addVertex(BulkDataWriter vc, int col, float[] arr, float u, float v) {
			vc.addVertex(arr[0], arr[1], arr[2], u, v, col);
		}

	}

	public record Cache(float[][] r0, float[][] r1) {

		private static Cache vertex(Matrix4f mat, float s0, float s1) {
			var p0 = new Vector4f(0, 0, 0, 1).mul(mat);
			var px = new Vector4f(1, 0, 0, 0).mul(mat);
			var py = new Vector4f(0, 1, 0, 0).mul(mat);
			var pz = new Vector4f(0, 0, 1, 0).mul(mat);
			var ans = new Cache(new float[8][3], new float[8][3]);
			fill(ans.r0, p0, px, py, pz, s0);
			fill(ans.r1, p0, px, py, pz, s1);
			return ans;
		}

		private static void fill(float[][] arr, Vector4f p0, Vector4f px, Vector4f py, Vector4f pz, float scale) {
			calc(arr[0], p0, px, pz, -scale, -scale);
			calc(arr[1], p0, px, pz, scale, -scale);
			calc(arr[2], p0, px, pz, -scale, scale);
			calc(arr[3], p0, px, pz, scale, scale);
			add(arr[4], arr[0], py);
			add(arr[5], arr[1], py);
			add(arr[6], arr[2], py);
			add(arr[7], arr[3], py);
		}

		private static void calc(float[] arr, Vector4f p0, Vector4f px, Vector4f pz, float sx, float sz) {
			arr[0] = p0.x + px.x * sx + pz.x * sz;
			arr[1] = p0.y + px.y * sx + pz.y * sz;
			arr[2] = p0.z + px.z * sx + pz.z * sz;
		}

		private static void add(float[] arr, float[] base, Vector4f p) {
			arr[0] = base[0] + p.x;
			arr[1] = base[1] + p.y;
			arr[2] = base[2] + p.z;
		}

	}

}
