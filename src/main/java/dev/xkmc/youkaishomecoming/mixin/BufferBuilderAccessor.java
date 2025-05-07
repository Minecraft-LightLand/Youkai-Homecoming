package dev.xkmc.youkaishomecoming.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {

	@Accessor
	int getNextElementByte();

	@Accessor
	void setNextElementByte(int index);

	@Accessor
	int getVertices();

	@Accessor
	void setVertices(int vert);

}
