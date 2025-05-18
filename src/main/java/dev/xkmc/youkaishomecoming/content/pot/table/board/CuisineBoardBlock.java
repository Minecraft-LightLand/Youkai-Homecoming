package dev.xkmc.youkaishomecoming.content.pot.table.board;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class CuisineBoardBlock implements OnClickBlockMethod, ShapeBlockMethod {

	public static final BlockMethod INS = new CuisineBoardBlock();
	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.CUISINE_BOARD_BE, CuisineBoardBlockEntity.class);
	public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 1, 15);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p.mapColor(MapColor.WOOD).sound(SoundType.WOOD)
						.strength(1).noOcclusion().pushReaction(PushReaction.DESTROY),
				INS, BE, BlockProxy.HORIZONTAL);

	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof CuisineBoardBlockEntity be) {
			if (be.addItem(stack)) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					ItemStack cont = stack.getCraftingRemainingItem();
					stack.shrink(1);
					if (!cont.isEmpty()) {
						player.getInventory().placeItemBackInInventory(cont);
					}
				}
				return InteractionResult.SUCCESS;
			} else if (stack.isEmpty() && be.addToPlayer(player)) {
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildState(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/" + ctx.getName())))
				.texture("all", pvd.modLoc("block/utensil/" + ctx.getName()))
		);
	}

}
