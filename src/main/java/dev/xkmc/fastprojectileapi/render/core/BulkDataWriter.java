package dev.xkmc.fastprojectileapi.render.core;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.youkaishomecoming.mixin.BufferBuilderAccessor;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class BulkDataWriter {

	private final VertexConsumer vc;
	private final BufferBuilder direct;

	public BulkDataWriter(VertexConsumer vc, int size) {
		this.vc = vc;
		direct = vc instanceof BufferBuilder buf ? buf : null;
	}

	public void addVertex(Matrix4f m4, float x, float y, float z, float u, float v, int col) {
		var vec = new Vector4f(x, y, z, 1).mul(m4);
		addVertex(vec.x, vec.y, vec.z, u, v, col);
	}

	public void addVertex(float x, float y, float z, float u, float v, int col) {
		if (direct == null) {
			vc.vertex(x, y, z).uv(u, v).color(col).endVertex();
		} else {
			direct.putFloat(0, x);
			direct.putFloat(4, y);
			direct.putFloat(8, z);
			direct.putFloat(12, u);
			direct.putFloat(16, v);
			direct.putByte(20, (byte) FastColor.ARGB32.red(col));
			direct.putByte(21, (byte) FastColor.ARGB32.green(col));
			direct.putByte(22, (byte) FastColor.ARGB32.blue(col));
			direct.putByte(23, (byte) FastColor.ARGB32.alpha(col));
			((BufferBuilderAccessor) direct).setNextElementByte(((BufferBuilderAccessor) direct).getNextElementByte() + 24);
			((BufferBuilderAccessor) direct).setVertices(((BufferBuilderAccessor) direct).getVertices() + 1);
		}
	}

	public void flush() {
	}

}
