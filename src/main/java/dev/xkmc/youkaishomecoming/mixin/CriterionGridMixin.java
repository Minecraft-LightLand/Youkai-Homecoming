package dev.xkmc.youkaishomecoming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "betteradvancements.common.util.CriterionGrid")
public class CriterionGridMixin {

	@WrapOperation(remap = false, method = "findOptimalCriterionGrid", at = @At(value = "INVOKE", remap = true,
			target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
	private static MutableComponent youkaishomecoming$useTranslatable(String text, Operation<MutableComponent> original) {
		if (text.startsWith(YoukaisHomecoming.MODID + ":")) {//TODO tag
			var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(text));
			return Component.translatable(item.getDescriptionId());
		}
		return original.call(text);
	}

}
