package dev.xkmc.fastprojectileapi.render.core;

import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;

public class BulkDataWriter {

	private final VertexConsumer vc;
	private final BufferBuilder direct;
	private final ByteBuffer buffer;

	public BulkDataWriter(VertexConsumer vc, int size) {
		this.vc = vc;
		direct = vc instanceof BufferBuilder buf ? buf : null;
		buffer = direct == null ? null : MemoryTracker.create(size * 6 * 4 * 4);
	}


	public void addVertex(Matrix4f m4, float x, float y, float z, float u, float v, int col) {
		var vec = new Vector4f(x, y, z, 1).mul(m4);
		addVertex(vec.x, vec.y, vec.z, u, v, col);
	}

	public void addVertex(float x, float y, float z, float u, float v, int col) {
		if (buffer == null) {
			//vc.vertex(x, y, z).uv(u, v).color(col).endVertex();
		} else if (true) {
			buffer.putFloat(x);
			buffer.putFloat(y);
			buffer.putFloat(z);
			buffer.putFloat(u);
			buffer.putFloat(v);
			buffer.put((byte) FastColor.ARGB32.red(col));
			buffer.put((byte) FastColor.ARGB32.green(col));
			buffer.put((byte) FastColor.ARGB32.blue(col));
			buffer.put((byte) FastColor.ARGB32.alpha(col));
		}
	}


	public void flush() {
		if (direct != null && buffer != null) {
			buffer.position(0);
			direct.putBulkData(buffer);
		}
	}

}
