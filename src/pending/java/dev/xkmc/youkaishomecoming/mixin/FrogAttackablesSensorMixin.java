package dev.xkmc.youkaishomecoming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.FrogAttackablesSensor;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrogAttackablesSensor.class)
public class FrogAttackablesSensorMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Frog;canEat(Lnet/minecraft/world/entity/LivingEntity;)Z"),
			method = "isMatchingEntity")
	private boolean youkaishomecoming$canEat(LivingEntity slime, Operation<Boolean> original, LivingEntity attacker, LivingEntity target) {
		if (attacker instanceof Frog frog) {
			if (FrogGodCapability.canEatSpecial(frog, target)) {
				return true;
			}
		}
		return original.call(slime);
	}

}
