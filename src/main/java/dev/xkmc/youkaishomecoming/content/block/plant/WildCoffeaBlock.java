package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class WildCoffeaBlock extends DoublePlantBlock {

	public WildCoffeaBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public static void buildWildLoot(RegistrateBlockLootTables pvd, WildCoffeaBlock block, YHCrops crop) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(helper.silk())
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(crop.getFruits())
								.apply(helper.fortuneBin())))
				).when(helper.enumState(block, HALF, DoubleBlockHalf.LOWER))));
	}

	public static void buildWildModel(DataGenContext<Block, WildCoffeaBlock> ctx, RegistrateBlockstateProvider pvd, YHCrops crop) {
		String tex = crop.getName();
		pvd.getVariantBuilder(ctx.get()).forAllStatesExcept(state ->
				ConfiguredModel.builder().modelFile(pvd.models()
								.cross(tex + (state.getValue(HALF) == DoubleBlockHalf.UPPER ? "" : "_bottom"),
										pvd.modLoc("block/wild_" + tex +
												(state.getValue(HALF) == DoubleBlockHalf.UPPER ? "_top" : "_bottom")))
								.renderType("cutout"))
						.build());
	}


}
