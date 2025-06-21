package dev.xkmc.youkaishomecoming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "betteradvancements.common.util.CriterionGrid")
public class CriterionGridMixin {

	@WrapOperation(remap = false, method = "findOptimalCriterionGrid", at = @At(value = "INVOKE", remap = true,
			target = "Lnet/minecraft/network/chat/Component;translatableWithFallback(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
	private static MutableComponent youkaishomecoming$useTranslatable(String key, String alt, Operation<MutableComponent> original) {
		if (alt.startsWith("youkaishomecoming:")) {
			var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(alt));
			if (item != null) return Component.translatable(item.getDescriptionId());
		}
		return original.call(key, alt);
	}

}
