package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.function.Supplier;

public class UdumbaraBlock extends YHCropBlock {

	private final Supplier<Item> fruit;

	public UdumbaraBlock(BlockBehaviour.Properties pProperties, Supplier<Item> seed, Supplier<Item> fruit) {
		super(pProperties, seed);
		this.fruit = fruit;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos low = pos.below();
		if (state.getBlock() == this) {
			var soil = level.getBlockState(low);
			var tri = soil.canSustainPlant(level, low, Direction.UP, state);
			if (tri.isTrue()) return true;
			if (tri.isFalse()) return false;
		}
		return this.mayPlaceOn(level.getBlockState(low), level, low);
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (level.isNight()) {
			int i = this.getAge(state);
			boolean seeSky = level.canSeeSky(pos);
			boolean brightMoon = level.getMoonBrightness() > 0.8;
			for (int x = -1; x <= 1; x++) {
				for (int y = 1; y <= 3; y++) {
					for (int z = -1; z <= 1; z++) {
						if (level.getBlockState(pos.offset(x, y, z)).is(YHBlocks.MOON_LANTERN.get())) {
							seeSky = true;
							brightMoon = true;
						}
					}
				}
			}
			if (seeSky && (i < getMaxAge() - 1 || i == getMaxAge() - 1 && brightMoon)) {
				float f = getGrowthSpeed(state, level, pos);
				if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
					level.setBlock(pos, getStateForAge(i + 1), 2);
					CommonHooks.fireCropGrowPost(level, pos, state);
					if (i + 1 == getMaxAge())
						level.scheduleTick(pos, this, YHModConfig.SERVER.udumbaraDuration.get());
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
	public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
		return false;
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, YHCropBlock block, YHCrops crop) {
		var helper = new LootHelper(pvd);
		pvd.add(block, pvd.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(crop.getSeed())))
				.withPool(LootPool.lootPool()
						.when(helper.intState(block, CropBlock.AGE, 7))
						.add(LootItem.lootTableItem(crop.getFruits()).apply(helper.fortuneBin())))));
	}

}
