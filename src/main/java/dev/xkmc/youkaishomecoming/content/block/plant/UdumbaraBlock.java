package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;

import java.util.function.Supplier;

public class UdumbaraBlock extends YHCropBlock {

	private final Supplier<Item> fruit;

	public UdumbaraBlock(BlockBehaviour.Properties pProperties, Supplier<Item> seed, Supplier<Item> fruit) {
		super(pProperties, seed);
		this.fruit = fruit;
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (level.isNight() && level.canSeeSky(pos)) {
			int i = this.getAge(state);
			if (i < getMaxAge() - 1 || i == getMaxAge() - 1 && level.getMoonBrightness() > 0.8) {
				float f = getGrowthSpeed(this, level, pos);
				if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
					level.setBlock(pos, getStateForAge(i + 1), 2);
					ForgeHooks.onCropsGrowPost(level, pos, state);
					if (i + 1 == getMaxAge())
						level.scheduleTick(pos, this, YHModConfig.COMMON.udumbaraDuration.get());
				}
			}
		} else if (level.isDay() && level.canSeeSky(pos)) {
			int i = getAge(state);
			if (i > 0) {
				level.setBlock(pos, getStateForAge(i - 1), 2);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.setBlock(pos, state.setValue(AGE, getMaxAge() - 1), 2);
	}

	public InteractionResult use(BlockState state, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (state.getValue(AGE) < getMaxAge()) {
			return InteractionResult.PASS;
		} else {
			popResource(pLevel, pPos, fruit.get().getDefaultInstance());
			pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
					1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
			BlockState next = state.setValue(AGE, state.getValue(AGE) - 1);
			pLevel.setBlock(pPos, next, 2);
			pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, next));
			return InteractionResult.sidedSuccess(pLevel.isClientSide);
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return false;
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, YHCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed())))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
	}

}
