package dev.xkmc.youkaishomecoming.content.pot.base;

import dev.xkmc.l2library.base.tile.BaseTank;
import dev.xkmc.youkaishomecoming.compat.create.CreateFillingTest;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public interface FluidItemTile {

	static InteractionResult addItem(FluidItemTile be, ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		FluidStack fluid = be.getFluidHandler().getFluidInTank(0);
		boolean hasFluid = false;
		if (fluid.getFluid() instanceof YHFluid sake) {
			if (fluid.getAmount() >= sake.type.amount() && stack.is(sake.type.getContainer())) {
				if (!level.isClientSide()) {
					be.getFluidHandler().drain(sake.type.amount(), IFluidHandler.FluidAction.EXECUTE);
					player.getInventory().placeItemBackInInventory(sake.type.asStack(1));
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
				return InteractionResult.SUCCESS;
			}
			hasFluid = true;
		}
		var fillOpt = CreateFillingTest.test(level, fluid, stack);
		if (fillOpt.isPresent()) {
			if (!level.isClientSide()) {
				ItemStack ans = fillOpt.get().result().get();
				player.getInventory().placeItemBackInInventory(ans);
				be.notifyTile();
			}
			return InteractionResult.SUCCESS;
		}
		if (!hasFluid || stack.getItem() instanceof SlipBottleItem || stack.getItem() instanceof SakeBottleItem) {
			LazyOptional<IFluidHandlerItem> opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
			if (opt.resolve().isPresent()) {
				if (!level.isClientSide() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection())) {
					be.notifyTile();
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.CONSUME;
				}
			}
		}
		ItemStack copy = stack.copy();
		copy.setCount(1);
		if (be.getItemHandler().canAddItem(copy)) {
			ItemStack remain = be.getItemHandler().addItem(copy);
			if (remain.isEmpty()) {
				stack.shrink(1);
				be.notifyTile();
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.CONSUME;
	}

	BaseTank getFluidHandler();

	SimpleContainer getItemHandler();

	void notifyTile();

}
