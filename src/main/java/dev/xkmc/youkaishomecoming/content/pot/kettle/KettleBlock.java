package dev.xkmc.youkaishomecoming.content.pot.kettle;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fluids.FluidStack;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.List;

public class KettleBlock implements
		CreateBlockStateBlockMethod, DefaultStateBlockMethod,
		PlacementBlockMethod, SetPlacedByBlockMethod,
		ShapeUpdateBlockMethod, ShapeBlockMethod,
		ToolTipBlockMethod, OnClickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(YHBlocks.KETTLE_BE, KettleBlockEntity.class);

	public static final EnumProperty<CookingPotSupport> SUPPORT = CookingPotBlock.SUPPORT;

	protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 7, 13);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SUPPORT);
	}

	@Override
	public BlockState getDefaultState(BlockState blockState) {
		return blockState.setValue(SUPPORT, CookingPotSupport.NONE);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		return context.getClickedFace().equals(Direction.DOWN) ?
				state.setValue(SUPPORT, CookingPotSupport.HANDLE) :
				state.setValue(SUPPORT, this.getTrayState(level, pos));
	}

	private CookingPotSupport getTrayState(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES) ? CookingPotSupport.TRAY : CookingPotSupport.NONE;
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing.getAxis().equals(Direction.Axis.Y) &&
				!current.getValue(SUPPORT).equals(CookingPotSupport.HANDLE) ?
				current.setValue(SUPPORT, this.getTrayState(level, currentPos)) : current;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity user, ItemStack stack) {
		if (level.getBlockEntity(pos) instanceof KettleBlockEntity be) {
			be.readFromStack(stack);
		}
	}

	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof KettleBlockEntity be) {

			if (stack.isEmpty()) {
				if (!level.isClientSide()) {
					if (player.isShiftKeyDown()) {
						be.dumpInventory();
					} else {

					}
				}
				return InteractionResult.SUCCESS;
			} else {
				return FluidItemTile.addItem(be, stack, level, pos, player, hand, hit);
			}
		}
		return InteractionResult.PASS;
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> list, TooltipFlag pFlag) {
		var root = pStack.getTag();
		if (root != null && root.contains("KettleFluid", Tag.TAG_COMPOUND)) {
			var fluid = FluidStack.loadFluidStackFromNBT(root.getCompound("KettleFluid"));
			if (!fluid.isEmpty() && fluid.getFluid() instanceof YHFluid sake)
				list.add(sake.type.asItem().getDescription());
		}
		list.add(YHLangData.KETTLE_INFO.get());
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var kettle = pvd.models().getBuilder("block/kettle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.renderType("cutout");
		var handle = pvd.models().getBuilder("block/kettle_handle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle_handle")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.texture("handle", pvd.modLoc("block/utensil/cooking_pot_handle"))
				.texture("chain", pvd.modLoc("block/utensil/chain"))
				.renderType("cutout");
		var tray = pvd.models().getBuilder("block/kettle_tray")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/kettle_tray")))
				.texture("kettle", pvd.modLoc("block/utensil/kettle"))
				.texture("tray_side", pvd.modLoc("block/utensil/cooking_pot_tray_side"))
				.texture("tray_top", pvd.modLoc("block/utensil/cooking_pot_tray_top"))
				.renderType("cutout");
		pvd.horizontalBlock(ctx.get(), state -> switch (state.getValue(SUPPORT)) {
			case NONE -> kettle;
			case HANDLE -> handle;
			case TRAY -> tray;
		});
	}

}
