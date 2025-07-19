package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.content.block.variants.LeftClickBlock;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;

public class BowlBlock extends HorizontalDirectionalBlock implements ISteamerContentBlock, LeftClickBlock {

	public static final Vec3 IRON_SHAPE = new Vec3(4, 4, 4);
	public static final Vec3 WOOD_SHAPE = new Vec3(5, 3, 5);
	public static final Vec3 BAMBOO_SHAPE = new Vec3(2, 3, 5.5);
	public static final Vec3 RAW_BAMBOO_SHAPE = new Vec3(2, 5, 5.5);
	public static final Vec3 POT_SHAPE = new Vec3(2, 6, 2);
	public static final Vec3 STOCKPOT_SHAPE = new Vec3(1, 15, 1);

	private final VoxelShape shape_x, shape_z;
	protected final ItemLike food;

	public BowlBlock(Properties prop, Vec3 saucer, ItemLike food) {
		super(prop);
		this.food = food;
		shape_x = Block.box(saucer.x, 0, saucer.z, 16 - saucer.x, saucer.y, 16 - saucer.z);
		shape_z = Block.box(saucer.z, 0, saucer.x, 16 - saucer.z, saucer.y, 16 - saucer.x);

	}

	public BowlBlock(Properties prop, Vec3 saucer) {
		super(prop);
		food = this;
		shape_x = Block.box(saucer.x, 0, saucer.z, 16 - saucer.x, saucer.y, 16 - saucer.z);
		shape_z = Block.box(saucer.z, 0, saucer.x, 16 - saucer.z, saucer.y, 16 - saucer.x);
	}

	@Override
	public float clearance() {
		return 2;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(FACING).getAxis() == Direction.Axis.Z ? shape_x : shape_z;
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		return collectBowl(state, level, pos, player, YHBlocks.WOOD_BOWL.get(), Items.BOWL) ||
				collectBowl(state, level, pos, player, YHBlocks.BAMBOO_BOWL.get(), Items.BAMBOO) ||
				collectBowl(state, level, pos, player, YHBlocks.IRON_BOWL.get(), YHBlocks.IRON_BOWL.asItem());
	}

	private boolean collectBowl(BlockState state, Level level, BlockPos pos, Player player, Block block, Item item) {
		if (state.is(block)) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				player.getInventory().placeItemBackInInventory(item.getDefaultInstance());
			}
			return true;
		}
		return false;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);
		var item = food.asItem().getDefaultInstance();
		var food = item.getFoodProperties(player);
		if (food == null || !player.canEat(false)) return InteractionResult.PASS;
		if (!stack.isEmpty()) return InteractionResult.PASS;//TODO spoon
		if (!level.isClientSide()) {
			player.eat(level, item.copy());
			consume(state, level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	protected void consume(BlockState state, Level level, BlockPos pos) {
		var cont = asItem().getDefaultInstance().getCraftingRemainingItem();
		if (cont.getItem() instanceof BlockItem block) {
			level.setBlockAndUpdate(pos, block.getBlock().defaultBlockState()
					.setValue(BlockStateProperties.HORIZONTAL_FACING,
							state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
		} else {
			level.setBlockAndUpdate(pos, YHBlocks.WOOD_BOWL.getDefaultState()
					.setValue(BlockStateProperties.HORIZONTAL_FACING,
							state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
		}
	}

	private static void buildBowlModel(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd, String folder) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/" + folder + "/" + ctx.getName())))
				.texture("bowl", pvd.modLoc("block/bowl/" + folder + "/" + folder + "_bowl"))
				.texture("base", pvd.modLoc("block/bowl/" + folder + "/" + ctx.getName()))
				.renderType("cutout"));
	}

	public static BlockBuilder<IronBowlBlock, L2Registrate> ironBowl(String id) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new IronBowlBlock(p, BowlBlock.IRON_SHAPE))
				.properties(p -> p.mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))
				.blockstate((ctx, pvd) -> buildBowlModel(ctx, pvd, "iron"))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	public static BlockBuilder<BowlBlock, L2Registrate> ironBowlFood(String id) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new BowlBlock(p, BowlBlock.IRON_SHAPE))
				.properties(p -> p.mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))
				.blockstate((ctx, pvd) -> buildBowlModel(ctx, pvd, "iron"))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	public static BlockBuilder<BowlBlock, L2Registrate> woodBowlFood(String id) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new BowlBlock(p, BowlBlock.WOOD_SHAPE))
				.properties(p -> p.mapColor(MapColor.WOOD).strength(0.5F, 1.0F).sound(SoundType.WOOD))
				.blockstate((ctx, pvd) -> buildBowlModel(ctx, pvd, "wood"))
				.tag(BlockTags.MINEABLE_WITH_AXE);
	}

	public static BlockBuilder<BowlBlock, L2Registrate> rawBambooBowl(String id) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new BowlBlock(p, BowlBlock.RAW_BAMBOO_SHAPE))
				.properties(p -> p.mapColor(MapColor.PLANT).strength(0.2F, 0.5F).sound(SoundType.WOOD))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						pvd.models().getBuilder("block/" + ctx.getName())
								.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/bamboo/raw_bamboo")))
								.texture("bamboo", pvd.modLoc("block/bowl/bamboo/raw_bamboo"))
								.renderType("cutout")))
				.tag(BlockTags.MINEABLE_WITH_AXE);
	}

	public static BlockBuilder<BowlBlock, L2Registrate> bambooBowl(String id) {
		return YoukaisHomecoming.REGISTRATE.block(id, p -> new BowlBlock(p, BowlBlock.BAMBOO_SHAPE))
				.properties(p -> p.mapColor(MapColor.PLANT).strength(0.2F, 0.5F).sound(SoundType.WOOD))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(),
						pvd.models().getBuilder("block/" + ctx.getName())
								.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/bamboo/" + ctx.getName())))
								.texture("out", pvd.modLoc("block/bowl/bamboo/bamboo_bowl"))
								.texture("in", pvd.modLoc("block/bowl/bamboo/bamboo_inside"))
								.texture("leaf", pvd.modLoc("block/bowl/bamboo/bamboo_leaf"))
								.texture("base", pvd.modLoc("block/bowl/bamboo/" + ctx.getName()))
								.renderType("cutout")))
				.tag(BlockTags.MINEABLE_WITH_AXE);
	}

}
