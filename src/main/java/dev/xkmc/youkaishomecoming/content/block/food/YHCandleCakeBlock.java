package dev.xkmc.youkaishomecoming.content.block.food;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class YHCandleCakeBlock extends AbstractCandleBlock {
	public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
	protected static final VoxelShape CAKE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
	protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);
	protected static final VoxelShape SHAPE = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);
	private static final Map<Pair<Block, CakeBlock>, YHCandleCakeBlock> BY_CANDLE_AND_CAKE = Maps.newHashMap();
	private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 1.0, 0.5));

	public static boolean hasEntry(Block candle, YHCakeBlock cake) {
		return BY_CANDLE_AND_CAKE.get(Pair.of(candle, cake)) != null;
	}

	public static BlockState byCandle(Block candle, YHCakeBlock cake) {
		return BY_CANDLE_AND_CAKE.get(Pair.of(candle, cake)).defaultBlockState();
	}


	private final Supplier<YHCakeBlock> baseCake;
	public final Block candle;

	public YHCandleCakeBlock(Supplier<YHCakeBlock> baseCake, Block candle, BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
		this.baseCake = baseCake;
		this.candle = candle;
		BY_CANDLE_AND_CAKE.put(Pair.of(candle, baseCake.get()), this);
	}

	protected Iterable<Vec3> getParticleOffsets(BlockState p_152868_) {
		return PARTICLE_OFFSETS;
	}

	public VoxelShape getShape(BlockState p_152875_, BlockGetter p_152876_, BlockPos p_152877_, CollisionContext p_152878_) {
		return SHAPE;
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
			if (candleHit(result) && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
				extinguish(player, state, level, pos);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			InteractionResult interactionresult = baseCake.get().eatSlice(level, pos, baseCake.get().defaultBlockState(), player);
			if (interactionresult.consumesAction()) {
				dropResources(state, level, pos);
			}

			return interactionresult;
		}

		return InteractionResult.PASS;
	}

	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(this.baseCake.get());
	}

	private static boolean candleHit(BlockHitResult result) {
		return result.getLocation().y - (double) result.getBlockPos().getY() > 0.5;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(LIT);
	}

	public BlockState updateShape(BlockState state, Direction dire, BlockState sourceState, LevelAccessor level, BlockPos pos, BlockPos sourcePos) {
		return dire == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() :
				super.updateShape(state, dire, sourceState, level, pos, sourcePos);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isSolid();
	}

	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return CakeBlock.FULL_CAKE_SIGNAL;
	}

	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

}
