package dev.xkmc.youkaishomecoming.content.block.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class SurpriseFeastBlock extends FeastBlock {

	protected static final VoxelShape SHAPE = box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);

	public SurpriseFeastBlock(Properties properties, Supplier<Item> servingItem) {
		super(properties, servingItem, true);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	public static void buildModel(DataGenContext<Block, SurpriseFeastBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> {
			int serve = state.getValue(SERVINGS);
			String suffix = serve == 0 ? "_leftover" : "_stage" + (4 - serve);
			String id = ctx.getName() + suffix;
			var ans = pvd.models().getBuilder("block/" + id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/feast/" + id)));
			ans.texture("base", pvd.modLoc("block/surprise_chest"));
			ans.texture("umbrella", pvd.modLoc("block/surprise_umbrella"));
			if (serve > 0) {
				ans.texture("soup", pvd.modLoc("block/surprise_soup"));
			}
			ans.renderType("cutout");
			return ans;
		});
	}

	public static void builtLoot(RegistrateBlockLootTables pvd, SurpriseFeastBlock block) {
		pvd.add(block, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.CHEST))
						.when(ExplosionCondition.survivesExplosion())));
	}

}
