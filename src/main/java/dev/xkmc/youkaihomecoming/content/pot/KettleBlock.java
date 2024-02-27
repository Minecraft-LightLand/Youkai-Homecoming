package dev.xkmc.youkaihomecoming.content.pot;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import javax.annotation.Nullable;

public class KettleBlock extends BasePotBlock {

	protected static final VoxelShape SHAPE = box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

	public KettleBlock(Properties prop) {
		super(prop);
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return YHBlocks.KETTLE_BE.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		return level.isClientSide ? createTickerHelper(blockEntity, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::animationTick) :
				createTickerHelper(blockEntity, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::cookingTick);
	}


	public static void buildModel(DataGenContext<Block, KettleBlock> ctx, RegistrateBlockstateProvider pvd) {
		var kettle = pvd.models().getBuilder("block/kettle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle")))
				.texture("kettle", pvd.modLoc("block/kettle"));
		var handle = pvd.models().getBuilder("block/kettle_handle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle_handle")))
				.texture("kettle", pvd.modLoc("block/kettle"))
				.texture("handle", pvd.modLoc("block/cooking_pot_handle"))
				.texture("chain", pvd.modLoc("block/chain"));
		var tray = pvd.models().getBuilder("block/kettle_tray")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle_tray")))
				.texture("kettle", pvd.modLoc("block/kettle"))
				.texture("tray_side", pvd.modLoc("block/cooking_pot_tray_side"))
				.texture("tray_top", pvd.modLoc("block/cooking_pot_tray_top"));
		pvd.horizontalBlock(ctx.get(), state -> switch (state.getValue(SUPPORT)) {
			case NONE -> kettle;
			case HANDLE -> handle;
			case TRAY -> tray;
		});
	}

}
