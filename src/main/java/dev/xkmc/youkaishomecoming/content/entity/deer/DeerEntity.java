package dev.xkmc.youkaishomecoming.content.entity.deer;

import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public class DeerEntity extends Animal {

	private static final Lazy<Ingredient> FOOD_ITEMS = Lazy.of(() -> Ingredient.of(YHFood.SAKURA_MOCHI.item));//TODO

	protected DeerEntity(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS.get(), false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes() {//TODO
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.PIG_AMBIENT;//TODO
	}

	protected SoundEvent getHurtSound(DamageSource p_29502_) {
		return SoundEvents.PIG_HURT;//TODO
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.PIG_DEATH;//TODO
	}

	protected void playStepSound(BlockPos p_29492_, BlockState p_29493_) {
		this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);//TODO
	}

	@Nullable
	public Pig getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return EntityType.PIG.create(level);//TODO
	}

	public boolean isFood(ItemStack stack) {
		return FOOD_ITEMS.get().test(stack);
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);//TODO
	}

}
