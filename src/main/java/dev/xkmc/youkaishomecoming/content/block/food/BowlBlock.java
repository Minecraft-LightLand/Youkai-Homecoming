package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import dev.xkmc.youkaishomecoming.util.WaterConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BowlBlock extends HorizontalDirectionalBlock implements ISteamerContentBlock, LeftClickBlock {

	public static final Vec3 IRON_SHAPE = new Vec3(4, 4, 4);
	public static final Vec3 WOOD_SHAPE = new Vec3(5, 3, 5);
	public static final Vec3 BAMBOO_SHAPE = new Vec3(2, 3, 5.5);
	public static final Vec3 RAW_BAMBOO_SHAPE = new Vec3(2, 5, 5.5);

	private final VoxelShape shape_x, shape_z;

	public BowlBlock(Properties prop, Vec3 saucer) {
		super(prop);
		shape_x = Block.box(saucer.x, 0, saucer.z, 16 - saucer.x, saucer.y, 16 - saucer.z);
		shape_z = Block.box(saucer.z, 0, saucer.x, 16 - saucer.z, saucer.y, 16 - saucer.x);
	}

	@Override
	public float clearance() {
		return 2;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? shape_x : shape_z;
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		return collectBowl(state, level, pos, player, YHItems.WOOD_BOWL.get(), Items.BOWL) ||
				collectBowl(state, level, pos, player, YHItems.BAMBOO_BOWL.get(), Items.BAMBOO) ||
				collectBowl(state, level, pos, player, YHItems.IRON_BOWL.get(), YHItems.IRON_BOWL.asItem());
	}

	private boolean collectBowl(BlockState state, Level level, BlockPos pos, Player player, Block block, Item item) {
		if (state.is(block)) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(item.getDefaultInstance());
			}
			return true;
		}
		return false;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);
		if (state.is(YHItems.IRON_BOWL.get()) &&
				CookingBlockEntity.isHeatedPos(level, pos) &&
				WaterConsumer.isWaterContainer(stack, 250)) {
			if (!level.isClientSide()) {
				var pot = YHBlocks.SMALL_POT.getDefaultState().setValue(FACING, state.getValue(FACING));
				level.setBlockAndUpdate(pos, pot);
				if (!player.getAbilities().instabuild) {
					if (stack.getCount() > 1) {
						var copy = stack.copyWithCount(1);
						var remain = WaterConsumer.drainWater(copy, 250);
						player.getInventory().placeItemBackInInventory(remain);
					} else {
						player.setItemInHand(hand, WaterConsumer.drainWater(stack, 250));
					}
				}
			}
			return InteractionResult.SUCCESS;
		}
		if (!stack.isEmpty()) return InteractionResult.PASS;
		var item = state.getBlock().asItem().getDefaultInstance();
		var food = item.getFoodProperties(player);
		if (food != null && player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, item.copy());
				var cont = item.getCraftingRemainingItem();
				if (cont.getItem() instanceof BlockItem block) {
					level.setBlockAndUpdate(pos, block.getBlock().defaultBlockState()
							.setValue(BlockStateProperties.HORIZONTAL_FACING,
									state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
				} else {
					level.setBlockAndUpdate(pos, YHItems.WOOD_BOWL.getDefaultState()
							.setValue(BlockStateProperties.HORIZONTAL_FACING,
									state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
