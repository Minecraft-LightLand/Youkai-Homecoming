package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.content.entity.youkai.SyncedData;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class CrabEntity extends WaterAnimal implements Bucketable {

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(ForgeMod.SWIM_SPEED.get(), 4);
	}

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(CrabEntity.class, ser);
	}

	private static final SyncedData DATA = new SyncedData(CrabEntity::defineId);
	static final EntityDataAccessor<Integer> TRANSIENT_DATA = DATA.define(SyncedData.INT, 0, null);
	static final EntityDataAccessor<Integer> FLAGS = DATA.define(SyncedData.INT, 0, "flags");
	static final EntityDataAccessor<Integer> VARIANT = DATA.define(SyncedData.INT, 0, "variant");

	public final CrabProperties prop = new CrabProperties(this);

	public CrabEntity(EntityType<? extends WaterAnimal> type, Level level) {
		super(type, level);
		moveControl = new CrabMoveControl(this);
	}

	protected SyncedData data() {
		return DATA;
	}

	protected SynchedEntityData entityData() {
		return entityData;
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
	protected void handleAirSupply(int air) {
		setAirSupply(300);
	}

	@Override
	public void setZza(float val) {
		if (onGround())
			setXxa(val);
		else super.setZza(val);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
		this.goalSelector.addGoal(4, new CrabRandomWalkGoal(this, 1, 40));
	}

	protected PathNavigation createNavigation(Level level) {
		return new AmphibiousPathNavigation(this, level);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData group, @Nullable CompoundTag data) {
		var biome = level.getBiome(blockPosition());
		if (biome.is(YHBiomeTagsProvider.CRAB_MUD)) {
			prop.setVariant(CrabVariant.MUD);
		}
		return super.finalizeSpawn(level, ins, type, group, data);
	}

	public void saveToBucketTag(ItemStack stack) {
		Bucketable.saveDefaultDataToBucketTag(this, stack);
		CompoundTag tag = stack.getOrCreateTag();
		var b = fromBucket();
		setFromBucket(false);
		data().write(tag, entityData);
		setFromBucket(b);
	}

	public void loadFromBucketTag(CompoundTag tag) {
		Bucketable.loadDefaultDataFromBucketTag(this, tag);
		data().read(tag, entityData);
	}

	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_FILL_FISH;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return YHItems.CRAB_BUCKET.asStack();
	}

	@Override
	public boolean fromBucket() {
		return prop.isFromBucket();
	}

	@Override
	public void setFromBucket(boolean b) {
		prop.setFromBucket(b);
	}

}
