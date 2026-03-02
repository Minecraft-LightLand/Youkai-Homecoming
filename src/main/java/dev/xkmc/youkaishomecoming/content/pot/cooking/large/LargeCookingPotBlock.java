package dev.xkmc.youkaishomecoming.content.pot.cooking.large;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.block.food.PotFoodBlock;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotClick;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotFall;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class LargeCookingPotBlock implements ShapeBlockMethod {

	public static final BlockMethod INS = new LargeCookingPotBlock();
	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.LARGE_POT_BE, LargeCookingPotBlockEntity.class);
	public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 15, 15);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, INS, new PotClick(YHBlocks.STOCKPOT), new PotFall(), BE, BlockTemplates.HORIZONTAL);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildState(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/stock/stockpot")))
				.texture("top", pvd.modLoc("block/bowl/stock/stockpot_top"))
				.texture("side", pvd.modLoc("block/bowl/stock/stockpot_side"))
				.texture("inside", pvd.modLoc("block/bowl/stock/stockpot_inside"))
				.texture("bottom", pvd.modLoc("block/bowl/stock/stockpot_bottom"))
				.texture("parts", pvd.modLoc("block/bowl/stock/stockpot_parts"))
				.renderType("cutout")
		);
	}

	public static void buildPotFood(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd, String tex) {
		pvd.horizontalBlock(ctx.get(), state -> {
					int stage = state.getValue(PotFoodBlock.SERVE_4);
					String suffix = stage == 4 ? "" : ("_serve" + stage);
					return pvd.models().getBuilder(ctx.getName() + suffix)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/stock/stock_serve" + stage)))
							.texture("top", pvd.modLoc("block/bowl/stock/stockpot_top"))
							.texture("side", pvd.modLoc("block/bowl/stock/stockpot_side"))
							.texture("inside", pvd.modLoc("block/bowl/stock/stockpot_inside"))
							.texture("bottom", pvd.modLoc("block/bowl/stock/stockpot_bottom"))
							.texture("parts", pvd.modLoc("block/bowl/stock/stockpot_parts"))
							.texture("base", pvd.modLoc("block/bowl/stock/" + tex))
							.renderType("cutout");
				}
		);
	}

	public static void createLoot(RegistrateBlockLootTables pvd, Block block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(YHBlocks.STOCKPOT.asItem()))
						.when(helper.silk().invert()))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.CAULDRON))
						.when(helper.silk()))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(YHBlocks.BIG_SPOON))
						.when(helper.silk()))
		);
	}

}
