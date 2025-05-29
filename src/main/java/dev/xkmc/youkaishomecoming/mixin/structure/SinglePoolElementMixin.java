package dev.xkmc.youkaishomecoming.mixin.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SinglePoolElement.class)
public class SinglePoolElementMixin {

	@Shadow
	@Final
	protected Either<ResourceLocation, StructureTemplate> template;

	@WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/pools/SinglePoolElement;getSettings(Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Z)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"))
	public StructurePlaceSettings youkaishomcoming$noLiquid(
			SinglePoolElement instance, Rotation rot, BoundingBox box, boolean chain,
			Operation<StructurePlaceSettings> original) {
		var ans = original.call(instance, rot, box, chain);
		if (template.left().map(e -> e.getNamespace().equals(YoukaisHomecoming.MODID)).orElse(false)) {
			ans.setKeepLiquids(false);
		}
		return ans;
	}

}
