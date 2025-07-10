package dev.xkmc.youkaishomecoming.mixin.effect;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
	public void youkaishomecoming$canBeSeenAsEnemy$unconscious(CallbackInfoReturnable<Boolean> cir) {
		if (hasEffect(YHEffects.UNCONSCIOUS.get())) {
			cir.setReturnValue(false);
		}
	}

	@WrapOperation(method = "addEatEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
	public boolean youkaishomecoming$addEffect(LivingEntity user, MobEffectInstance ins, Operation<Boolean> original) {
		return original.call(user, EffectEventHandlers.onEat(user, ins));
	}

}
