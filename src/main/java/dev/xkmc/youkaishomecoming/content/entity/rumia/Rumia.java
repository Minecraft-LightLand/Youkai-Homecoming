package dev.xkmc.youkaishomecoming.content.entity.rumia;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.floating.FloatingYoukai;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SerialClass
public class Rumia extends FloatingYoukai {

	@SerialClass.SerialField
	final RumiaStateMachine state = new RumiaStateMachine();

	public Rumia(EntityType<? extends Rumia> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(4, new RumiaAttackGoal(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.FLYING_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 48);
	}

	@Override
	protected void actuallyHurt(DamageSource pDamageSource, float amount) {
		super.actuallyHurt(pDamageSource, Math.min(8, amount));//TODO config
	}

	@Override
	public void aiStep() {
		super.aiStep();
		state.tick(this);
	}

	public boolean isCharged() {
		return state.isCharged(this);
	}

	public boolean isBlocked() {
		return state.isBlocked(this);
	}

}