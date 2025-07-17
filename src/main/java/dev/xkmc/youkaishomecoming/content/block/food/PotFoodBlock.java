package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PotFoodBlock extends BowlBlock {

	public static final IntegerProperty SERVE_2 = IntegerProperty.create("serve", 1, 2);
	public static final IntegerProperty SERVE_4 = IntegerProperty.create("serve", 1, 4);

	public static class Pot4 extends PotFoodBlock {

		public Pot4(Properties p, Vec3 saucer, ItemLike food) {
			super(p, saucer, food, SERVE_4, 4);
		}

		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			super.createBlockStateDefinition(builder);
			builder.add(SERVE_4);
		}

	}

	public static class Pot2 extends PotFoodBlock {

		public Pot2(Properties p, Vec3 saucer, ItemLike food) {
			super(p, saucer, food, SERVE_2, 2);
		}

		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			super.createBlockStateDefinition(builder);
			builder.add(SERVE_2);
		}

	}

	private final IntegerProperty prop;
	private final int serve;

	public PotFoodBlock(Properties p, Vec3 saucer, ItemLike food, IntegerProperty prop, int serve) {
		super(p, saucer, food);
		this.prop = prop;
		this.serve = serve;
		registerDefaultState(defaultBlockState().setValue(prop, serve));
	}


	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);
		var item = food.asItem().getDefaultInstance();
		if (food.asItem() != asItem()) {
			if (item.hasCraftingRemainingItem()) {
				if (stack.is(item.getCraftingRemainingItem().getItem())) {
					if (!level.isClientSide()) {
						if (!player.isCreative())
							stack.shrink(1);
						player.getInventory().placeItemBackInInventory(item);
						consume(state, level, pos);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	protected void consume(BlockState state, Level level, BlockPos pos) {
		int serve = state.getValue(prop);
		if (serve > 1) {
			level.setBlockAndUpdate(pos, state.setValue(prop, serve - 1));
			return;
		}
		super.consume(state, level, pos);
	}

	public ItemStack asBowls() {
		return food.asItem().getDefaultInstance().copyWithCount(serve);
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, PotFoodBlock b, Item item) {
		pvd.add(b, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(AlternativesEntry.alternatives(
						LootItem.lootTableItem(b.asItem())
								.when(LootTableTemplate.withBlockState(b, b.prop, b.serve)),
						LootItem.lootTableItem(item)))));
	}

}
