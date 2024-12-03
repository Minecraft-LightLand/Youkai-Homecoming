package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class CoffeaCropBlock extends DoubleCropBlock implements HarvestableBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D)};

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 11);

	private final Supplier<Item> seed;

	public CoffeaCropBlock(Properties prop, Supplier<Item> seed) {
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
		return 11;
	}

	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(AGE) != getMaxAge() && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		}
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			state = level.getBlockState(pos.below());
			pos = pos.below();
		}
		if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(AGE) == getMaxAge()) {
			if (!level.isClientSide()) {
				int j = 1 + level.random.nextInt(2);
				popResource(level, pos, new ItemStack(YHCrops.COFFEA.getFruits(), j));
				level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
						1.0F, 0.8F + level.random.nextFloat() * 0.4F);
				BlockState blockstate = state.setValue(AGE, 8);
				setGrowth(level, pos, 8, 2);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		BlockPos lower;
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			lower = pos.below();
			state = level.getBlockState(lower);
			if (state.getBlock() != this) return null;
		} else lower = pos;
		if (state.getValue(AGE) < getMaxAge())
			return null;
		return new HarvestResult((l, p) -> setGrowth(l, lower, 8, 2), List.of(
				new ItemStack(YHCrops.COFFEA.getFruits(), 1 + level.random.nextInt(2))
		));
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(YHTagGen.FARMLAND_COFFEA);
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
		if (age <= 3) {
			return SHAPE_BY_AGE[age];
		}
		if (age == 6 && pState.getValue(HALF) == DoubleBlockHalf.UPPER)
			return SHAPE_BY_AGE[4];
		return Shapes.block();
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.get("coffea");
	}

	public static void buildPlantModel(DataGenContext<Block, CoffeaCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String tex = name + "_stage" + age;
			if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				return ConfiguredModel.builder().modelFile(pvd.models()
						.withExistingParent(tex + "_upper", pvd.mcLoc("block/air"))
						.texture("particle", pvd.modLoc("block/" + name + "_leaves"))
				).build();
			}
			if (age <= 5) {
				return ConfiguredModel.builder().modelFile(pvd.models()
						.cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
			} else {
				var file = pvd.models()
						.getBuilder("block/" + tex)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + tex)));
				file.ao(false);
				file.renderType("cutout");
				if (age == 6 || age == 7) {
					file.texture("leaves", pvd.modLoc("block/" + name + "_leaves"));
				}
				file.texture("top", pvd.modLoc("block/" + name + "_bush_top"));
				file.texture("side", pvd.modLoc("block/" + name + "_bush_side"));
				file.texture("trunk", pvd.modLoc("block/" + name + "_trunk" + age));
				file.texture("particle", pvd.modLoc("block/" + name + "_trunk" + age));
				return ConfiguredModel.builder().modelFile(file).build();
			}
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, CoffeaCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed()))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER))))
						.withPool(LootPool.lootPool()
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 11)))
								.add(LootItem.lootTableItem(crop.getFruits())
										.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER)))
						)));
	}

}