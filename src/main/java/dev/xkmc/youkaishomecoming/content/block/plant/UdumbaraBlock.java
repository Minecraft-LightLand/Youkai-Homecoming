package dev.xkmc.youkaishomecoming.content.block.plant;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;

import java.util.function.Supplier;

public class UdumbaraBlock extends YHBushBlock {

	public UdumbaraBlock(BlockBehaviour.Properties pProperties, Supplier<Item> fruit) {
		super(pProperties, fruit);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (level.getRawBrightness(pos, 0) >= 8) return false;
		return super.canSurvive(state, level, pos);
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(FLOWERING)) {
			if (level.isNight() && level.canSeeSky(pos) && level.getMoonBrightness() > 0.8) {
				if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
					BlockState next = state.setValue(FLOWERING, true);
					level.setBlock(pos, next, 2);
					level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(next));
					ForgeHooks.onCropsGrowPost(level, pos, state);
					level.scheduleTick(pos, this, YHModConfig.COMMON.udumbaraDuration.get());
				}
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.setBlock(pos, state.setValue(FLOWERING, false), 2);
	}

	public InteractionResult use(BlockState state, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!state.getValue(FLOWERING)) {
			return InteractionResult.PASS;
		} else {
			popResource(pLevel, pPos, fruit.get().getDefaultInstance());
			pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
					1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
			BlockState next = state.setValue(FLOWERING, false);
			pLevel.setBlock(pPos, next, 2);
			pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, next));
			return InteractionResult.sidedSuccess(pLevel.isClientSide);
		}
	}

}
