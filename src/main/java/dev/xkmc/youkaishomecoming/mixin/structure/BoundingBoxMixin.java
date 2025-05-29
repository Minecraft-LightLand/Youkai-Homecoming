package dev.xkmc.youkaishomecoming.mixin.structure;

import dev.xkmc.youkaishomecoming.events.StructureHandler;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoundingBox.class)
public class BoundingBoxMixin implements StructureHandler.BoxTagger {

	@Unique
	private String youkaihomecoming$tag;

	@Override
	public void youkaihomecoming$setTag(String str) {
		youkaihomecoming$tag = str;
	}

	@Override
	public @Nullable String youkaihomecoming$getTag() {
		return youkaihomecoming$tag;
	}

}
