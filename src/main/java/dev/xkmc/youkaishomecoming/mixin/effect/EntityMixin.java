package dev.xkmc.youkaishomecoming.mixin.effect;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.events.GeneralEventHandlers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
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

	@WrapOperation(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getInputVector(Lnet/minecraft/world/phys/Vec3;FF)Lnet/minecraft/world/phys/Vec3;"))
	public Vec3 youkaishomecoming$craby(Vec3 vec3, float speed, float yrot, Operation<Vec3> original) {
		if (((Object) this) instanceof LivingEntity le) {
			var ins = le.getEffect(YHEffects.CRABY.get());
			if (ins != null) {
				if (vec3.lengthSqr() > 1) vec3 = vec3.normalize();
				vec3 = new Vec3(vec3.x * (1 + (1 + ins.getAmplifier()) * 0.5), vec3.y, vec3.z);
				double nv2 = vec3.lengthSqr();
				if (nv2 > 1) {
					speed = (float) (speed * Math.sqrt(nv2));
				}
			}
		}
		return original.call(vec3, speed, yrot);
	}

}
