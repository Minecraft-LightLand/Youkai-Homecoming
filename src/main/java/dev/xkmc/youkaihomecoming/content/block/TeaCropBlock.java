
package dev.xkmc.youkaihomecoming.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class TeaCropBlock extends DoubleCropBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D)};

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);

	private final Supplier<Item> seed;

	public TeaCropBlock(Properties prop, Supplier<Item> seed) {
		super(prop);
		this.seed = seed;
		registerDefaultState(defaultBlockState().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, HALF);
	}

	@Override
	protected IntegerProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 6;
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return super.mayPlaceOn(pState, pLevel, pPos);
	}

	protected ItemLike getBaseSeedId() {
		return seed.get();
	}

	@Override
	public int getDoubleBlockStart() {
		return 6;
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		int age = getAge(pState);
		if (age <= 4) {
			return SHAPE_BY_AGE[age];
		}
		if (age == 6 && pState.getValue(HALF) == DoubleBlockHalf.UPPER)
			return SHAPE_BY_AGE[5];
		return Shapes.block();
	}

	public static void buildPlantModel(DataGenContext<Block, TeaCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String tex = name + "_stage" + age;
			if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				return ConfiguredModel.builder().modelFile(pvd.models()
						.withExistingParent(tex + "_upper", pvd.mcLoc("block/air"))
						.texture("particle", pvd.modLoc("block/" + name + "_bush_leaves"))
				).build();
			}
			if (age <= 4) {
				return ConfiguredModel.builder().modelFile(pvd.models()
						.cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
			} else {
				var file = pvd.models()
						.getBuilder("block/" + tex)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + tex)));
				file.ao(false);
				file.renderType("cutout");
				if (age == 5) {
					file.texture("base", pvd.modLoc("block/" + name + "_stage5"));
				} else {
					file.texture("leaves", pvd.modLoc("block/" + name + "_bush_leaves"));
					file.texture("trunk", pvd.modLoc("block/" + name + "_bush_trunk"));
				}
				file.texture("top", pvd.modLoc("block/" + name + "_bush_top"));
				file.texture("side", pvd.modLoc("block/" + name + "_bush_side"));
				return ConfiguredModel.builder().modelFile(file).build();
			}
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, TeaCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed()))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER))))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 6)))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER)))
						)));
	}

}