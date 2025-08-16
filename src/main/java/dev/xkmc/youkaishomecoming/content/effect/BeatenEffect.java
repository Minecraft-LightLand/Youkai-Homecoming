package dev.xkmc.youkaishomecoming.content.effect;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class BeatenEffect extends MobEffect {

    public BeatenEffect(MobEffectCategory category, int color) {
        super(category, color);
        String uuid = MathHelper.getUUIDFromString("beaten").toString();
        // Reduce max health by half (multiply by -0.5)
        addAttributeModifier(Attributes.MAX_HEALTH, uuid, -0.5, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            // Force player to crouch (1.20.1 compatible way)
            if(player.getHealth()>=5) {
                player.setHealth(player.getMaxHealth()/2);
            }
            if(Objects.requireNonNull(player.getEffect(YHEffects.BEATEN.get())).getDuration()>=600) {
                player.setSwimming(true);
}
            else{
                player.setSprinting(false);
                player.setForcedPose(null);
                }
        }else if (entity instanceof Mob mob) {
            if(Objects.requireNonNull(mob.getEffect(YHEffects.BEATEN.get())).getDuration()>20){
                entity.setPose(Pose.SLEEPING);
                mob.setNoAi(true);
            }
            else {
                if(Objects.requireNonNull(mob.getEffect(YHEffects.BEATEN.get())).getDuration()>1){
                    mob.setPose(Pose.STANDING);
                }
                else{
                mob.setNoAi(false);
                }
            }}

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        // Cannot be cured by milk bucket or other curative items
        return List.of();
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            MobEffectInstance beatenEffect = player.getEffect(YHEffects.BEATEN.get());
            if (beatenEffect != null) {
                float healAmount = event.getAmount();
                int durationReduction = (int) (healAmount * 20); // 5 * heal amount * 20 ticks per second

                int currentDuration = beatenEffect.getDuration();
                int newDuration = Math.max(0, currentDuration - durationReduction);

                if (newDuration <= 0) {
                    // Remove the effect completely
                    player.removeEffect(YHEffects.BEATEN.get());
                } else {
                    // Update the effect with reduced duration
                    player.removeEffect(YHEffects.BEATEN.get());
                    MobEffectInstance newEffect = new MobEffectInstance(
                            YHEffects.BEATEN.get(),
                            newDuration,
                            beatenEffect.getAmplifier(),
                            beatenEffect.isAmbient(),
                            beatenEffect.isVisible(),
                            beatenEffect.showIcon()
                    );
                    player.addEffect(newEffect);
                }
            }
        }
        if (entity instanceof Player player ){
            if(player.getHealth()>=0.5*player.getMaxHealth()&&player.getEffect(YHEffects.BEATEN.get())!=null){

                event.setAmount(0);}
            }



    }

    @SubscribeEvent
    public static void onMobEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect() == YHEffects.BEATEN.get() && event.getEntity() instanceof Player player) {
            // 当效果被移除时，重置玩家姿态

            player.setForcedPose(null);

        }
        if (event.getEffect() == YHEffects.BEATEN.get() &&event.getEntity() instanceof Mob mob) {
            // 当效果被移除时，恢复非玩家实体的 AI
            mob.setNoAi(false);
            mob.setPose(Pose.STANDING);
        }
    }
    // Note: onEffectStarted and onEffectRemoved don't exist in 1.20.1
    // Pose forcing is handled in applyEffectTick and through events
}