package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.item.fluid.IYHFluidHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;

public class BottleBlock extends HorizontalDirectionalBlock implements LeftClickBlock {

	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 4);
	private static final VoxelShape[] SHAPE = {
			box(6, 0, 6, 10, 14, 10),
			box(3, 0, 5, 13, 14, 11),
			box(5, 0, 3, 11, 14, 13),
			box(3, 0, 3, 13, 14, 13)
	};

	public BottleBlock(Properties prop) {
		super(prop);
		registerDefaultState(defaultBlockState().setValue(COUNT, 1));
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COUNT, FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int count = state.getValue(COUNT);
		if (count <= 1) return SHAPE[0];
		if (count == 2) {
			var dir = state.getValue(FACING);
			return dir.getAxis() == Direction.Axis.Z ? SHAPE[1] : SHAPE[2];
		}
		return SHAPE[3];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(this.asItem())) {
			int count = state.getValue(COUNT);
			if (count < 4) {
				if (!level.isClientSide()) {
					level.setBlockAndUpdate(pos, state.setValue(COUNT, count + 1));
					if (!player.getAbilities().instabuild) {
						stack.shrink(1);
					}
					level.playSound(null, pos, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1, 1);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public boolean leftClick(BlockState state, Level level, BlockPos pos, Player player) {
		if (player.getCooldowns().isOnCooldown(asItem()))
			return level.isClientSide;
		player.getCooldowns().addCooldown(asItem(), 4);
		if (level.isClientSide()) return true;
		int count = state.getValue(COUNT);
		if (count <= 1) {
			level.removeBlock(pos, false);
		} else {
			level.setBlockAndUpdate(pos, state.setValue(COUNT, count - 1));
		}
		if (!player.getAbilities().instabuild) {
			player.getInventory().placeItemBackInInventory(asItem().getDefaultInstance());
		}
		level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 1);
		return true;
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, BottleBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem()))
						.when(LootTableTemplate.withBlockState(block, COUNT, 1)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem()))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
						.when(LootTableTemplate.withBlockState(block, COUNT, 2)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem()))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
						.when(LootTableTemplate.withBlockState(block, COUNT, 3)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem()))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
						.when(LootTableTemplate.withBlockState(block, COUNT, 4)))
		);
	}

	public static void buildModel(DataGenContext<Block, BottleBlock> ctx, RegistrateBlockstateProvider pvd, IYHFluidHolder drink) {
		String folder = drink.bottleTextureFolder();
		pvd.horizontalBlock(ctx.get(), state -> {
			var count = state.getValue(COUNT);
			String suffix = count == 1 ? "" : "_" + count;
			return pvd.models().getBuilder("block/" + ctx.getName() + suffix)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bottle/" + folder + suffix)))
					.texture("bottle", pvd.modLoc("block/bottle/" + folder + "/" + ctx.getName()))
					.renderType("cutout");
		});
	}


}
