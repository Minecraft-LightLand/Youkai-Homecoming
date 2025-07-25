package dev.xkmc.youkaishomecoming.content.pot.ferment;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.compat.create.CreateFillingTest;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
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

	@OnlyIn(Dist.CLIENT)
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
				return FluidItemTile.addItem(be, stack, level, pos, player, hand, hit);
			}
		}
		return InteractionResult.PASS;
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var nolid = pvd.models().getBuilder("block/fermentation_tank")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/fermentation_tank")))
				.texture("side", pvd.modLoc("block/utensil/fermentation_tank_side"))
				.texture("top", pvd.modLoc("block/utensil/fermentation_tank_top"))
				.texture("bottom", pvd.modLoc("block/utensil/fermentation_tank_bottom"))
				.texture("inside", pvd.modLoc("block/utensil/fermentation_tank_inside"))
				.texture("lid", pvd.modLoc("block/utensil/fermentation_tank_lid"))
				.renderType("cutout");
		var lid = pvd.models().getBuilder("block/fermentation_tank_lid")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/fermentation_tank_lid")))
				.texture("side", pvd.modLoc("block/utensil/fermentation_tank_side"))
				.texture("top", pvd.modLoc("block/utensil/fermentation_tank_top"))
				.texture("bottom", pvd.modLoc("block/utensil/fermentation_tank_bottom"))
				.texture("inside", pvd.modLoc("block/utensil/fermentation_tank_inside"))
				.texture("lid", pvd.modLoc("block/utensil/fermentation_tank_lid"))
				.texture("lid_handle", pvd.modLoc("block/utensil/fermentation_tank_lid_handle"))
				.renderType("cutout");
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(OPEN) ? nolid : lid).build());
	}

}
