package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.content.entity.animal.common.StateMachineMob;
import dev.xkmc.youkaishomecoming.content.entity.youkai.SyncedData;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CrabEntity extends WaterAnimal implements Bucketable, StateMachineMob {

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

	public final CrabStateMachine states = new CrabStateMachine(this);
	public final CrabProperties prop = new CrabProperties(this);

	CrabDigGoal dig;

	public CrabEntity(EntityType<? extends WaterAnimal> type, Level level) {
		super(type, level);
		moveControl = new CrabMoveControl(this);
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.LEFT;
	}

	// core

	protected void registerGoals() {
		dig = new CrabDigGoal(this);
		super.registerGoals();
		this.goalSelector.addGoal(1, new CrabFlipGoal(this));
		this.goalSelector.addGoal(2, new CrabHideGoal(this));
		this.goalSelector.addGoal(3, new CrabPanicGoal(this, 1.25D));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
		this.goalSelector.addGoal(5, dig);
		this.goalSelector.addGoal(10, new CrabRandomWalkGoal(this, 1, 40));
	}

	protected PathNavigation createNavigation(Level level) {
		return new AmphibiousPathNavigation(this, level);
	}

	protected SyncedData data() {
		return DATA;
	}

	protected SynchedEntityData entityData() {
		return entityData;
	}

	public CrabStateMachine states() {
		return states;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		data().register(entityData);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		data().write(tag, entityData);
		states.write(tag);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data().read(tag, entityData);
		states.read(tag);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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

	// entity properties

	@Override
	protected void handleAirSupply(int air) {
		setAirSupply(300);
	}

	public void dig(BlockState down) {
		if (level() instanceof ServerLevel sl) {
			var biome = sl.getBiome(blockPosition);
			boolean sand = false;
			if (down.is(BlockTags.SAND)) sand = true;
			else if (!down.is(Tags.Blocks.GRAVEL)) return;
			ResourceLocation table = sand ? YHLootGen.CRAB_SAND_BASE : YHLootGen.CRAB_GRAVEL_BASE;
			if (biome.is(YHBiomeTagsProvider.CRAB_MUD)) {
				table = sand ? YHLootGen.CRAB_SAND_RIVER : YHLootGen.CRAB_GRAVEL_RIVER;
			} else if (biome.is(BiomeTags.IS_BEACH) || biome.is(BiomeTags.IS_OCEAN)) {
				table = sand ? YHLootGen.CRAB_SAND_BEACH : YHLootGen.CRAB_GRAVEL_BEACH;
			}
			var list = sl.getServer().getLootData().getLootTable(table).getRandomItems(new LootParams.Builder(sl)
					.withParameter(LootContextParams.ORIGIN, position())
					.withParameter(LootContextParams.THIS_ENTITY, this)
					.create(LootContextParamSets.GIFT));
			if (list.isEmpty()) return;
			var stack = list.get(random.nextInt(list.size())).copyWithCount(1);
			setItemInHand(InteractionHand.MAIN_HAND, stack);
			setDropChance(EquipmentSlot.MAINHAND, 1);
			sl.playSound(null, blockPosition, SoundEvents.ITEM_PICKUP,
					SoundSource.AMBIENT, 0.7f, 1);
		}
	}

	protected boolean isValidItemToGrab(ItemStack stack) {
		if (!stack.getItem().canFitInsideContainerItems()) return false;
		if (stack.getItem() instanceof MobBucketItem) return false;
		return !stack.is(Items.NAME_TAG);
	}

	@Override
	public void setZza(float val) {
		if (onGround())
			setXxa(val);
		else super.setZza(val);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty() && states().isFlipped()) {
			if (level() instanceof ServerLevel sl) {
				states().flipBack();
				sl.playSound(null, blockPosition, SoundEvents.ITEM_PICKUP,
						SoundSource.AMBIENT, 0.7f, 1);
			}
			return InteractionResult.SUCCESS;
		}
		if (!stack.isEmpty() && getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && states.canGrab() && isValidItemToGrab(stack)) {
			if (level() instanceof ServerLevel sl) {
				setItemInHand(InteractionHand.MAIN_HAND, stack.split(1));
				setDropChance(EquipmentSlot.MAINHAND, 1);
				states.transitionTo(CrabState.SWING);
				sl.playSound(this, blockPosition, SoundEvents.ITEM_BREAK,
						SoundSource.AMBIENT, 0.7f, 1);
				if (stack.is(Items.WATER_BUCKET) && player instanceof ServerPlayer sp)
					YHCriteriaTriggers.CRAB_GRAB.trigger(sp);
			}
			return InteractionResult.SUCCESS;
		}
		var ans = Bucketable.bucketMobPickup(player, hand, this);
		if (ans.isPresent()) return ans.get();
		return super.mobInteract(player, hand);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		var ans = super.getDimensions(pose);
		if (states().isHiding())
			ans = ans.scale(1, 0.1f);
		return ans;
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData group, @Nullable CompoundTag data) {
		var biome = level.getBiome(blockPosition());
		if (biome.is(YHBiomeTagsProvider.CRAB_MUD)) {
			prop.setVariant(CrabVariant.MUD);
		}
		return super.finalizeSpawn(level, ins, type, group, data);
	}

	// bucket

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
