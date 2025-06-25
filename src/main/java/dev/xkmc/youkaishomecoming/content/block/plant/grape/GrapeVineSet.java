package dev.xkmc.youkaishomecoming.content.block.plant.grape;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.youkaishomecoming.content.block.plant.rope.RopeLoggedCropBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.util.VoxelBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class GrapeVineSet {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 9);

	public final YHCrops crop;
	public final BlockEntry<GrapeTrunk> trunk;
	public final BlockEntry<GrapeVine> center;
	public final BlockEntry<GrapeBranch> side;

	public GrapeVineSet(YHCrops crop) {
		this.crop = crop;
		String name = crop.getName();
		this.trunk = YoukaisHomecoming.REGISTRATE.block(name + "_trunk", p -> new GrapeTrunk(p, crop::getSeed))
				.properties(p -> BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
						.noCollission().randomTicks().strength(1)
						.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
				.blockstate(this::buildTrunkModel)
				.loot(this::buildTrunkLoot)
				.tag(BlockTags.CLIMBABLE)
				.register();
		this.center = YoukaisHomecoming.REGISTRATE.block(name + "_vines", GrapeVine::new)
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate(this::buildVineModel)
				.loot(this::buildVineLoot)
				.tag(BlockTags.CLIMBABLE)
				.register();
		this.side = YoukaisHomecoming.REGISTRATE.block(name + "_branches", GrapeBranch::new)
				.initialProperties(() -> Blocks.WHEAT)
				.blockstate(this::buildBranchModel)
				.loot(this::buildVineLoot)
				.tag(BlockTags.CLIMBABLE)
				.register();
	}

	public class GrapeTrunk extends VineTrunkBlock {

		public GrapeTrunk(Properties prop, ItemLike clone) {
			super(prop, clone);
		}

		@Override
		protected CenterCropVineBlock getTop() {
			return center.get();
		}
	}

	public class GrapeVine extends CenterCropVineBlock {

		public static final VoxelShape SMALL_X = box(0, 0, 7, 16, 16, 9);
		public static final VoxelShape SMALL_Z = box(7, 0, 0, 9, 16, 16);
		public static final VoxelShape LARGE_X = box(0, 0, 4, 16, 16, 12);
		public static final VoxelShape LARGE_Z = box(4, 0, 0, 12, 16, 16);

		public GrapeVine(Properties prop) {
			super(prop);
		}

		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return state.getValue(AXIS) == Direction.Axis.X ?
					state.getValue(getAgeProperty()) >= 5 ? LARGE_X : SMALL_X :
					state.getValue(getAgeProperty()) >= 5 ? LARGE_Z : SMALL_Z;
		}

		@Override
		protected VineTrunkBlock getTrunk() {
			return trunk.get();
		}

		@Override
		protected BranchCropVineBlock getSide() {
			return side.get();
		}

		@Override
		protected ItemLike getFruit() {
			return crop.getFruits();
		}

		@Override
		public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
			return crop.getSeed().getDefaultInstance();
		}

		@Override
		protected IntegerProperty getAgeProperty() {
			return AGE;
		}

		@Override
		public int getMaxAge() {
			return 9;
		}

		@Override
		protected int getBaseAge() {
			return 6;
		}

		@Override
		protected int getFirstAge() {
			return 3;
		}

	}

	public class GrapeBranch extends BranchCropVineBlock {

		public static final VoxelShape[] SMALL, LARGE;

		static {
			SMALL = new VoxelShape[4];
			LARGE = new VoxelShape[4];
			for (int i = 0; i < 4; i++) {
				SMALL[i] = new VoxelBuilder(7, 0, 0, 9, 16, 16)
						.rotateFromNorth(Direction.from2DDataValue(i));
				LARGE[i] = new VoxelBuilder(4, 0, 0, 12, 16, 16)
						.rotateFromNorth(Direction.from2DDataValue(i));
			}
		}

		public GrapeBranch(Properties prop) {
			super(prop);
		}

		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return (state.getValue(getAgeProperty()) >= 5 ? LARGE : SMALL)[state.getValue(FACING).get2DDataValue()];
		}

		@Override
		protected CenterCropVineBlock getCenter() {
			return center.get();
		}

		@Override
		public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
			return crop.getSeed().getDefaultInstance();
		}

		@Override
		protected ItemLike getFruit() {
			return crop.getFruits();
		}

		@Override
		protected IntegerProperty getAgeProperty() {
			return AGE;
		}

		@Override
		public int getMaxAge() {
			return 9;
		}

		@Override
		protected int getBaseAge() {
			return 6;
		}

	}

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

	protected void buildTrunkModel(DataGenContext<Block, GrapeTrunk> ctx, RegistrateBlockstateProvider pvd) {
		String name = crop.getName();
		String type = crop.getTypeName();
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			boolean merged = state.getValue(GrapeTrunk.MERGED);
			if (merged) {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("tree_" + name + "_trunk")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_trunk")))
						.texture("cross", "block/plants/" + type + "/trunk_cross")
						.texture("post", "block/plants/" + type + "/trunk_post")
						.renderType("cutout")
				).build();
			} else {
				return ConfiguredModel.builder().modelFile(pvd.models().getBuilder("tree_" + name + "_trunk_rope")
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rope_crop")))
						.texture("cross", "block/plants/" + type + "/trunk_cross")
						.texture("rope_side", pvd.modLoc("block/plants/rope"))
						.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
						.renderType("cutout")
				).build();
			}
		});
	}

	protected void buildTrunkLoot(RegistrateBlockLootTables pvd, VineTrunkBlock block) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.seed))
				.add(LootItem.lootTableItem(Items.STICK))
		));
	}

	protected void buildVineLoot(RegistrateBlockLootTables pvd, BaseCropVineBlock block) {
		pvd.add(block, pvd.applyExplosionDecay(block,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(block.getAgeProperty(), block.getMaxAge())))
						.add(LootItem.lootTableItem(crop.getFruits())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))
				)));
	}

	protected void buildVineModel(DataGenContext<Block, GrapeVine> ctx, RegistrateBlockstateProvider pvd) {
		String name = crop.getName();
		String[] strs = name.split("_");
		String col = strs[0];
		String type = strs[1];
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(GrapeCropBlock.AGE);
			boolean l = state.getValue(CenterCropVineBlock.LEFT);
			boolean r = state.getValue(CenterCropVineBlock.RIGHT);
			boolean top = state.getValue(BaseCropVineBlock.TOP);
			String suffix = "";
			if (age == ctx.get().getMaxAge()) {
				suffix = "_" + col;
			}
			String coil = "block/plants/" + type + "/vine/coil" + age + suffix;
			String vine = "block/plants/" + type + "/vine/vine" + age + suffix;
			var builder = pvd.models().getBuilder("tree_" + name + "_vine" + age + (l ? "l" : "") + (r ? "r" : "") + (top ? "_top" : ""));
			if (age >= 5) {
				if (top) {
					builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_vine_mature_top")));
				} else {
					String bottom = "block/plants/" + type + "/vine/bottom" + age + suffix;
					builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_vine_mature")));
					builder.texture("bottom", pvd.modLoc(bottom));
				}
				String leaves = "block/plants/" + type + "/leaves/leaves" + age + suffix;
				builder.texture("leaves", pvd.modLoc(leaves));
			} else {
				builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_vine")));
			}
			builder.texture("coil", pvd.modLoc(coil))
					.texture("coil_left", pvd.modLoc(l ? coil : vine))
					.texture("coil_right", pvd.modLoc(r ? coil : vine))
					.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
					.renderType("cutout");
			return ConfiguredModel.builder().modelFile(builder).rotationY(
					state.getValue(CenterCropVineBlock.AXIS) == Direction.Axis.X ? 0 : 90
			).build();
		});
	}

	protected void buildBranchModel(DataGenContext<Block, GrapeBranch> ctx, RegistrateBlockstateProvider pvd) {
		String name = crop.getName();
		String[] strs = name.split("_");
		String col = strs[0];
		String type = strs[1];
		pvd.horizontalBlock(ctx.get(), state -> {
			int age = state.getValue(GrapeCropBlock.AGE);
			boolean top = state.getValue(BaseCropVineBlock.TOP);
			boolean ext = state.getValue(BranchCropVineBlock.EXTENDED);
			String suffix = "";
			if (age == ctx.get().getMaxAge()) {
				suffix = "_" + col;
			}
			String coil = "block/plants/" + type + "/branch/coil" + age + suffix;
			String vine = "block/plants/" + type + "/branch/vine" + age + suffix;
			var builder = pvd.models().getBuilder("tree_" + name + "_branch" + age + (top ? "_top" : "") + (ext ? "_extended" : ""));
			if (age >= 5) {
				if (top) {
					builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_branch_mature_top")));
				} else {
					String bottom = "block/plants/" + type + "/branch/bottom" + age + suffix;
					builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_branch_mature")));
					builder.texture("bottom", pvd.modLoc(bottom));
				}
				String leaves = "block/plants/" + type + "/leaves/leaves" + age + suffix;
				builder.texture("leaves", pvd.modLoc(leaves));
			} else {
				builder.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/plant/grape_branch")));
			}
			builder.texture("coil", pvd.modLoc(coil))
					.texture("vine", pvd.modLoc(ext ? coil : vine))
					.texture("rope_top", pvd.modLoc("block/plants/rope_top"))
					.renderType("cutout");
			return builder;
		}, -90);
	}

}
