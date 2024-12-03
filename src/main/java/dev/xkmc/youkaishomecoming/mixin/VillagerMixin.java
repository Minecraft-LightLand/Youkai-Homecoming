package dev.xkmc.youkaishomecoming.mixin;

import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {

	public VillagerMixin(EntityType<? extends AbstractVillager> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}


	@Inject(at = @At("HEAD"), method = "onReputationEventFrom", cancellable = true)
	public void youkaishomecoming$onReputationEvent(ReputationEventType pType, Entity pTarget, CallbackInfo ci) {
		if (pType == ReputationEventType.VILLAGER_KILLED ||
				pType == ReputationEventType.VILLAGER_HURT ||
				pType == ReputationEventType.GOLEM_KILLED) {
			if (hasEffect(YHEffects.HYPNOSIS.get())) {
				ci.cancel();
			}
		}
	}

}
