package dev.xkmc.youkaishomecoming.mixin.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.youkaishomecoming.events.StructureHandler;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin {

	@WrapOperation(method = "lambda$forStructuresInChunk$2", at = @At(value = "INVOKE",
			target = "Lit/unimi/dsi/fastutil/objects/ObjectList;add(Ljava/lang/Object;)Z", remap = false))
	private static boolean youkaishomecoming$add(
			ObjectList instance, Object obj, Operation<Boolean> original,
			@Local(argsOnly = true) StructureStart start
	) {
		if (start.getStructure() instanceof JigsawStructure jigsaw && (obj instanceof Beardifier.Rigid rigid)) {
			if (((JigsawStructureAccessor) (Object) jigsaw).getStartPool().unwrapKey()
					.map(e -> e.location().toString().startsWith("youkaishomecoming:hakurei_shrine"))
					.orElse(false)) {
				((StructureHandler.BoxTagger) rigid.box()).youkaihomecoming$setTag("super_beard");
			}
		}
		return original.call(instance, obj);
	}

	@WrapOperation(method = "compute", at = @At(value = "INVOKE", ordinal = 0,
			target = "Lnet/minecraft/world/level/levelgen/Beardifier;getBeardContribution(IIII)D"))
	public double youkaishomecoming$extraBearding(
			int mx, int my, int mz, int h, Operation<Double> original,
			@Local BoundingBox box
	) {
		String tag = ((StructureHandler.BoxTagger) box).youkaihomecoming$getTag();
		if (tag != null && tag.equals("super_beard"))
			return StructureHandler.getCustomBeard(mx, my, mz, h);
		return original.call(mx, my, mz, h);
	}


}
