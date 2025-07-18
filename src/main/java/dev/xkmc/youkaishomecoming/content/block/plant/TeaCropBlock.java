package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
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

public class TeaCropBlock extends DoubleCropBlock implements HarvestableBlock {

	private static final VoxelShape SMALL = Block.box(4, 0, 4, 12, 11, 12);
	private static final VoxelShape TOP = Block.box(0, 0, 0, 16, 8, 16);

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0, 0, 0, 16, 5, 16),
			Block.box(0, 0, 0, 16, 7, 16),
			Block.box(0, 0, 0, 16, 11, 16),
			Block.box(0, 0, 0, 16, 14, 16)};

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 8);

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
		return 8;
	}

	protected int getResetAge() {
		return 4;
	}

	@Override
	protected float modifySpeed(BlockState state, float val) {
		if (state.getValue(AGE) >= getDoubleBlockStart()) {
			return val / 4;
		}
		return super.modifySpeed(state, val);
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
		if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(AGE) >= getDoubleBlockStart()) {
			if (!level.isClientSide()) {
				popResource(level, pos, getPickupResult(level, state));
				level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
						1.0F, 0.8F + level.random.nextFloat() * 0.4F);
				BlockState blockstate = state.setValue(AGE, getResetAge());
				setGrowth(level, pos, getResetAge(), 2);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}

	protected ItemStack getPickupResult(Level level, BlockState state) {
		if (state.getValue(AGE) == getMaxAge()) {
			return new ItemStack(YHCrops.TEA.getSeed(), 1);
		} else if (state.getValue(AGE) == getMaxAge() - 1) {
			return new ItemStack(YHItems.CAMELLIA.get(), 1);
		} else if (state.getValue(AGE) >= getDoubleBlockStart()) {
			int j = 1 + level.random.nextInt(2);
			return new ItemStack(YHCrops.TEA.getFruits(), j);
		} else return ItemStack.EMPTY;
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		BlockPos lower;
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			lower = pos.below();
			state = level.getBlockState(lower);
			if (state.getBlock() != this) return null;
		} else lower = pos;
		var stack = getPickupResult(level, state);
		if (stack.isEmpty()) return null;
		return new HarvestResult((l, p) -> setGrowth(l, lower, getResetAge(), 2), List.of(stack));
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(YHTagGen.FARMLAND_TEA);
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return  PlantType.get("tea");
	}

	protected ItemLike getBaseSeedId() {
		return seed.get();
	}

	@Override
	public int getDoubleBlockStart() {
		return 6;
	}

	protected int getBonemealAgeIncrease(Level p_52262_) {
		return 1;
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		int age = getAge(pState);
		if (age <= 3) {
			return SHAPE_BY_AGE[age];
		}
		if (age == 4) {
			return SMALL;
		}
		if (age >= getDoubleBlockStart() && pState.getValue(HALF) == DoubleBlockHalf.UPPER)
			return TOP;
		return Shapes.block();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int age = getAge(state);
		if (age <= 3) return Shapes.empty();
		return getShape(state, level, pos, ctx);
	}

	public static void buildPlantModel(DataGenContext<Block, TeaCropBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String tex = name + "_stage" + age;
			if (age < 4) {
				if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
					return ConfiguredModel.builder().modelFile(pvd.models()
							.withExistingParent(tex + "_upper", pvd.mcLoc("block/air"))
							.texture("particle", pvd.modLoc("block/plants/" + name + "/" + tex))
					).build();
				}
				return ConfiguredModel.builder().modelFile(pvd.models()
						.cross(tex, pvd.modLoc("block/plants/" + name + "/" + tex)).renderType("cutout")).build();
			}
			if (age == 4) {
				if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
					return ConfiguredModel.builder().modelFile(pvd.models()
							.withExistingParent(tex + "_upper", pvd.mcLoc("block/air"))
							.texture("particle", pvd.modLoc("block/plants/" + name + "/small_" + name + "_bush_top"))
					).build();
				}
				var file = pvd.models()
						.getBuilder("block/" + tex).ao(false).renderType("cutout")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/small_bush")))
						.texture("top", pvd.modLoc("block/plants/" + name + "/small_" + name + "_bush_top"))
						.texture("side", pvd.modLoc("block/plants/" + name + "/small_" + name + "_bush_side"))
						.texture("inside", pvd.modLoc("block/plants/" + name + "/small_" + name + "_bush_inside"));
				return ConfiguredModel.builder().modelFile(file).build();
			}
			if (age == 5) {
				if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
					return ConfiguredModel.builder().modelFile(pvd.models()
							.withExistingParent(tex + "_upper", pvd.mcLoc("block/air"))
							.texture("particle", pvd.modLoc("block/plants/" + name + "/" + name + "_bush_side"))
					).build();
				}
				var file = pvd.models()
						.getBuilder("block/" + tex).ao(false).renderType("cutout")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/medium_bush")))
						.texture("top", pvd.modLoc("block/plants/" + name + "/" + name + "_bush_top"))
						.texture("side", pvd.modLoc("block/plants/" + name + "/" + name + "_bush_side"))
						.texture("inside", pvd.modLoc("block/plants/" + name + "/medium_" + name + "_bush_inside"));
				return ConfiguredModel.builder().modelFile(file).build();
			}
			String side = name;
			if (age == 8) side = name + "_seed";
			if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				if (age == 7) {
					var file = pvd.models()
							.getBuilder("block/" + tex + "_upper").ao(false).renderType("cutout")
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/large_bush_top_blossom")))
							.texture("top", pvd.modLoc("block/plants/" + name + "/" + side + "_bush_top"))
							.texture("side", pvd.modLoc("block/plants/" + name + "/" + side + "_bush_side"))
							.texture("inside", pvd.modLoc("block/plants/" + name + "/medium_" + name + "_bush_inside"))
							.texture("base", pvd.modLoc("block/plants/" + name + "/camellia_flower_base"));
					return ConfiguredModel.builder().modelFile(file).build();
				}
				var file = pvd.models()
						.getBuilder("block/" + tex + "_upper").ao(false).renderType("cutout")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/large_bush_top")))
						.texture("top", pvd.modLoc("block/plants/" + name + "/" + side + "_bush_top"))
						.texture("side", pvd.modLoc("block/plants/" + name + "/" + side + "_bush_side"))
						.texture("inside", pvd.modLoc("block/plants/" + name + "/medium_" + name + "_bush_inside"));
				return ConfiguredModel.builder().modelFile(file).build();
			}
			var file = pvd.models()
					.getBuilder("block/" + tex).ao(false).renderType("cutout")
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/large_bush_bottom")))
					.texture("side", pvd.modLoc("block/plants/" + name + "/" + side + "_bush_side"))
					.texture("inside", pvd.modLoc("block/plants/" + name + "/large_" + name + "_bush_inside"));
			return ConfiguredModel.builder().modelFile(file).build();
		});
	}

	public static void buildPlantLoot(RegistrateBlockLootTables pvd, TeaCropBlock block, YHCrops crop) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
								.add(LootItem.lootTableItem(crop.getSeed()))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HALF, DoubleBlockHalf.LOWER))))
		));
	}

}