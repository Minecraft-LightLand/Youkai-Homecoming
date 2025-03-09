package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.client.model.generators.ModelFile;

import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.POT_RACKS;
import static dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerStates.RACKS;

public class SteamerBlockJsons {

	private static final Integer[][] VALS = {{1, 2, 3, 4}, {2, 3, 4}, {3, 4}, {4}};

	public static void genPotModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		var pot = pvd.models().getBuilder("steamer_pot")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/steamer_pot")))
				.texture("side", pvd.modLoc("block/steamer_pot_side"))
				.texture("top", pvd.modLoc("block/steamer_pot_top"))
				.texture("handle", pvd.modLoc("block/steamer_pot_handle"))
				.texture("bottom", pvd.modLoc("block/steamer_pot_bottom"))
				.renderType("cutout");

		for (int d = 0; d < 4; d++) {
			var dir = Direction.from2DDataValue(d);
			builder.part().rotationY((int) dir.toYRot()).modelFile(pot).addModel()
					.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
			var r2 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_rack_2"));
			var r3 = new ModelFile.UncheckedModelFile(pvd.modLoc("block/steamer_rack_3"));
			builder.part().rotationY((int) dir.toYRot()).modelFile(r2).addModel()
					.condition(POT_RACKS, 1, 2)
					.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
			builder.part().rotationY((int) dir.toYRot()).modelFile(r3).addModel()
					.condition(POT_RACKS, 2)
					.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
		}
	}


	public static void genRackModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		for (int h = 0; h < 4; h++) {
			String id = "steamer_rack" + (h == 0 ? "" : "_" + h);
			var rack = pvd.models().getBuilder(id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/steamer_rack_" + h)))
					.texture("top", pvd.modLoc("block/steamer_rack_top"))
					.texture("bottom", pvd.modLoc("block/steamer_rack_bottom"))
					.texture("side", pvd.modLoc("block/steamer_rack_side"))
					.texture("inside", pvd.modLoc("block/steamer_rack_inside"))
					.texture("outside", pvd.modLoc("block/steamer_rack_outside"))
					.texture("rim", pvd.modLoc("block/steamer_rack_rim"))
					.texture("particle", pvd.modLoc("block/steamer_rack_top"))
					.renderType("cutout");

			for (int d = 0; d < 4; d++) {
				var dir = Direction.from2DDataValue(d);
				builder.part().rotationY((int) dir.toYRot()).modelFile(rack).addModel()
						.condition(RACKS, VALS[h])
						.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
			}
		}
	}

	public static void genPotLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_POT.asItem(), 1)))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 1))
						.when(LootTableTemplate.withBlockState(block, POT_RACKS, 1)))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 2))
						.when(LootTableTemplate.withBlockState(block, POT_RACKS, 2)))
		);
	}

	public static void genRackLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 1))
						.when(LootTableTemplate.withBlockState(block, RACKS, 1)))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 2))
						.when(LootTableTemplate.withBlockState(block, RACKS, 2)))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 3))
						.when(LootTableTemplate.withBlockState(block, RACKS, 3)))
				.withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(YHBlocks.STEAMER_RACK.asItem(), 4))
						.when(LootTableTemplate.withBlockState(block, RACKS, 4)))
		);
	}
}
