package dev.xkmc.youkaishomecoming.content.block.furniture;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class ThinTrapdoorBlock extends TrapDoorBlock {

	protected static final int TH = 3;
	protected static final VoxelShape EAST_OPEN_AABB = Block.box(0, 0, 0, TH, 16, 16);
	protected static final VoxelShape WEST_OPEN_AABB = Block.box(16 - TH, 0, 0, 16, 16, 16);
	protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0, 0, 0, 16, 16, TH);
	protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0, 0, 16 - TH, 16, 16, 16);
	protected static final VoxelShape BOTTOM_AABB = Block.box(0, 0, 0, 16, TH, 16);
	protected static final VoxelShape TOP_AABB = Block.box(0, 16 - TH, 0, 16, 16, 16);


	public ThinTrapdoorBlock(Properties pProperties, BlockSetType pType) {
		super(pProperties, pType);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if (!pState.getValue(OPEN)) {
			return pState.getValue(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
		} else {
			return switch (pState.getValue(FACING)) {
				case SOUTH -> SOUTH_OPEN_AABB;
				case WEST -> WEST_OPEN_AABB;
				case EAST -> EAST_OPEN_AABB;
				default -> NORTH_OPEN_AABB;
			};
		}
	}

	public static void buildModels(RegistrateBlockstateProvider pvd, TrapDoorBlock block, String baseName, ResourceLocation texture) {
		var handler = new TemplateModelHandler<>(pvd.models());
		ModelFile bottom = handler.trapdoorOrientableBottom(baseName + "_bottom", texture);
		ModelFile top = handler.trapdoorOrientableTop(baseName + "_top", texture);
		ModelFile open = handler.trapdoorOrientableOpen(baseName + "_open", texture);
		trapdoorBlock(pvd, block, bottom, top, open);
	}

	public static void trapdoorBlock(RegistrateBlockstateProvider pvd, TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open) {
		pvd.getVariantBuilder(block).forAllStatesExcept(state -> {
			int xRot = 0;
			int yRot = ((int) state.getValue(TrapDoorBlock.FACING).toYRot()) + 180;
			boolean isOpen = state.getValue(TrapDoorBlock.OPEN);
			if (isOpen && state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
				xRot += 180;
				yRot += 180;
			}
			yRot %= 360;
			return ConfiguredModel.builder().modelFile(isOpen ? open : state.getValue(TrapDoorBlock.HALF) == Half.TOP ? top : bottom)
					.rotationX(xRot)
					.rotationY(yRot)
					.build();
		}, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
	}

}
