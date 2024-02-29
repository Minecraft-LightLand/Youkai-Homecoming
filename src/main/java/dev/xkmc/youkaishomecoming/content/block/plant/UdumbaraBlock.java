package dev.xkmc.youkaishomecoming.content.block.plant;

import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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

}
