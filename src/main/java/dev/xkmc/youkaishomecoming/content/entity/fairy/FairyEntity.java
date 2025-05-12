package dev.xkmc.youkaishomecoming.content.entity.fairy;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class FairyEntity extends GeneralYoukaiEntity {

	private static final ResourceLocation SPELL_RUMIA = YoukaisHomecoming.loc("rumia");

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.ATTACK_DAMAGE, 4);
	}

	public FairyEntity(EntityType<? extends FairyEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	protected boolean wouldAttack(LivingEntity entity) {
		if (shouldIgnore(entity)) return false;
		if (entity.hasEffect(YHEffects.FAIRY.get()))
			return false;
		return YHModConfig.COMMON.fairyAttackYoukaified.get() && EffectEventHandlers.isYoukai(entity);
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		return super.shouldHurt(le) || (!(le instanceof YoukaiEntity)) && le instanceof Mob mob && mob.getTarget() != null;
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowSpellCircle()) {
			return null;
		}
		return SPELL_RUMIA;
	}

	public void onDanmakuHit(LivingEntity e, IYHDanmaku danmaku) {
	}

	@Override
	public void die(DamageSource source) {
		boolean prev = dead;
		super.die(source);
		var e = source.getEntity();
		if (!prev && dead && e instanceof LivingEntity le && !source.is(YHDamageTypes.DANMAKU_TYPE)) {
			if (!e.isAlive() || !e.isAddedToWorld() || e.isRemoved())
				return;
			trySummonReinforcementOnDeath(le);
		}
	}

	@Override
	public float percentageDamage(LivingEntity le) {
		return super.percentageDamage(le) / 2;
	}

	@Override
	public void trySummonReinforcementOnDeath(LivingEntity le) {
		TouhouConditionalSpawns.triggetFairyReinforcement(this, le, position());
	}

}
