package dev.xkmc.youkaishomecoming.content.entity.animal.deer;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.*;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal.DeerEatBlockGoal;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal.DeerPanicGoal;
import dev.xkmc.youkaishomecoming.content.entity.animal.deer.goal.DeerRelaxGoal;
import dev.xkmc.youkaishomecoming.content.entity.youkai.SyncedData;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHSounds;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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
import java.util.ArrayList;
import java.util.List;

public class DeerEntity extends Animal implements StateMachineMob {

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(DeerEntity.class, ser);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.25);
	}

	private static final SyncedData DATA = new SyncedData(DeerEntity::defineId);
	private static final Lazy<Ingredient> FOOD_ITEMS = Lazy.of(() -> Ingredient.of(YHFood.SAKURA_MOCHI.item));//TODO

	static final EntityDataAccessor<Integer> TRANSIENT_DATA = DATA.define(SyncedData.INT, 0, null);
	static final EntityDataAccessor<Integer> FLAGS = DATA.define(SyncedData.INT, 0, "flags");
	static final EntityDataAccessor<Integer> EAT_STAGE = DATA.define(SyncedData.INT, 0, "eat_stage");
	static final EntityDataAccessor<Integer> VARIANT = DATA.define(SyncedData.INT, 0, "variant");

	public final DeerStateMachine states = new DeerStateMachine(this);
	public final DeerProperties prop = new DeerProperties(this);

	protected List<INotifyMoveGoal> notifiers;
	protected DeerPanicGoal panic;
	public DeerEatBlockGoal eat;

	public DeerEntity(EntityType<? extends DeerEntity> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
		panic = new DeerPanicGoal(this, 1.25D);
		eat = new DeerEatBlockGoal(this);
		notifiers = new ArrayList<>();

		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, panic);
		goalSelector.addGoal(3, new BreedNotifyGoal<>(this, 1.0D).register(notifiers));
		goalSelector.addGoal(4, new TemptNotifyGoal<>(this, 1.2D, FOOD_ITEMS.get(), false).register(notifiers));
		goalSelector.addGoal(5, new FollowParentNotifyGoal<>(this, 1.1D).register(notifiers));
		goalSelector.addGoal(6, eat);
		goalSelector.addGoal(7, new DeerRelaxGoal(this));
		goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(13, new RandomLookAroundGoal(this));
	}

	@Override
	public List<INotifyMoveGoal> notifiers() {
		return notifiers == null ? List.of() : notifiers;
	}

	// core

	protected SyncedData data() {
		return DATA;
	}

	protected SynchedEntityData entityData() {
		return entityData;
	}

	public DeerStateMachine states() {
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

	protected void ageBoundaryReached() {
		if (isBaby()) {
			prop.setHorned(false);
		} else {
			prop.setHorned(prop.isMale());
		}
	}

	public void ate() {
		heal(1);
		super.ate();
		if (isBaby()) {
			ageUp(60);
		} else if (!prop.isHorned() && prop.isMale()) {
			int stage = prop.getEatAge();
			if (stage >= 3) {
				prop.setHorned(true);
				prop.setEatAge(0);
			} else {
				prop.setEatAge(stage + 1);
			}
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		var ans = super.getDimensions(pose);
		if (states().isRelaxed())
			ans = ans.scale(1, 0.7f);
		return ans;
	}


	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData group, @Nullable CompoundTag data) {
		var ans = super.finalizeSpawn(level, ins, type, group, data);
		prop.setMale(level.getRandom().nextBoolean());
		if (!isBaby()) {
			prop.setHorned(prop.isMale());
		}
		var biome = level.getBiome(blockPosition());
		if (biome.is(YHBiomeTagsProvider.WHITE_GRAPE)) {
			prop.setVariant(DeerVariant.FALLOW);
		} else if (biome.is(YHBiomeTagsProvider.BLACK_GRAPE)) {
			prop.setVariant(DeerVariant.WHITELIPPED);
		} else {
			prop.setVariant(DeerVariant.NORMAL);
		}
		return ans;
	}

	// animal properties

	protected SoundEvent getAmbientSound() {
		return YHSounds.DEER_AMBIENT.get();
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return YHSounds.DEER_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return YHSounds.DEER_DEATH.get();
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
	}

	@Nullable
	public DeerEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
		var ans = YHEntities.DEER.create(level);
		if (ans == null) return null;
		if (mob instanceof DeerEntity deer && random.nextBoolean()) {
			ans.prop.setVariant(deer.prop.getVariant());
		} else ans.prop.setVariant(prop.getVariant());
		ans.prop.setMale(random.nextBoolean());
		if (ans.prop.getVariant() == DeerVariant.SAKURA) {
			if (random.nextFloat() < 0.3f)
				ans.prop.setVariant(DeerVariant.NORMAL);
		}
		return ans;
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
