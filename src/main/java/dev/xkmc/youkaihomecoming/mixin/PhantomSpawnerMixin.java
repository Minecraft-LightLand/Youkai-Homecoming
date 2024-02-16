package dev.xkmc.youkaihomecoming.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.youkaihomecoming.events.GeneralEventHandlers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"), method = "tick")
	public boolean youkaihomecoming$phantomSpawn$cancel(ServerPlayer player, Operation<Boolean> original) {
		return !GeneralEventHandlers.preventPhantomSpawn(player) && original.call(player);
	}

}
