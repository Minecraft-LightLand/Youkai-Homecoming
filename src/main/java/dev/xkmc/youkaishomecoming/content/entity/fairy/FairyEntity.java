package dev.xkmc.youkaishomecoming.content.entity.fairy;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TouhouConditionalSpawns;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;
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
		goalSelector.addGoal(5, new TemptGoal(this, 1,
				Ingredient.of(YHFood.FAIRY_CANDY.item.get()), false));
	}

	protected boolean wouldAttack(LivingEntity entity) {
		if (shouldIgnore(entity)) return false;
		return YHModConfig.COMMON.fairyAttackYoukaified.get() && EffectEventHandlers.isYoukai(entity);
	}

	@Override
	public boolean shouldHurt(LivingEntity le) {
		return super.shouldHurt(le) || le instanceof Mob mob && mob.getTarget() != null;
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
			TouhouConditionalSpawns.triggetFairyReinforcement(this, le, position());
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		initSpellCard();
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}

	public void initSpellCard() {
	}

	public static boolean checkFairySpawnRules(EntityType<? extends FairyEntity> e, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource rand) {
		if (e != YHEntities.CIRNO.get() && !ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return false;
		if (!checkMobSpawnRules(e, level, type, pos, rand)) return false;
		if (!YHModConfig.COMMON.cirnoSpawn.get()) return false;
		var aabb = AABB.ofSize(pos.getCenter(), 48, 24, 48);
		if (!level.getEntitiesOfClass(FairyEntity.class, aabb).isEmpty()) return false;
		var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false);
		if (player == null) return false;
		return !YHModConfig.COMMON.cirnoSpawnCheckEffect.get() || EffectEventHandlers.isCharacter(player);
	}

}
