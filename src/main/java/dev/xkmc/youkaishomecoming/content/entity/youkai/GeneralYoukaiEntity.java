package dev.xkmc.youkaishomecoming.content.entity.youkai;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class GeneralYoukaiEntity extends YoukaiEntity {

	private static final ResourceLocation SPELL = YoukaisHomecoming.loc("ex_rumia");

	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(GeneralYoukaiEntity.class, ser);
	}

	protected static final SyncedData SPELL_DATA = new SyncedData(GeneralYoukaiEntity::defineId, YOUKAI_DATA);

	private static final EntityDataAccessor<String> SPELL_MODEL = SPELL_DATA.define(SyncedData.STRING, "", "modelId");

	private int tickAggressive;

	public GeneralYoukaiEntity(EntityType<? extends GeneralYoukaiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setPersistenceRequired();
	}

	@Override
	protected SyncedData data() {
		return SPELL_DATA;
	}

	public String getModelId() {
		String ans = entityData.get(SPELL_MODEL);
		if (ans.isEmpty()) return "";
		return ans;
	}

	public void syncModel() {
		String model = null;
		if (spellCard != null) model = spellCard.getModelId();
		if (model == null) model = "";
		entityData.set(SPELL_MODEL, model);
	}

	protected void registerGoals() {
		goalSelector.addGoal(4, new YoukaiAttackGoal<>(this));
		goalSelector.addGoal(6, new FloatGoal(this));
		goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new MultiHurtByTargetGoal(this, GeneralYoukaiEntity.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, this::wouldAttack));
	}

	protected boolean wouldAttack(LivingEntity entity) {
		if (shouldIgnore(entity)) return false;
		return entity.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		if (shouldIgnore(le)) return false;
		return super.shouldHurt(le) || wouldAttack(le);
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide()) {
			if (isAggressive()) {
				if (tickAggressive < 20)
					tickAggressive++;
			} else if (tickAggressive > 0) {
				tickAggressive--;
			}
		}
	}

	@Override
	public boolean shouldShowSpellCircle() {
		return level().isClientSide() ? isAggressive() : getTarget() != null;
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowSpellCircle()) {
			return null;
		}
		return SPELL;
	}

	@Override
	public float getCircleSize(float pTick) {
		return tickAggressive == 0 ? 0 : Math.min(1, (tickAggressive + pTick) / 20f);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		initSpellCard();
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public void initSpellCard() {
	}

}