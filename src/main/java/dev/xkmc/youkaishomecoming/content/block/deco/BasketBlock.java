package dev.xkmc.youkaishomecoming.content.block.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import org.jetbrains.annotations.Nullable;

public class BasketBlock {

	public static final int MAX = 11;
	public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, MAX);
	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

	public static final BlockEntry<DelegateBlock> BASKET;

	static {

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.TAB.getKey());
		BASKET = YoukaisHomecoming.REGISTRATE.block("short_basket", p -> DelegateBlock.newBaseBlock(p,
						BlockProxy.HORIZONTAL, new Empty()))
				.blockstate(BasketBlock::buildBasketModel)
				.initialProperties(() -> Blocks.BAMBOO_SLAB)
				.simpleItem()
				.tag(BlockTags.MINEABLE_WITH_AXE)
				.register();

		YoukaisHomecoming.REGISTRATE.defaultCreativeTab(YoukaisHomecoming.CROP.getKey());
		Baskets.register();
	}

	public record Empty() implements ShapeBlockMethod, OnClickBlockMethod {

		@Override
		public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return SHAPE;
		}

		@Override
		public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
			var stack = player.getItemInHand(hand);
			for (var e : Baskets.values()) {
				if (e.test.get().test(stack)) {
					if (!level.isClientSide()) {
						stack.shrink(1);
						var next = e.block.getDefaultState()
								.setValue(BlockProxy.HORIZONTAL_FACING, state.getValue(BlockProxy.HORIZONTAL_FACING))
								.setValue(COUNT, 1);
						level.setBlockAndUpdate(pos, next);
					}
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}

	}

	public record Filled(Baskets baskets) implements CreateBlockStateBlockMethod, DefaultStateBlockMethod,
			ShapeBlockMethod, OnClickBlockMethod {

		@Override
		public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
			var stack = player.getItemInHand(hand);
			int current = state.getValue(COUNT);
			if (stack.isEmpty()) {
				if (!level.isClientSide()) {
					player.getInventory().placeItemBackInInventory(baskets.item.asItem().getDefaultInstance());
					var next = current == 1 ? BASKET.getDefaultState()
							.setValue(BlockProxy.HORIZONTAL_FACING, state.getValue(BlockProxy.HORIZONTAL_FACING)) :
							state.setValue(COUNT, current - 1);
					level.setBlockAndUpdate(pos, next);
				}
				return InteractionResult.SUCCESS;
			}
			if (current < MAX && baskets.test.get().test(stack)) {
				if (!level.isClientSide()) {
					stack.shrink(1);
					var next = baskets.block.getDefaultState()
							.setValue(BlockProxy.HORIZONTAL_FACING, state.getValue(BlockProxy.HORIZONTAL_FACING))
							.setValue(COUNT, current + 1);
					level.setBlockAndUpdate(pos, next);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(COUNT);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(COUNT, MAX);
		}

		@Override
		public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
			return SHAPE;
		}

	}

	public static void buildBasketModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/basket/basket")))
				.texture("top", pvd.modLoc("block/basket/basket_top"))
				.texture("side", pvd.modLoc("block/basket/basket_side"))
				.texture("parts", pvd.modLoc("block/basket/basket_parts"))
				.renderType("cutout")
		);
	}

	public static void buildStackModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd, String id, String model, boolean extra) {
		var builder = pvd.getMultipartBuilder(ctx.get());
		var basket = new ModelFile.UncheckedModelFile(pvd.modLoc("block/short_basket"));
		var layer = pvd.models().getBuilder("block/" + id + "_stack6")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/basket/basket_stack")))
				.texture("top", pvd.modLoc("block/basket/" + id + "_stack_layer"))
				.texture("side", pvd.modLoc("block/basket/" + id + "_stack_layer_side"))
				.renderType("cutout");
		ModelFile[] low = new ModelFile[5];
		ModelFile[] high = new ModelFile[5];
		for (int i = 1; i <= 5; i++) {
			var sub = pvd.models().getBuilder("block/" + id + "_stack" + i)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/basket/" + model + "_stack" + i)));
			sub.texture("placement", pvd.modLoc("block/basket/" + id + "_placement"));
			if (extra) {
				sub.texture("placement_2", pvd.modLoc("block/basket/" + id + "_placement_2"));
			}
			low[i - 1] = sub.renderType("cutout");

			var top = new BlockModelBuilder(null, null)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_stack" + i)));
			top.rootTransforms().translation(0, 5f / 16, 0).end();
			high[i - 1] = pvd.models().getBuilder("block/" + id + "_stack" + i + "_upper")
					.customLoader(CompositeModelBuilder::begin)
					.child("layer", new BlockModelBuilder(null, null)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_stack6"))))
					.child("top", top)
					.end()
					.texture("particle", pvd.modLoc("block/basket/basket_top"))
					.guiLight(BlockModel.GuiLight.SIDE);

		}
		for (int d = 0; d < 4; d++) {
			var dir = Direction.from2DDataValue(d);
			int rot = ((int) dir.toYRot() + 180) % 360;
			builder.part().modelFile(basket).rotationY(rot).addModel()
					.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
			for (int i = 1; i <= MAX; i++) {
				var part = i <= 5 ? low[i - 1] : i == 6 ? layer : high[i - 7];
				builder.part().modelFile(part).rotationY(rot).addModel().condition(COUNT, i)
						.condition(BlockProxy.HORIZONTAL_FACING, dir).end();
			}
		}
		pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.mcLoc("block/block")))
				.customLoader(CompositeModelBuilder::begin)
				.child("basket", new BlockModelBuilder(null, null)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/short_basket"))))
				.child("top", new BlockModelBuilder(null, null)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_stack5_upper"))))
				.end()
				.texture("particle", pvd.modLoc("block/basket/basket_top"))
				.guiLight(BlockModel.GuiLight.SIDE);

	}

	public static void loot(RegistrateBlockLootTables pvd, DelegateBlock b, ItemLike item) {
		var loot = LootTable.lootTable();
		loot.withPool(LootPool.lootPool().add(LootItem.lootTableItem(b))
				.when(LootTableTemplate.withBlockState(b, COUNT, MAX)));
		loot.withPool(LootPool.lootPool().add(LootItem.lootTableItem(BASKET))
				.when(LootTableTemplate.withBlockState(b, COUNT, MAX).invert()));
		for (int i = 1; i < MAX; i++)
			loot.withPool(LootPool.lootPool().add(LootItem.lootTableItem(item))
					.when(LootTableTemplate.withBlockState(b, COUNT, i)));
		pvd.add(b, loot);
	}

	public static void register() {
	}

}
