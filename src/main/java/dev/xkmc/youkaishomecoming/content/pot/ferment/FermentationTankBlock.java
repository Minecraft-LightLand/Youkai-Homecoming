package dev.xkmc.youkaishomecoming.content.pot.ferment;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class FermentationTankBlock implements CreateBlockStateBlockMethod, OnClickBlockMethod, ShapeBlockMethod, AnimateTickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.FERMENT_BE, FermentationTankBlockEntity.class);

	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	private static final VoxelShape NO_LID = Block.box(1, 0, 1, 15, 14, 15);
	private static final VoxelShape LID = Block.box(1, 0, 1, 15, 15, 15);

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(OPEN);
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(OPEN) ? NO_LID : LID;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof FermentationTankBlockEntity be) {
			ItemStack stack = player.getItemInHand(hand);
			if (!state.getValue(OPEN)) {
				level.setBlockAndUpdate(pos, state.setValue(OPEN, true));
				return InteractionResult.SUCCESS;
			}
			if (stack.isEmpty()) {
				if (!level.isClientSide()) {
					if (player.isShiftKeyDown()) {
						be.dumpInventory();
					} else {
						level.setBlockAndUpdate(pos, state.setValue(OPEN, false));
					}
				}
				return InteractionResult.SUCCESS;
			} else {
				return addItem(be, stack, level, pos, player, hand, hit);
			}
		}
		return InteractionResult.PASS;
	}

	private static InteractionResult addItem(FermentationTankBlockEntity be, ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		FluidStack fluid = be.fluids.getFluidInTank(0);
		boolean hasFluid = false;
		if (fluid.getFluid() instanceof SakeFluid sake) {
			if (fluid.getAmount() >= sake.type.amount() && stack.is(sake.type.getContainer())) {
				if (!level.isClientSide()) {
					be.fluids.drain(sake.type.amount(), IFluidHandler.FluidAction.EXECUTE);
					player.getInventory().placeItemBackInInventory(sake.type.asStack(1));
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
				return InteractionResult.SUCCESS;
			}
			hasFluid = true;
		}
		if (!hasFluid || player.getItemInHand(hand).getItem() instanceof SakeBottleItem) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
			if (opt.resolve().isPresent()) {
				if (!level.isClientSide() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection())) {
					be.notifyTile();
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.CONSUME;
				}
			}
		}
		ItemStack copy = stack.copy();
		copy.setCount(1);
		if (be.items.canAddItem(copy)) {
			ItemStack remain = be.items.addItem(copy);
			if (remain.isEmpty()) {
				stack.shrink(1);
				be.notifyTile();
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.CONSUME;
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var nolid = pvd.models().getBuilder("block/fermentation_tank")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/fermentation_tank")))
				.texture("side", pvd.modLoc("block/fermentation_tank_side"))
				.texture("top", pvd.modLoc("block/fermentation_tank_top"))
				.texture("bottom", pvd.modLoc("block/fermentation_tank_bottom"))
				.texture("inside", pvd.modLoc("block/fermentation_tank_inside"))
				.texture("lid", pvd.modLoc("block/fermentation_tank_lid"))
				.renderType("cutout");
		var lid = pvd.models().getBuilder("block/fermentation_tank_lid")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/fermentation_tank_lid")))
				.texture("side", pvd.modLoc("block/fermentation_tank_side"))
				.texture("top", pvd.modLoc("block/fermentation_tank_top"))
				.texture("bottom", pvd.modLoc("block/fermentation_tank_bottom"))
				.texture("inside", pvd.modLoc("block/fermentation_tank_inside"))
				.texture("lid", pvd.modLoc("block/fermentation_tank_lid"))
				.texture("lid_handle", pvd.modLoc("block/fermentation_tank_lid_handle"))
				.renderType("cutout");
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(OPEN) ? nolid : lid).build());
	}

}
