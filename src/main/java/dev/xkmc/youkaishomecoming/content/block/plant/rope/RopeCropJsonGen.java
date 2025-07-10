package dev.xkmc.youkaishomecoming.content.block.plant.rope;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class RopeCropJsonGen {

	public static void buildRootedModel(DataGenContext<Block, ? extends RootedClimbingCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			boolean rope = state.getValue(RopeLoggedCropBlock.ROPELOGGED);
			boolean base = state.getValue(RootedClimbingCropBlock.BASE);
			int age = state.getValue(CropBlock.AGE);
			String tex = "block/plants/" + name + "/" + (base ? "base" : "vine") + age;
			if (rope) {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("rope_" + name + "_" + (base ? "base" : "vine") + age)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop")))
						.texture("cross", tex)
						.texture("rope_side", pvd.modLoc("block/plants/rope"))
						.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
						.renderType("cutout")
				).build();
			} else {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("bare_" + name + "_" + (base ? "base" : "vine") + age)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop_bare")))
						.texture("cross", tex)
						.renderType("cutout")
				).build();
			}
		});
	}

}
