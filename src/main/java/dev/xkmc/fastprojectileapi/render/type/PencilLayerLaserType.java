package dev.xkmc.fastprojectileapi.render.type;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.core.BulkDataWriter;
import dev.xkmc.fastprojectileapi.render.core.DanmakuRenderStates;
import dev.xkmc.fastprojectileapi.render.core.DisplayType;
import dev.xkmc.fastprojectileapi.render.core.ProjectileRenderer;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;
import java.util.function.Consumer;

public record PencilLayerLaserType(ResourceLocation inner, ResourceLocation outer, int color)
		implements RenderableProjectileType<PencilLayerLaserType, PencilLayerLaserType.Ins> {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void start(MultiBufferSource buffer, List<Ins> list) {
		boolean additive = YHModConfig.CLIENT.laserRenderAdditive.get();
		boolean invert = YHModConfig.CLIENT.laserRenderInverted.get();
		int n = list.size() * 4;
		int count = 1;
		if (invert || !additive) count++;
		BulkDataWriter vc;
		vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(inner, DisplayType.TRANSPARENT)), n * count);
		for (var e : list) {
			e.texInner(vc, e.core);
		}
		if (invert || !additive) {
			for (var e : list) {
				e.texOuter(invert, vc, e.tran);
			}
		}
		vc.flush();
		if (additive) {
			vc = new BulkDataWriter(buffer.getBuffer(DanmakuRenderStates.laser(outer, DisplayType.ADDITIVE)), n);
			for (var e : list) {
				e.texOuter(false, vc, e.add);
			}
			vc.flush();
		}
	}

	@Override
	public void create(Consumer<Ins> holder, ProjectileRenderer<?> r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		double fade = r.fading(e);
		double tran = fade * YHModConfig.CLIENT.laserTransparency.get();
		int core = (int) (fade * 0xff) << 24 | 0xffffff;
		int outer = (int) (tran * 0xff) << 24 | (color & 0xffffff);
		int add = (int) ((color & 0xff) * tran) |
				(int) ((color >> 8 & 0xff) * tran) << 8 |
				(int) ((color >> 16 & 0xff) * tran) << 16 | 0xff000000;
		var data = Cache.vertex(pose.last().pose());
		holder.accept(new Ins(data, core, outer, add));
	}

	public record Ins(Cache cache, int core, int tran, int add) {

		public void texInner(BulkDataWriter vc, int color) {
			renderPart(false, vc, color, cache.r0);
		}

		public void texOuter(boolean invert, BulkDataWriter vc, int color) {
			renderPart(invert, vc, color, cache.r1);
		}

		private void renderPart(boolean invert, BulkDataWriter vc, int color, float[][] arr) {
			renderConeStart(invert, vc, color, arr, 0, 12);
			renderCube(invert, vc, color, arr, 0);
			renderCube(invert, vc, color, arr, 4);
			renderConeEnd(invert, vc, color, arr, 8, 13);
		}

		private void renderCube(boolean invert, BulkDataWriter vc, int color, float[][] arr, int start) {
			int i0 = start;
			int i1 = start + 1;
			int i2 = start + 2;
			int i3 = start + 3;
			int i4 = start + 4;
			int i5 = start + 5;
			int i6 = start + 6;
			int i7 = start + 7;
			renderQuad(invert, vc, color, arr, i0, i2, i4, i6);
			renderQuad(invert, vc, color, arr, i3, i1, i7, i5);
			renderQuad(invert, vc, color, arr, i2, i3, i6, i7);
			renderQuad(invert, vc, color, arr, i1, i0, i5, i4);
		}

		private void renderConeStart(boolean invert, BulkDataWriter vc, int color, float[][] arr, int start, int vertex) {
			int v = vertex;
			int i4 = start;
			int i5 = start + 1;
			int i6 = start + 2;
			int i7 = start + 3;
			renderQuad(invert, vc, color, arr, v, v, i4, i6);
			renderQuad(invert, vc, color, arr, v, v, i7, i5);
			renderQuad(invert, vc, color, arr, v, v, i6, i7);
			renderQuad(invert, vc, color, arr, v, v, i5, i4);
		}

		private void renderConeEnd(boolean invert, BulkDataWriter vc, int color, float[][] arr, int start, int vertex) {
			int v = vertex;
			int i0 = start;
			int i1 = start + 1;
			int i2 = start + 2;
			int i3 = start + 3;
			renderQuad(invert, vc, color, arr, i0, i2, v, v);
			renderQuad(invert, vc, color, arr, i3, i1, v, v);
			renderQuad(invert, vc, color, arr, i2, i3, v, v);
			renderQuad(invert, vc, color, arr, i1, i0, v, v);
		}

		private void renderQuad(boolean invert, BulkDataWriter vc, int col, float[][] arr, int i0, int i1, int i2, int i3) {
			if (invert) {
				addVertex(vc, col, arr[i3], 0, 0);
				addVertex(vc, col, arr[i1], 0, 1);
				addVertex(vc, col, arr[i0], 1, 1);
				addVertex(vc, col, arr[i2], 1, 0);
			} else {
				addVertex(vc, col, arr[i2], 1, 0);
				addVertex(vc, col, arr[i0], 1, 1);
				addVertex(vc, col, arr[i1], 0, 1);
				addVertex(vc, col, arr[i3], 0, 0);
			}
		}

		private void addVertex(BulkDataWriter vc, int col, float[] arr, float u, float v) {
			vc.addVertex(arr[0], arr[1], arr[2], u, v, col);
		}

	}

	public record Cache(float[][] r0, float[][] r1) {

		private static Cache vertex(Matrix4f mat) {
			var p0 = new Vector4f(0, 0, 0, 1).mul(mat);
			var px = new Vector4f(1, 0, 0, 0).mul(mat);
			var py = new Vector4f(0, 1, 0, 0).mul(mat);
			var pz = new Vector4f(0, 0, 1, 0).mul(mat);
			var ans = new Cache(new float[14][3], new float[14][3]);
			fill(ans.r0, p0, px, py, pz, 1f / 6, 1f / 8, 1f / 4, 1f / 2);
			fill(ans.r1, p0, px, py, pz, 1f / 3, 1f / 4, 1f / 4, 7f / 8);
			return ans;
		}

		private static void fill(float[][] arr, Vector4f p0, Vector4f px, Vector4f py, Vector4f pz, float r0, float r1, float dy, float end) {
			fill(0, arr, p0, px, py, pz, r1, 0.5f - dy);
			fill(4, arr, p0, px, py, pz, r0, 0.5f);
			fill(8, arr, p0, px, py, pz, r1, 0.5f + dy);
			end(arr[12], p0, py, 0.5f - end);
			end(arr[13], p0, py, 0.5f + end);
		}

		private static void fill(int offset, float[][] arr, Vector4f p0, Vector4f px, Vector4f py, Vector4f pz, float r0, float dy) {
			calc(arr[offset], p0, px, pz, py, -r0, -r0, dy);
			calc(arr[offset + 1], p0, px, pz, py, r0, -r0, dy);
			calc(arr[offset + 2], p0, px, pz, py, -r0, r0, dy);
			calc(arr[offset + 3], p0, px, pz, py, r0, r0, dy);
		}

		private static void calc(float[] arr, Vector4f p0, Vector4f px, Vector4f pz, Vector4f py, float sx, float sz, float dy) {
			arr[0] = p0.x + px.x * sx + pz.x * sz + py.x * dy;
			arr[1] = p0.y + px.y * sx + pz.y * sz + py.y * dy;
			arr[2] = p0.z + px.z * sx + pz.z * sz + py.z * dy;
		}

		private static void end(float[] arr, Vector4f p0, Vector4f py, float dy) {
			arr[0] = p0.x + py.x * dy;
			arr[1] = p0.y + py.y * dy;
			arr[2] = p0.z + py.z * dy;
		}

	}

}
