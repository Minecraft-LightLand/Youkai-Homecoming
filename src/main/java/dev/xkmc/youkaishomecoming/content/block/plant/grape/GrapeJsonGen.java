package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class GrapeJsonGen {


	public static void buildPlantModel(DataGenContext<Block, GrapeCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		String[] strs = name.split("_");
		String col = strs[0];
		String type = strs[1];
		int start = ctx.get().getDoubleBlockStart();
		var empty = pvd.models()
				.withExistingParent("small_" + name + "_upper_empty", pvd.mcLoc("block/air"))
				.texture("particle", pvd.modLoc("block/plants/" + type + "/small/upper" + start));
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(GrapeCropBlock.AGE);
			boolean rope = state.getValue(RopeLoggedCropBlock.ROPELOGGED);
			boolean root = state.getValue(DoubleRopeCropBlock.ROOT);
			if (!root && age < start) {
				return ConfiguredModel.builder().modelFile(empty).build();
			}
			String tag = (root ? "lower" : "upper") + age;
			String tex = "block/plants/" + type + "/small/" + tag;
			if (age == ctx.get().getMaxAge()) {
				tex += "_" + col;
			}
			if (rope) {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("small_rope_" + name + "_" + tag)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop")))
						.texture("cross", tex)
						.texture("rope_side", pvd.modLoc("block/plants/rope"))
						.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
						.renderType("cutout")
				).build();
			} else {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("small_bare_" + name + "_" + tag)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop_bare")))
						.texture("cross", tex)
						.renderType("cutout")
				).build();
			}
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, GrapeCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed()))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(block.getAgeProperty(), block.getMaxAge())).invert())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoubleRopeCropBlock.ROOT, true))))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(block.getAgeProperty(), block.getMaxAge())))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoubleRopeCropBlock.ROOT, true)))
						)));
	}

}
