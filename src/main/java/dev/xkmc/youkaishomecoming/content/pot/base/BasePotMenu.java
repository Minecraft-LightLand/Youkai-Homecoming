package dev.xkmc.youkaishomecoming.content.pot.base;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMealSlot;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;

public abstract class BasePotMenu<T extends BasePotRecipe> extends RecipeBookMenu<RecipeWrapper, T> {
	public static final ResourceLocation EMPTY_CONTAINER_SLOT = YoukaisHomecoming.loc("item/empty_container_slot_bottle");
	public final BasePotBlockEntity<T> blockEntity;
	public final ItemStackHandler inventory;
	protected final ContainerData cookingPotData;
	private final ContainerLevelAccess canInteractWithCallable;
	protected final Level level;

	public BasePotMenu(MenuType<? extends BasePotMenu> menu, int id, Inventory inv, @Nullable FriendlyByteBuf data) {
		this(menu, id, inv, getTileEntity(inv, data), new SimpleContainerData(4));
	}

	public BasePotMenu(MenuType<? extends BasePotMenu> menu, int id, Inventory inv, BasePotBlockEntity<T> be, ContainerData data) {
		super(menu, id);
		blockEntity = be;
		inventory = be.getInventory();
		cookingPotData = data;
		level = inv.player.level();
		assert be.getLevel() != null;
		canInteractWithCallable = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
		int startX = 8;
		int startY = 18;
		int inputStartX = 35;
		int inputStartY = 18;
		int borderSlotSize = 18;

		int startPlayerInvY;
		{
			int column;
			for (startPlayerInvY = 0; startPlayerInvY < 2; ++startPlayerInvY) {
				for (column = 0; column < 2; ++column) {
					addSlot(new SlotItemHandler(inventory, startPlayerInvY * 2 + column, inputStartX + column * borderSlotSize, inputStartY + startPlayerInvY * borderSlotSize));
				}
			}
		}

		addSlot(new CookingPotMealSlot(inventory, BasePotBlockEntity.MEAL_DISPLAY_SLOT, 121, 27));
		addSlot(new SlotItemHandler(inventory, BasePotBlockEntity.CONTAINER_SLOT, 89, 54) {
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_CONTAINER_SLOT);
			}
		});
		addSlot(new BasePotResultSlot(inv.player, be, inventory, BasePotBlockEntity.OUTPUT_SLOT, 121, 54));
		startPlayerInvY = startY * 4 + 12;

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				addSlot(new Slot(inv, 9 + row * 9 + column, startX + column * borderSlotSize, startPlayerInvY + row * borderSlotSize));
			}
		}

		for (int column = 0; column < 9; ++column) {
			addSlot(new Slot(inv, column, startX + column * borderSlotSize, 142));
		}

		addDataSlots(data);
	}

	private static <T extends BasePotRecipe> BasePotBlockEntity<T> getTileEntity(Inventory playerInventory, @Nullable FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
		if (tileAtPos instanceof BasePotBlockEntity<?> pot) {
			return Wrappers.cast(pot);
		} else {
			throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
		}
	}

	public abstract Block getBlock();

	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWithCallable, playerIn, getBlock());
	}

	public ItemStack quickMoveStack(Player playerIn, int index) {
		int indexMealDisplay = BasePotBlockEntity.MEAL_DISPLAY_SLOT;
		int indexContainerInput = BasePotBlockEntity.CONTAINER_SLOT;
		int indexOutput = BasePotBlockEntity.OUTPUT_SLOT;
		int startPlayerInv = indexOutput + 1;
		int endPlayerInv = startPlayerInv + 36;
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			slotStackCopy = slotStack.copy();
			if (index == indexOutput) {
				if (!moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index <= indexOutput) {
				if (!moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				boolean isValidContainer = slotStack.is(ModTags.SERVING_CONTAINERS) || slotStack.is(blockEntity.getContainer().getItem());
				if (isValidContainer && !moveItemStackTo(slotStack, indexContainerInput, indexContainerInput + 1, false)) {
					return ItemStack.EMPTY;
				}

				if (!moveItemStackTo(slotStack, 0, indexMealDisplay, false)) {
					return ItemStack.EMPTY;
				}

				if (!moveItemStackTo(slotStack, indexContainerInput, indexOutput, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotStack.getCount() == slotStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, slotStack);
		}

		return slotStackCopy;
	}

	public double getCookProgressionScaled() {
		int i = cookingPotData.get(0);
		int j = cookingPotData.get(1);
		return j != 0 && i != 0 ? i * 1.0 / j : 0;
	}

	public boolean isHeated() {
		return blockEntity.isHeated();
	}

	public void fillCraftSlotsStackedContents(StackedContents helper) {
		for (int i = 0; i < inventory.getSlots(); ++i) {
			helper.accountSimpleStack(inventory.getStackInSlot(i));
		}

	}

	public void clearCraftingContent() {
		for (int i = 0; i < BasePotBlockEntity.MEAL_DISPLAY_SLOT; ++i) {
			inventory.setStackInSlot(i, ItemStack.EMPTY);
		}

	}

	@Override
	public boolean recipeMatches(RecipeHolder<T> recipeHolder) {
		return recipeHolder.value().matches(new RecipeWrapper(inventory), level);
	}

	public int getResultSlotIndex() {
		return BasePotBlockEntity.CONTAINER_SLOT;
	}

	public int getGridWidth() {
		return 2;
	}

	public int getGridHeight() {
		return 2;
	}

	public int getSize() {
		return BasePotBlockEntity.CONTAINER_SLOT;
	}

	public abstract RecipeBookType getRecipeBookType();

	public boolean shouldMoveToInventory(int slot) {
		return slot < getGridWidth() * getGridHeight();
	}

}
