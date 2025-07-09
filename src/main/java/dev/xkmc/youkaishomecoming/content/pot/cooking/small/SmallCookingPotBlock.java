package dev.xkmc.youkaishomecoming.content.pot.cooking.small;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class SmallCookingPotBlock implements OnClickBlockMethod, ShapeBlockMethod {

	public static final BlockMethod INS = new SmallCookingPotBlock();
	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.SMALL_POT_BE, SmallCookingPotBlockEntity.class);
	public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 4, 12);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, INS, BE, BlockProxy.HORIZONTAL);
	}

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof SmallCookingPotBlockEntity be) {
			if (player.isShiftKeyDown()) {
				be.dumpInventory();
				player.playSound(SoundEvents.ITEM_PICKUP, 1, 1);
				var bowl = YHItems.IRON_BOWL.getDefaultState().setValue(BlockProxy.HORIZONTAL_FACING,
						state.getValue(BlockProxy.HORIZONTAL_FACING));
				level.setBlockAndUpdate(pos, bowl);
				return InteractionResult.SUCCESS;
			}
			if (be.tryAddItem(stack)) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
					} else {
						ItemStack cont = stack.getCraftingRemainingItem();
						stack.shrink(1);
						if (!cont.isEmpty()) {
							player.getInventory().placeItemBackInInventory(cont.copyWithCount(1));
						}
					}
				}
				player.playSound(SoundEvents.WOOL_PLACE, 0.8F, 1.0F);
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
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/iron_bowl"))));
	}

}
