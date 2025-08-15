package dev.xkmc.youkaishomecoming.content.pot.table.board;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.pot.table.item.SearHelper;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
import vectorwing.farmersdelight.common.registry.ModSounds;

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
			if (player.isShiftKeyDown()) {
				be.clear();
				player.playSound(SoundEvents.COMPOSTER_FILL, 1, 1);
				return InteractionResult.SUCCESS;
			}
			if (be.performToolAction(stack)) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (stack.isDamageableItem()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
					}
				}
				player.playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
				return InteractionResult.SUCCESS;
			}
			int cost = be.addItem(stack);
			if (cost > 0) {
				if (!level.isClientSide && !player.getAbilities().instabuild) {
					if (SearHelper.isFireSource(stack)) {
						if (stack.isDamageableItem()) {
							stack.hurtAndBreak(cost, player, p -> p.broadcastBreakEvent(hand));
							return InteractionResult.SUCCESS;
						}
					}
					if (SlipBottleItem.isSlipContainer(stack)) {
						var toConsume = stack.split(1);
						player.setItemInHand(hand, SlipBottleItem.drain(toConsume));
						player.getInventory().placeItemBackInInventory(stack);
						return InteractionResult.SUCCESS;
					}
					boolean withCont = stack.is(YHTagGen.PLACE_WITH_CONTAINER);
					ItemStack cont = stack.getCraftingRemainingItem();
					stack.shrink(cost);
					if (!withCont && !cont.isEmpty()) {
						player.getInventory().placeItemBackInInventory(cont.copyWithCount(cost));
					}
					if (player instanceof ServerPlayer sp && be.getModel().complete(level).isPresent()) {
						YHCriteriaTriggers.TABLE.trigger(sp);
					}
				}
				player.playSound(SoundEvents.WOOL_PLACE, 1, 1);
				return InteractionResult.SUCCESS;
			}
			if (stack.isEmpty() && be.addToPlayer(player)) {
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
