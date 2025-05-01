package dev.xkmc.youkaishomecoming.content.pot.base;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

@SuppressWarnings("deprecation")
public abstract class BasePotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<CookingPotSupport> SUPPORT = CookingPotBlock.SUPPORT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BasePotBlock(Properties pProperties) {
		super(pProperties);
		registerDefaultState(defaultBlockState().setValue(SUPPORT, CookingPotSupport.NONE).setValue(WATERLOGGED, false));
	}

	public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (heldStack.isEmpty() && player.isShiftKeyDown()) {
			level.setBlockAndUpdate(pos, state.setValue(SUPPORT,
					state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE) ?
							this.getTrayState(level, pos) : CookingPotSupport.HANDLE));
			level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
		} else if (!level.isClientSide) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof BasePotBlockEntity<?> be) {
				ItemStack servingStack = be.useHeldItemOnMeal(heldStack);
				if (servingStack != ItemStack.EMPTY) {
					if (!player.getInventory().add(servingStack)) {
						player.drop(servingStack, false);
					}
					level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					player.openMenu(be, pos);
				}
			}

			return ItemInteractionResult.SUCCESS;
		}

		return ItemInteractionResult.SUCCESS;
	}

	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		FluidState fluid = level.getFluidState(context.getClickedPos());
		BlockState state = this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
		return context.getClickedFace().equals(Direction.DOWN) ?
				state.setValue(SUPPORT, CookingPotSupport.HANDLE) :
				state.setValue(SUPPORT, this.getTrayState(level, pos));
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return facing.getAxis().equals(Direction.Axis.Y) &&
				!state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE) ?
				state.setValue(SUPPORT, this.getTrayState(level, currentPos)) : state;
	}

	private CookingPotSupport getTrayState(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES) ? CookingPotSupport.TRAY : CookingPotSupport.NONE;
	}

	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(level, pos, state);
		if (level.getBlockEntity(pos) instanceof BasePotBlockEntity<?> pot)
			return pot.getAsItem();
		return stack;
	}

	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof BasePotBlockEntity<?> cookingPotEntity) {
				cookingPotEntity.getUsedRecipesAndPopExperience(level, Vec3.atCenterOf(pos));
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}

	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SUPPORT, WATERLOGGED);
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof BasePotBlockEntity<?> cookingPotEntity) {
			if (cookingPotEntity.isHeated()) {
				SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty() ? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get() : ModSounds.BLOCK_COOKING_POT_BOIL.get();
				double x = (double) pos.getX() + 0.5;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5;
				if (random.nextInt(10) == 0) {
					level.playLocalSound(x, y, z, boilSound, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.2F + 0.9F, false);
				}
			}
		}

	}

	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof BasePotBlockEntity<?>) {
			ItemStackHandler inventory = ((BasePotBlockEntity<?>) tileEntity).getInventory();
			return MathUtils.calcRedstoneFromItemHandler(inventory);
		} else {
			return 0;
		}
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, BasePotBlock block) {
		pvd.add(block, LootTable.lootTable().withPool(
				pvd.applyExplosionCondition(block,
						LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(block)
										.apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
												.include(DataComponents.CUSTOM_NAME)
												.include(ModDataComponents.MEAL.get())
												.include(ModDataComponents.CONTAINER.get()))))));

	}

}
