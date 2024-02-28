package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract boolean hasEffect(MobEffect pEffect);

	@Inject(at = @At("HEAD"), method = "canBeSeenAsEnemy", cancellable = true)
	public void youkaihomecoming$canBeSeenAsEnemy$unconscious(CallbackInfoReturnable<Boolean> cir) {
		if (hasEffect(YHEffects.UNCONSCIOUS.get())) {
			cir.setReturnValue(false);
		}
	}

}
