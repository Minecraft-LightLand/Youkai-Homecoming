package dev.xkmc.youkaishomecoming.content.pot.moka;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class MokaMakerBlock extends BasePotBlock {

	protected static final VoxelShape SHAPE = box(5.5, 0, 5.5, 10.5, 8.5, 10.5);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

	public MokaMakerBlock(Properties prop) {
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
		return YHBlocks.MOKA_BE.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		return level.isClientSide ? createTickerHelper(blockEntity, YHBlocks.MOKA_BE.get(), BasePotBlockEntity::animationTick) :
				createTickerHelper(blockEntity, YHBlocks.MOKA_BE.get(), BasePotBlockEntity::cookingTick);
	}

	public static void buildModel(DataGenContext<Block, MokaMakerBlock> ctx, RegistrateBlockstateProvider pvd) {
		var pot = pvd.models().getBuilder("block/moka_pot")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_pot")))
				.texture("maker", pvd.modLoc("block/moka_pot"))
				.renderType("cutout");
		var tray = pvd.models().getBuilder("block/moka_tray")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/moka_pot_tray")))
				.texture("maker", pvd.modLoc("block/moka_pot"))
				.texture("tray_side", pvd.modLoc("block/cooking_pot_tray_side"))
				.texture("tray_top", pvd.modLoc("block/cooking_pot_tray_top"))
				.renderType("cutout");
		pvd.horizontalBlock(ctx.get(), state -> switch (state.getValue(SUPPORT)) {
			case NONE, HANDLE -> pot;
			case TRAY -> tray;
		});
	}

}
