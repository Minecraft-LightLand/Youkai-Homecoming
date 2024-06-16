package dev.xkmc.youkaishomecoming.content.block.furniture;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class ThinDoorBlock extends DoorBlock {

	protected static final float TH = 3.0F;
	protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 0, 16, 16, TH);
	protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 16 - TH, 16, 16, 16);
	protected static final VoxelShape WEST_AABB = Block.box(16 - TH, 0, 0, 16, 16, 16);
	protected static final VoxelShape EAST_AABB = Block.box(0, 0, 0, TH, 16, 16);

	public ThinDoorBlock(Properties pProperties, BlockSetType pType) {
		super(pProperties, pType);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction direction = pState.getValue(FACING);
		boolean close = !pState.getValue(OPEN);
		boolean rightHinge = pState.getValue(HINGE) == DoorHingeSide.RIGHT;
		return switch (direction) {
			default -> close ? EAST_AABB : (rightHinge ? NORTH_AABB : SOUTH_AABB);
			case SOUTH -> close ? SOUTH_AABB : (rightHinge ? EAST_AABB : WEST_AABB);
			case WEST -> close ? WEST_AABB : (rightHinge ? SOUTH_AABB : NORTH_AABB);
			case NORTH -> close ? NORTH_AABB : (rightHinge ? WEST_AABB : EAST_AABB);
		};
	}

	public static void buildModels(RegistrateBlockstateProvider pvd, DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top) {
		var models = new TemplateModelHandler<>(pvd.models());
		ModelFile bottomLeft = models.doorBottomLeft(baseName + "_bottom_left", bottom, top);
		ModelFile bottomLeftOpen = models.doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top);
		ModelFile bottomRight = models.doorBottomRight(baseName + "_bottom_right", bottom, top);
		ModelFile bottomRightOpen = models.doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top);
		ModelFile topLeft = models.doorTopLeft(baseName + "_top_left", bottom, top);
		ModelFile topLeftOpen = models.doorTopLeftOpen(baseName + "_top_left_open", bottom, top);
		ModelFile topRight = models.doorTopRight(baseName + "_top_right", bottom, top);
		ModelFile topRightOpen = models.doorTopRightOpen(baseName + "_top_right_open", bottom, top);
		doorBlock(pvd, block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
	}

	public static void doorBlock(RegistrateBlockstateProvider pvd, DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) {
		pvd.getVariantBuilder(block).forAllStatesExcept(state -> {
			int yRot = ((int) state.getValue(DoorBlock.FACING).toYRot()) + 90;
			boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
			boolean open = state.getValue(DoorBlock.OPEN);
			boolean lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
			if (open) {
				yRot += 90;
			}
			if (right && open) {
				yRot += 180;
			}
			yRot %= 360;

			ModelFile model = null;
			if (lower && right && open) {
				model = bottomRightOpen;
			} else if (lower && !right && open) {
				model = bottomLeftOpen;
			}
			if (lower && right && !open) {
				model = bottomRight;
			} else if (lower && !right && !open) {
				model = bottomLeft;
			}
			if (!lower && right && open) {
				model = topRightOpen;
			} else if (!lower && !right && open) {
				model = topLeftOpen;
			}
			if (!lower && right && !open) {
				model = topRight;
			} else if (!lower && !right && !open) {
				model = topLeft;
			}

			return ConfiguredModel.builder().modelFile(model)
					.rotationY(yRot)
					.build();
		}, DoorBlock.POWERED);
	}

}
