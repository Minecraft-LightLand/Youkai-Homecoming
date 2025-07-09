package dev.xkmc.youkaishomecoming.content.block.food;

import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingInv;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
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

import java.util.List;

public class BowlBlock extends HorizontalDirectionalBlock implements ISteamerContentBlock {

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

	public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
		return def.setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? shape_x : shape_z;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.is(YHItems.WOOD_BOWL.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(Items.BOWL.getDefaultInstance());
			}
			return InteractionResult.SUCCESS;
		}
		if (state.is(YHItems.BAMBOO_BOWL.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(Items.BAMBOO.getDefaultInstance());
			}
			return InteractionResult.SUCCESS;
		}
		if (state.is(YHItems.IRON_BOWL.get())) {
			if (!level.isClientSide()) {
				var stack = player.getItemInHand(hand);
				if (!stack.isEmpty() && CookingBlockEntity.isHeatedPos(level, pos)) {
					var recipe = level.getRecipeManager().getRecipeFor(YHBlocks.COOKING_RT.get(),
							new CookingInv(List.of(), false), level);
					if (recipe.isPresent()) {
						var pot = YHBlocks.SMALL_POT.getDefaultState().setValue(FACING, state.getValue(FACING));
						level.setBlockAndUpdate(pos, pot);
						return pot.use(level, player, hand, hit);
					}
				}
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(YHItems.IRON_BOWL.asStack());
			}
			return InteractionResult.SUCCESS;
		}
		var stack = state.getBlock().asItem().getDefaultInstance();
		var food = stack.getFoodProperties(player);
		if (food != null && player.canEat(false)) {
			if (!level.isClientSide()) {
				player.eat(level, stack.copy());
				var cont = stack.getCraftingRemainingItem();
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
