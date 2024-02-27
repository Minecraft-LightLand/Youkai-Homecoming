package dev.xkmc.youkaihomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

public class KettleBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<CookingPotSupport> SUPPORT = CookingPotBlock.SUPPORT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public KettleBlock(Properties prop) {
		super(prop);
		registerDefaultState(defaultBlockState().setValue(SUPPORT, CookingPotSupport.NONE).setValue(WATERLOGGED, false));
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
