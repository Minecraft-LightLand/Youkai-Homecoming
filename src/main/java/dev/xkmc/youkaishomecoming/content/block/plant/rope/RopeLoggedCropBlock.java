package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.List;

public abstract class RopeLoggedCropBlock extends CropBlock implements HarvestableBlock {

	public static boolean isRope(BlockState state) {
		return Configuration.ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES.get() ? state.is(ModTags.ROPES) : state.is(ModBlocks.ROPE.get());
	}

	public static BlockState getRopeBlock() {
		Block rope = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Configuration.DEFAULT_TOMATO_VINE_ROPE.get()));
		Block block = rope != null ? rope : ModBlocks.ROPE.get();
		return block.defaultBlockState();
	}

	public static final BooleanProperty ROPELOGGED = BooleanProperty.create("ropelogged");


	private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

	public RopeLoggedCropBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any()
				.setValue(getAgeProperty(), 0)
				.setValue(ROPELOGGED, false)
		);
	}

	protected abstract ItemLike getBaseSeedId();

	protected abstract ItemLike getFruit();

	public abstract int getBaseAge();

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (!mayGrow(state, level, pos, random)) return;
		int age = getAge(state);
		if (mayGrowTo(state, level, pos, age + 1)) {
			float speed = getGrowthSpeed(this, level, pos);
			if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
				growTo(state, level, pos, age + 1);
				ForgeHooks.onCropsGrowPost(level, pos, state);
			}
		}
		postGrowth(level, pos, random);
	}

	protected boolean mayGrowTo(BlockState state, LevelReader level, BlockPos pos, int age) {
		return age <= getMaxAge();
	}

	protected void growTo(BlockState state, ServerLevel level, BlockPos pos, int age) {
		level.setBlock(pos, state.setValue(getAgeProperty(), age), 2);
	}

	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int newAge = getAge(state) + getBonemealAgeIncrease(level);
		int maxAge = getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}
		if (mayGrowTo(state, level, pos, newAge)) {
			growTo(state, level, pos, newAge);
		}
		postGrowth(level, pos, random);
	}

	public void postGrowth(ServerLevel level, BlockPos pos, RandomSource random) {

	}

	protected int getBonemealAgeIncrease(Level level) {
		return super.getBonemealAgeIncrease(level) / 2;
	}

	protected boolean mayGrow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		return level.getRawBrightness(pos, 0) >= 9;
	}

	protected void pickup(BlockState state, Level level, BlockPos pos, Player player) {
		int quantity = 1 + level.random.nextInt(2);
		popResource(level, pos, new ItemStack(getFruit(), quantity));
		level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
		level.setBlock(pos, state.setValue(getAgeProperty(), getBaseAge()), 2);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		boolean isRope = false;
		if (stack.getItem() instanceof BlockItem item) {
			isRope = isRope(item.getBlock().defaultBlockState());
		}
		if (!isRope) {
			int age = state.getValue(getAgeProperty());
			boolean isMature = age == getMaxAge();
			if (!isMature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
				return InteractionResult.PASS;
			} else if (isMature) {
				pickup(state, level, pos, player);
				return InteractionResult.SUCCESS;
			} else {
				return super.use(state, level, pos, player, hand, hit);
			}
		}
		if (!state.getValue(ROPELOGGED)) {
			if (!level.isClientSide()) {
				level.setBlock(pos, state.setValue(ROPELOGGED, true), 2);
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(getAgeProperty(), ROPELOGGED);
	}

	public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return state.getValue(ROPELOGGED) && state.is(BlockTags.CLIMBABLE);
	}

	public boolean hasGoodCropConditions(LevelReader level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {
		boolean rope = state.getValue(ROPELOGGED);
		doPlayerDestroy(level, player, pos, state, be, stack);
		if (rope) {
			destroyAndPlaceRope(level, pos);
		}
	}

	public void doPlayerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, be, stack);
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState fstate, LevelAccessor level, BlockPos cpos, BlockPos fpos) {
		if (!state.canSurvive(level, cpos)) {
			level.scheduleTick(cpos, this, 1);
		}
		return state;
	}

	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.destroyBlock(pos, true);
		if (state.getValue(ROPELOGGED)) {
			destroyAndPlaceRope(level, pos);
		}
	}

	public static void destroyAndPlaceRope(Level level, BlockPos pos) {
		level.setBlockAndUpdate(pos, getRopeBlock());
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos blockPos) {
		if (state.getValue(getAgeProperty()) < getMaxAge()) return null;
		int quantity = 1 + level.random.nextInt(2);
		return new HarvestResult(state.setValue(getAgeProperty(), getBaseAge()),
				List.of(new ItemStack(getFruit(), quantity)));
	}

}
