package dev.xkmc.youkaishomecoming.content.pot.storage.bottle;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.util.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SauceRackBlock implements ShapeBlockMethod, OnClickBlockMethod {

	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.SAUCE_RACK_BE, SauceRackBlockEntity.class);

	public static final VoxelShape[] SHAPES;

	static {
		SHAPES = new VoxelShape[4];

		for (int i = 0; i < 4; i++) {
			var dir = Direction.from2DDataValue(i);
			var bottom = new VoxelBuilder(0, 0, 10, 16, 1, 15).rotateFromNorth(dir);
			var flat = new VoxelBuilder(1, 11, 12, 15, 12, 15).rotateFromNorth(dir);
			var low = new VoxelBuilder(0, 0, 15, 16, 16, 16).rotateFromNorth(dir);
			SHAPES[i] = Shapes.or(bottom, flat, low);

		}

	}


	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int dir = state.getValue(BlockProxy.HORIZONTAL_FACING).get2DDataValue();
		return SHAPES[ dir];
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		var dir = state.getValue(BlockProxy.HORIZONTAL_FACING);
		if (level.getBlockEntity(pos) instanceof SauceRackBlockEntity be) {
			var vec = hit.getLocation().subtract(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
					.yRot(-(float) ((180 - dir.toYRot()) / 180 * Math.PI));
			int x = Mth.clamp((int) ((-vec.x + 0.5) * 3), 0, 2);
			if (be.click(player, hand, x)) {
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	public static void buildModels(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/sauce_rack")))
				.texture("back", pvd.modLoc("block/storage/sauce_rack/" + ctx.getName() + "_back"))
				.texture("top", pvd.modLoc("block/storage/sauce_rack/" + ctx.getName() + "_top"))
				.texture("parts", pvd.modLoc("block/storage/sauce_rack/" + ctx.getName() + "_parts"))
				.renderType("cutout"));
	}

}
