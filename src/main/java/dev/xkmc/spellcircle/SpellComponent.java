package dev.xkmc.spellcircle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SerialClass
public class SpellComponent {

	@Nullable
	public static SpellComponent getFromConfig(String s) {
		return YoukaisHomecoming.SPELL.getMerged().map.get(s);
	}

	@SerialClass.SerialField
	public ArrayList<Stroke> strokes = new ArrayList<>();

	@SerialClass.SerialField
	public ArrayList<Layer> layers = new ArrayList<>();

	@OnlyIn(Dist.CLIENT)
	public void render(RenderHandle handle) {
		handle.matrix.pushPose();
		for (Stroke stroke : strokes) {
			stroke.render(handle);
		}
		for (Layer layer : layers) {
			layer.render(handle);
		}
		handle.matrix.popPose();
	}

	@SerialClass
	public static class Value {

		@SerialClass.SerialField
		public float value, delta, amplitude, period, dt;

		@OnlyIn(Dist.CLIENT)
		public float get(float tick) {
			float ans = value + delta * tick;
			if (period > 0) ans += amplitude * (float) Math.sin((tick - dt) * 2 * Math.PI / period);
			return ans;
		}

	}

	@SerialClass
	public static class Stroke {

		@SerialClass.SerialField
		public int vertex, cycle = 1;

		@SerialClass.SerialField
		public String color;

		@SerialClass.SerialField
		public float width, radius, z, angle;

		@OnlyIn(Dist.CLIENT)
		public void render(RenderHandle handle) {
			float da = (float) Math.PI * 2 * cycle / vertex;
			float a = angle;
			float w = width / (float) Math.cos(da / 2);
			int col = getColor();
			for (int i = 0; i < vertex; i++) {
				rect(handle, a, da, radius, w, z, col);
				a += da;
			}

		}

		@OnlyIn(Dist.CLIENT)
		private int getColor() {
			if (color == null) return -1;
			String str = color;
			if (str.startsWith("0x")) {
				str = str.substring(2);
			}
			return Integer.parseUnsignedInt(str, 16);
		}

		@OnlyIn(Dist.CLIENT)
		private static void rect(RenderHandle handle, float a, float da, float r, float w, float z, int col) {
			vertex(handle, a, r - w / 2, z, col);
			vertex(handle, a, r + w / 2, z, col);
			vertex(handle, a + da, r + w / 2, z, col);
			vertex(handle, a + da, r - w / 2, z, col);
		}

		@OnlyIn(Dist.CLIENT)
		private static void vertex(RenderHandle handle, float a, float r, float z, int col) {
			int alp = (int) ((col >> 24 & 0xff) * handle.alpha);
			handle.builder.vertex(handle.matrix.last().pose(),
							r * (float) Math.cos(a),
							r * (float) Math.sin(a),
							z).color(
							col >> 16 & 0xff,
							col >> 8 & 0xff,
							col & 0xff,
							alp)
					.endVertex();
		}

	}

	@SerialClass
	public static class Layer {

		@SerialClass.SerialField
		public ArrayList<String> children = new ArrayList<>();

		private List<SpellComponent> _children;

		@Nullable
		@SerialClass.SerialField
		public Value z_offset, scale, radius, rotation, alpha;

		@OnlyIn(Dist.CLIENT)
		public void render(RenderHandle handle) {
			if (_children == null) {
				_children = children.stream().map(SpellComponent::getFromConfig).collect(Collectors.toList());
				return;
			}
			int n = _children.size();
			float z = get(z_offset, handle, 0);
			float s = get(scale, handle, 1);
			float a = get(rotation, handle, 0);
			double r = get(radius, handle, 0);
			float al = handle.alpha;
			if (alpha != null) {
				handle.alpha *= alpha.get(handle.tick);
			}
			handle.matrix.pushPose();
			handle.matrix.translate(0, 0, z);
			handle.matrix.scale(s, s, s);
			for (SpellComponent child : _children) {
				handle.matrix.pushPose();
				handle.matrix.mulPose(Axis.ZP.rotationDegrees(a));
				handle.matrix.translate(r, 0, 0);
				child.render(handle);
				handle.matrix.popPose();
				a += 360f / n;
			}
			handle.matrix.popPose();
			handle.alpha = al;
		}

		@OnlyIn(Dist.CLIENT)
		private float get(@Nullable Value val, RenderHandle handle, float def) {
			return val == null ? def : val.get(handle.tick);
		}


	}


	@OnlyIn(Dist.CLIENT)
	public static class RenderHandle {

		public final PoseStack matrix;
		public final VertexConsumer builder;
		public final float tick;
		public final int light;

		public float alpha = 1;

		public RenderHandle(PoseStack matrix, VertexConsumer builder, float tick, int light) {
			this.matrix = matrix;
			this.builder = builder;
			this.tick = tick;
			this.light = light;
		}
	}

}
