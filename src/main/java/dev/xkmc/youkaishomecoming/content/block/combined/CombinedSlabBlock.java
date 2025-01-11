package dev.xkmc.youkaishomecoming.content.block.combined;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

public class CombinedSlabBlock extends Block {

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

	public CombinedSlabBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	private static void cube(ModelBuilder<?> builder) {
		builder.element()
				.from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#t1").cullface(Direction.DOWN).end()
				.face(Direction.NORTH).uvs(0, 8, 16, 16).texture("#s1").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(0, 8, 16, 16).texture("#s1").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 8, 16, 16).texture("#s1").cullface(Direction.WEST).end()
				.face(Direction.EAST).uvs(0, 8, 16, 16).texture("#s1").cullface(Direction.EAST).end()
				.end()
				.element()
				.from(0, 8, 0).to(16, 16, 16)
				.face(Direction.UP).uvs(0, 0, 16, 16).texture("#t2").cullface(Direction.UP).end()
				.face(Direction.NORTH).uvs(0, 0, 16, 8).texture("#s2").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(0, 0, 16, 8).texture("#s2").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 0, 16, 8).texture("#s2").cullface(Direction.WEST).end()
				.face(Direction.EAST).uvs(0, 0, 16, 8).texture("#s2").cullface(Direction.EAST).end()
				.end()
				.texture("t1", "#top_a")
				.texture("s1", "#side_a")
				.texture("t2", "#top_b")
				.texture("s2", "#side_b");
	}

	private static BlockModelBuilder base = null;

	public static BlockModelBuilder buildModel(DataGenContext<Block, ? extends CombinedSlabBlock> ctx, RegistrateBlockstateProvider pvd) {
		if (base == null) {
			base = pvd.models().withExistingParent("combined_slabs", "block/block");
			cube(base);
			base.texture("particle", "#top_a");
		}
		return pvd.models().getBuilder("block/" + ctx.getName()).parent(base);
	}

	public static void buildStates(DataGenContext<Block, ? extends CombinedSlabBlock> ctx, RegistrateBlockstateProvider pvd, IBlockSet a, IBlockSet b) {
		var model = buildModel(ctx, pvd)
				.texture("top_a", a.top())
				.texture("side_a", a.side())
				.texture("top_b", b.top())
				.texture("side_b", b.side());
		pvd.getVariantBuilder(ctx.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(model)
							.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.uvLock(true)
							.build();
				});
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, CombinedSlabBlock block, IBlockSet a, IBlockSet b) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(AlternativesEntry.alternatives(
				EntryGroup.list(
						helper.item(a.slab().value().asItem()),
						helper.item(b.slab().value().asItem())
				).when(AnyOfCondition.anyOf(
						helper.enumState(block, FACING, Direction.UP),
						helper.enumState(block, FACING, Direction.DOWN)
				)),
				EntryGroup.list(
						helper.item(a.vertical().value().asItem()),
						helper.item(b.vertical().value().asItem())
				)
		))));
	}

}
