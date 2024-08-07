package dev.xkmc.youkaishomecoming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.ShootTongue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShootTongue.class)
public class ShootTongueMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Frog;canEat(Lnet/minecraft/world/entity/LivingEntity;)Z"),
			method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/frog/Frog;)Z")
	private boolean youkaishomecoming$canEat(LivingEntity slime, Operation<Boolean> original, ServerLevel level, Frog frog) {
		if (FrogGodCapability.canEatSpecial(frog, slime)) {
			return true;
		}
		return original.call(slime);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V"),
			method = "eatEntity")
	private void youkaishomecoming$eatTarget(Entity instance, Entity.RemovalReason pReason, Operation<Void> original, ServerLevel pLevel, Frog frog) {
		FrogGodCapability.onEatTarget(frog, instance);
		original.call(instance, pReason);
	}

}
