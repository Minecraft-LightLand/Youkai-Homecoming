package dev.xkmc.youkaishomecoming.content.entity.danmaku;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class DanmakuPoofParticleOptions extends DustParticleOptionsBase {

	public static final Codec<DanmakuPoofParticleOptions> CODEC = RecordCodecBuilder.create(i -> i.group(
			ExtraCodecs.VECTOR3F.fieldOf("color").forGetter((e) -> e.color),
			Codec.FLOAT.fieldOf("scale").forGetter((e) -> e.scale)
	).apply(i, DanmakuPoofParticleOptions::new));

	public static final ParticleOptions.Deserializer<DanmakuPoofParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
		public DanmakuPoofParticleOptions fromCommand(ParticleType<DanmakuPoofParticleOptions> type, StringReader reader) throws CommandSyntaxException {
			Vector3f vector3f = DustParticleOptionsBase.readVector3f(reader);
			reader.expect(' ');
			float f = reader.readFloat();
			return new DanmakuPoofParticleOptions(vector3f, f);
		}

		public DanmakuPoofParticleOptions fromNetwork(ParticleType<DanmakuPoofParticleOptions> type, FriendlyByteBuf buf) {
			return new DanmakuPoofParticleOptions(DustParticleOptionsBase.readVector3f(buf), buf.readFloat());
		}
	};

	public DanmakuPoofParticleOptions(Vector3f col, float scale) {
		super(col, scale);
	}

	public ParticleType<DanmakuPoofParticleOptions> getType() {
		return YHDanmaku.POOF.get();
	}

}