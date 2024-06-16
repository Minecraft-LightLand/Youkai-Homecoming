package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

	@Inject(at = @At("HEAD"), method = "unpackLootTable")
	public void youkaishomecoming$trayLoad(Player player, CallbackInfo ci) {
		if (player != null) {
			EffectEventHandlers.disableKoishi(player);
		}
	}

}
