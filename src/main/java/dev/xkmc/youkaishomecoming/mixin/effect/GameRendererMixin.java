package dev.xkmc.youkaishomecoming.mixin.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.youkaishomecoming.events.ClientEventHandlers;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(at = @At("HEAD"), method = "bobHurt")
	public void youkaishomecoming$bobHurt$drunk(PoseStack pose, float pTick, CallbackInfo ci) {
		ClientEventHandlers.drunkView(pose, pTick);
	}

}
