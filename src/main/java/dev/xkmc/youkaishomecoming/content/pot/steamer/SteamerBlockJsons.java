package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.*;

public class SteamerBlockJsons {

	private static final Integer[][] VALS = {{1, 2, 3, 4}, {2, 3, 4}, {3, 4}, {4}};

	public static void genPotModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		var pot = pvd.models().getBuilder("steamer_pot")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/steamer_pot")))
				.texture("side", pvd.modLoc("block/utensil/steamer_pot_side"))
				.texture("top", pvd.modLoc("block/utensil/steamer_pot_top"))
				.texture("handle", pvd.modLoc("block/utensil/steamer_pot_handle"))
				.texture("bottom", pvd.modLoc("block/utensil/steamer_pot_bottom"))
				.renderType("cutout");

		var r2 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_rack_2"));
		var r3 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_rack_3"));
		var c2 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_lid_2"));
		var c3 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_lid_3"));

		for (int d = 0; d < 4; d++) {
			var dir = Direction.from2DDataValue(d);
			builder.part().rotationY((int) dir.toYRot()).modelFile(pot).addModel()
					.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
			builder.part().rotationY((int) dir.toYRot()).modelFile(r2).addModel()
					.condition(POT_RACKS, 1, 2)
					.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
			builder.part().rotationY(((int) dir.toYRot() + 180) % 360).modelFile(r3).addModel()
					.condition(POT_RACKS, 2)
					.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
			builder.part().rotationY((int) dir.toYRot()).modelFile(c2).addModel()
					.condition(POT_RACKS, 0).condition(CAPPED, true)
					.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
			builder.part().rotationY((int) dir.toYRot()).modelFile(c3).addModel()
					.condition(POT_RACKS, 1).condition(CAPPED, true)
					.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
		}
	}

	public static void genRackModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		BlockModelBuilder[] caps = new BlockModelBuilder[4];
		for (int h = 0; h < 4; h++) {
			String id = "steamer_lid" + (h == 0 ? "" : "_" + h);
			caps[h] = pvd.models().getBuilder(id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/steamer_lid_" + h)))
					.texture("lid", pvd.modLoc("block/utensil/steamer_lid"))
					.texture("handle", pvd.modLoc("block/utensil/steamer_lid_handle"))
					.renderType("cutout");
		}
		for (int h = 0; h < 4; h++) {
			String id = "steamer_rack" + (h == 0 ? "" : "_" + h);
			var rack = pvd.models().getBuilder(id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/steamer_rack_" + h)))
					.texture("top", pvd.modLoc("block/utensil/steamer_rack_top"))
					.texture("bottom", pvd.modLoc("block/utensil/steamer_rack_bottom"))
					.texture("side", pvd.modLoc("block/utensil/steamer_rack_side"))
					.texture("inside", pvd.modLoc("block/utensil/steamer_rack_inside"))
					.texture("outside", pvd.modLoc("block/utensil/steamer_rack_outside"))
					.texture("rim", pvd.modLoc("block/utensil/steamer_rack_rim"))
					.texture("particle", pvd.modLoc("block/utensil/steamer_rack_top"))
					.renderType("cutout");

			for (int d = 0; d < 4; d++) {
				var dir = Direction.from2DDataValue(d);
				builder.part().rotationY(((int) dir.toYRot() + 180 * h) % 360).modelFile(rack).addModel()
						.condition(RACKS, VALS[h])
						.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
				if (h < 3) {
					builder.part().rotationY((int) dir.toYRot()).modelFile(caps[h + 1]).addModel()
							.condition(RACKS, h + 1).condition(CAPPED, true)
							.condition(BlockTemplates.HORIZONTAL_FACING, dir).end();
				}
			}
		}
	}

	public static void genPotLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_POT.asItem(), 1)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 1))
						.when(helper.intState(block, POT_RACKS, 1)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 2))
						.when(helper.intState(block, POT_RACKS, 2)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_LID.asItem(), 1))
						.when(LootTableTemplate.withBlockState(block, CAPPED, true)))
		);
	}

	public static void genRackLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 1))
						.when(helper.intState(block, RACKS, 1)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 2))
						.when(helper.intState(block, RACKS, 2)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 3))
						.when(helper.intState(block, RACKS, 3)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_RACK.asItem(), 4))
						.when(helper.intState(block, RACKS, 4)))
				.withPool(LootPool.lootPool()
						.add(helper.item(YHBlocks.STEAMER_LID.asItem(), 1))
						.when(LootTableTemplate.withBlockState(block, CAPPED, true)))
		);
	}

	public static void genLidModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var cap = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_lid"));
		pvd.horizontalBlock(ctx.get(), cap);
	}

}
