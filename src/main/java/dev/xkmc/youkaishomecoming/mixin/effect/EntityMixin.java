package dev.xkmc.youkaishomecoming.mixin.effect;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.events.GeneralEventHandlers;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(at = @At("HEAD"), method = "dampensVibrations", cancellable = true)
	public void youkaishomecoming$dampensVibration(CallbackInfoReturnable<Boolean> cir) {
		Entity self = Wrappers.cast(this);
		if (GeneralEventHandlers.supressVibration(self)) {
			cir.setReturnValue(true);
		}
	}

}
