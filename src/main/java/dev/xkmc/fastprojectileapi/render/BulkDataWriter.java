package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class BulkDataWriter {

	private final VertexConsumer vc;
	private final BufferBuilder direct;
	private final ByteBuffer buffer;

	public BulkDataWriter(VertexConsumer vc, int size) {
		this.vc = vc;
		direct = vc instanceof BufferBuilder buf && false ? buf : null;
		buffer = direct == null ? null : ByteBuffer.allocate(size * 6 * 4 * 4);
	}


	public void addVertex(Matrix4f m4, float x, float y, float z, float u, float v, int col) {
		var vec = new Vector4f(x, y, z, 1).mul(m4);
		addVertex(vec.x, vec.y, vec.z, u, v, col);
	}

	public void addVertex(float x, float y, float z, float u, float v, int col) {
		if (buffer == null) {
			vc.vertex(x, y, z).uv(u, v).color(col).endVertex();
		} else {
			buffer.putFloat(x);
			buffer.putFloat(y);
			buffer.putFloat(z);
			buffer.putFloat(u);
			buffer.putFloat(v);
			buffer.putInt(col << 8 | col >>> 24);
		}
	}


	public void flush() {
		if (direct != null && buffer != null) {
			direct.putBulkData(buffer);
		}
	}

}
