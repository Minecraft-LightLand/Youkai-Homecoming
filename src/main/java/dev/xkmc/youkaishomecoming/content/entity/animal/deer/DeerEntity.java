package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import dev.xkmc.youkaishomecoming.content.entity.youkai.SyncedData;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public class DeerEntity extends Animal {

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(DeerEntity.class, ser);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.25);
	}

	private static final SyncedData DATA = new SyncedData(DeerEntity::defineId);
	private static final EntityDataAccessor<Integer> TRANSIENT_DATA = DATA.define(SyncedData.INT, 0, null);
	private static final EntityDataAccessor<Integer> FLAGS = DATA.define(SyncedData.INT, 0, "flags");
	private static final Lazy<Ingredient> FOOD_ITEMS = Lazy.of(() -> Ingredient.of(YHFood.SAKURA_MOCHI.item));//TODO

	public final DeerStateMachine states = new DeerStateMachine();

	protected DeerPanicGoal panic;
	protected DeerEatBlockGoal eat;

	public DeerEntity(EntityType<? extends DeerEntity> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
		panic = new DeerPanicGoal(this, 1.25D);
		eat = new DeerEatBlockGoal(this);

		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, panic);
		goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS.get(), false));
		goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(6, eat);
		goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(9, new RandomLookAroundGoal(this));
	}

	// core

	protected SyncedData data() {
		return DATA;
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
		if (states.handleEntityEvent(data)) {
			return;
		}
		super.handleEntityEvent(data);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float val) {
		super.actuallyHurt(source, val);
		states.onHurt(this);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		states.tick(this);
	}

	// states

	public void setPanic(boolean b) {
		int b0 = entityData.get(TRANSIENT_DATA);
		entityData.set(TRANSIENT_DATA, b ? (b0 | 1) : (b0 & ~1));
	}

	public boolean isPanic() {
		return (entityData.get(TRANSIENT_DATA) & 1) != 0;
	}

	public void setMale(boolean b) {
		int b0 = entityData.get(FLAGS);
		entityData.set(FLAGS, b ? (b0 | 1) : (b0 & ~1));
	}

	public boolean isMale() {
		return (entityData.get(FLAGS) & 1) != 0;
	}

	public void setHorned(boolean b) {
		int b0 = entityData.get(FLAGS);
		entityData.set(FLAGS, b ? (b0 | 2) : (b0 & ~2));
	}

	public boolean isHorned() {
		return (entityData.get(FLAGS) & 2) != 0;
	}

	protected void ageBoundaryReached() {
		if (this.isBaby()) {
			this.setHorned(false);
		} else {
			setHorned(isMale());
		}
	}

	public float relaxChance() {
		return 0.3f;
	}

	public int eatWillingness() {
		return isBaby() ? 50 : 1000;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData group, @Nullable CompoundTag data) {
		var ans = super.finalizeSpawn(level, ins, type, group, data);
		setMale(level.getRandom().nextBoolean());
		if (!isBaby()) {
			setHorned(isMale());
		}
		return ans;
	}

	// animal properties

	protected SoundEvent getAmbientSound() {
		return SoundEvents.SHEEP_AMBIENT;//TODO
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.SHEEP_HURT;//TODO
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.SHEEP_DEATH;//TODO
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);//TODO
	}

	@Nullable
	public DeerEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return YHEntities.DEER.create(level);
	}

	public boolean isFood(ItemStack stack) {
		return FOOD_ITEMS.get().test(stack);
	}

	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.6F * getEyeHeight(), getBbWidth() * 0.4F);
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions dim) {
		return dim.height * 0.95f;
	}

}
