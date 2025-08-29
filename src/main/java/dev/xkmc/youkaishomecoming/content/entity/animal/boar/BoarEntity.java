package dev.xkmc.youkaishomecoming.content.entity.animal.boar;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.StateMachineMob;
import dev.xkmc.youkaishomecoming.content.entity.youkai.SyncedData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public class BoarEntity extends Animal implements StateMachineMob {

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(BoarEntity.class, ser);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.25);
	}

	private static final SyncedData DATA = new SyncedData(BoarEntity::defineId);
	private static final Lazy<Ingredient> FOOD_ITEMS = Lazy.of(() -> Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT));

	static final EntityDataAccessor<Integer> TRANSIENT_DATA = DATA.define(SyncedData.INT, 0, null);
	static final EntityDataAccessor<Integer> FLAGS = DATA.define(SyncedData.INT, 0, "flags");
	static final EntityDataAccessor<Integer> VARIANT = DATA.define(SyncedData.INT, 0, "variant");

	public final BoarStateMachine states = new BoarStateMachine(this);
	public final BoarProperties prop = new BoarProperties(this);

	protected BoarPanicGoal panic;
	protected BoarEatBlockGoal eat;

	public BoarEntity(EntityType<? extends BoarEntity> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
		panic = new BoarPanicGoal(this, 1.25D);
		eat = new BoarEatBlockGoal(this);

		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, panic);
		goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS.get(), false));
		goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(6, eat);
		goalSelector.addGoal(7, new BoarSleepGoal(this));
		goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(13, new RandomLookAroundGoal(this));
		goalSelector.addGoal(14, new BoarWobbleGoal(this));

	}

	// core

	protected SyncedData data() {
		return DATA;
	}

	protected SynchedEntityData entityData() {
		return entityData;
	}

	public BoarStateMachine states() {
		return states;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		data().register(entityData);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		data().write(tag, entityData);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data().read(tag, entityData);
	}

	@Override
	public void handleEntityEvent(byte data) {
		if (data == EntityEvent.ARMORSTAND_WOBBLE) {
			states.wobble.stop();
			states.wobble.startIfStopped(tickCount);
		}
		if (states.transitionTo(data)) {
			return;
		}
		super.handleEntityEvent(data);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float val) {
		super.actuallyHurt(source, val);
		states.onHurt();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		states.tick();
	}

	// states

	public void ate() {
		heal(1);
		super.ate();
		if (isBaby()) {
			ageUp(60);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		var ans = super.getDimensions(pose);
		if (states().isSleeping())
			ans = ans.scale(1, 0.7f);
		return ans;
	}


	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData group, @Nullable CompoundTag data) {
		var ans = super.finalizeSpawn(level, ins, type, group, data);
		return ans;
	}

	// animal properties

	protected SoundEvent getAmbientSound() {
		return SoundEvents.PIG_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.PIG_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.PIG_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
	}

	@Nullable
	public BoarEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
		BoarEntity ans = YHEntities.BOAR.create(level);
		if (ans == null) return null;
		ans.prop.setVariant(prop.getVariant());
		return ans;
	}

	public boolean isFood(ItemStack stack) {
		return FOOD_ITEMS.get().test(stack);
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.8F * getEyeHeight(), getBbWidth() * 0.4F);
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) {
		return dim.height * 0.85f;
	}

}
