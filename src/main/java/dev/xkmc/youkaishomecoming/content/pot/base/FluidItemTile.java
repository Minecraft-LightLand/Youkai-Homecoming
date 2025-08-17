package dev.xkmc.youkaishomecoming.content.pot.base;

import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.youkaishomecoming.compat.create.CreateFillingTest;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public interface FluidItemTile {

	static InteractionResult addFluidOrItem(FluidItemTile be, ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		FluidStack fluid = be.getFluidHandler().getFluidInTank(0);
		boolean hasFluid = false;
		// take YH fluids
		if (fluid.getFluid() instanceof YHFluid sake) {
			if (fluid.getAmount() >= sake.type.amount() && stack.is(sake.type.getContainer())) {
				if (level instanceof ServerLevel sl) {
					be.getFluidHandler().drain(sake.type.amount(), IFluidHandler.FluidAction.EXECUTE);
					player.getInventory().placeItemBackInInventory(sake.type.asStack(1));
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					sl.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.7f, 1);
				}
				return InteractionResult.SUCCESS;
			}
			hasFluid = true;
		}
		// take fluid in create sense
		var fillOpt = CreateFillingTest.test(level, fluid, stack);
		if (fillOpt.isPresent()) {
			if (level instanceof ServerLevel sl) {
				ItemStack ans = fillOpt.get().result().get();
				player.getInventory().placeItemBackInInventory(ans);
				sl.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.7f, 1);
				be.notifyTile();
			}
			return InteractionResult.SUCCESS;
		}
		// fill or take fluid via YH items
		if (!hasFluid || stack.getItem() instanceof SlipBottleItem || stack.getItem() instanceof SakeBottleItem) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
			if (opt.resolve().isPresent()) {
				if ((level instanceof ServerLevel sl) && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection())) {
					be.notifyTile();
					sl.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.7f, 1);
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.CONSUME;
				}
			}
		}
		// fill water from bottle
		if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) {
			var attempt = be.getFluidHandler().fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.SIMULATE);
			if (attempt == 250) {
				if (level instanceof ServerLevel sl) {
					be.getFluidHandler().fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
					if (!player.isCreative()) {
						stack.shrink(1);
						player.getInventory().placeItemBackInInventory(Items.GLASS_BOTTLE.getDefaultInstance());
					}
					sl.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.7f, 1);
				}
			}
		}
		return addItem(be, stack, level, pos);
	}

	static InteractionResult addItem(FluidItemTile be, ItemStack stack, Level level, BlockPos pos) {
		ItemStack copy = stack.copyWithCount(1);
		if (be.getItemHandler().canAddItem(copy)) {
			if (level instanceof ServerLevel sl) {
				ItemStack remain = be.getItemHandler().addItem(copy);
				if (remain.isEmpty()) {
					stack.shrink(1);
					be.notifyTile();
					sl.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.7f, 1);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.CONSUME;
	}

	BaseTank getFluidHandler();

	SimpleContainer getItemHandler();

	void notifyTile();

}
