package dev.xkmc.youkaishomecoming.content.entity.reimu;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.UUID;

@SerialClass
public class ReimuEntity extends MaidenEntity {

	@SerialClass.SerialField
	private final HashSet<UUID> verifiedPlayers = new HashSet<>();
	@SerialClass.SerialField
	private int feedCD = 0;

	public ReimuEntity(EntityType<? extends ReimuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (feedCD > 0) feedCD--;
		if (feedCD > 0) {
			setFlag(16, true);
		} else setFlag(16, false);
	}

	@Override
	protected void tickEffects() {
		super.tickEffects();
		if (getFlag(16)) {
			boolean flag;
			if (this.isInvisible()) {
				flag = this.random.nextInt(15) == 0;
			} else {
				flag = this.random.nextBoolean();
			}
			int i = MobEffects.SATURATION.getColor();
			if (flag) {
				double d0 = (i >> 16 & 255) / 255.0D;
				double d1 = (i >> 8 & 255) / 255.0D;
				double d2 = (i & 255) / 255.0D;
				level().addParticle(ParticleTypes.ENTITY_EFFECT,
						getRandomX(0.5D), getRandomY(), getRandomZ(0.5D),
						d0, d1, d2);
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEdible()) {
			var food = stack.getFoodProperties(this);
			if (food != null) {
				if (player instanceof ServerPlayer sp && feedCD == 0) {
					ItemStack remain = stack.getCraftingRemainingItem();
					stack.shrink(1);
					YHCriteriaTriggers.FEED_REIMU.trigger(sp, stack);
					feedCD += food.getNutrition() * 100;
					if (food.getEffects().stream().anyMatch(e -> e.getFirst().getEffect() == YHEffects.UDUMBARA.get())) {
						verifiedPlayers.add(sp.getUUID());
						YHCriteriaTriggers.REIMU_HAPPY.trigger(sp);
					}
					if (stack.getUseAnimation() == UseAnim.DRINK)
						playSound(stack.getDrinkingSound());
					else playSound(stack.getEatingSound());
					if (!remain.isEmpty())
						sp.getInventory().placeItemBackInInventory(remain);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(player, hand);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		TouhouSpellCards.setReimu(this);
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

}
