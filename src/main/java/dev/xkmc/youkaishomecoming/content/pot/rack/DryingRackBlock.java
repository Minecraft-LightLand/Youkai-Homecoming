package dev.xkmc.youkaishomecoming.content.pot.rack;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.List;

public class DryingRackBlock extends BaseEntityBlock {

	protected static final VoxelShape SHAPE = box(0, 0, 0, 16, 2, 16);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public DryingRackBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity be) {
			var opt = be.getCookableRecipe(stack);
			if (opt.isPresent()) {
				if (!level.isClientSide && be.placeFood(player.getAbilities().instabuild ?
						stack.copy() : stack, opt.get().value().getCookingTime())) {
					return ItemInteractionResult.SUCCESS;
				}
				return ItemInteractionResult.CONSUME;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		pTooltip.add(YHLangData.DRYING_RACK.get());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity moka) {
				Containers.dropContents(level, pos, moka.getItems());
				level.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}

	}

	@Deprecated
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Deprecated
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity moka) {
			ItemStackHandler inventory = moka.getInventory();
			return MathUtils.calcRedstoneFromItemHandler(inventory);
		}
		return 0;
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return YHBlocks.RACK_BE.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		return level.isClientSide ? null : createTickerHelper(blockEntity, YHBlocks.RACK_BE.get(), DryingRackBlockEntity::cookTick);
	}

	@Deprecated
	public BlockState rotate(BlockState pState, Rotation pRot) {
		return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
	}

	@Deprecated
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;//TODO
	}

	public static void buildModel(DataGenContext<Block, DryingRackBlock> ctx, RegistrateBlockstateProvider pvd) {
		var pot = pvd.models().getBuilder("block/drying_rack")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/drying_rack")))
				.texture("rack", pvd.modLoc("block/drying_rack"))
				.renderType("cutout");
		pvd.horizontalBlock(ctx.get(), state -> pot);
	}

}
