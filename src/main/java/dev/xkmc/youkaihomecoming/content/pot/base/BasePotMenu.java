package dev.xkmc.youkaihomecoming.content.pot.base;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMealSlot;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;

public abstract class BasePotMenu extends RecipeBookMenu<RecipeWrapper> {
	public static final ResourceLocation EMPTY_CONTAINER_SLOT = new ResourceLocation(YoukaiHomecoming.MODID, "gui/empty_container_slot_bottle");
	public final BasePotBlockEntity blockEntity;
	public final ItemStackHandler inventory;
	private final ContainerData cookingPotData;
	private final ContainerLevelAccess canInteractWithCallable;
	protected final Level level;

	public BasePotMenu(MenuType<? extends BasePotMenu> menu, int id, Inventory inv, @Nullable FriendlyByteBuf data) {
		this(menu, id, inv, getTileEntity(inv, data), new SimpleContainerData(4));
	}

	public BasePotMenu(MenuType<? extends BasePotMenu> menu, int id, Inventory inv, BasePotBlockEntity be, ContainerData data) {
		super(menu, id);
		this.blockEntity = be;
		this.inventory = be.getInventory();
		this.cookingPotData = data;
		this.level = inv.player.level();
		this.canInteractWithCallable = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
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
					this.addSlot(new SlotItemHandler(this.inventory, startPlayerInvY * 2 + column, inputStartX + column * borderSlotSize, inputStartY + startPlayerInvY * borderSlotSize));
				}
			}
		}

		this.addSlot(new CookingPotMealSlot(this.inventory, BasePotBlockEntity.MEAL_DISPLAY_SLOT, 121, 27));
		this.addSlot(new SlotItemHandler(this.inventory, BasePotBlockEntity.CONTAINER_SLOT, 89, 54) {
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_CONTAINER_SLOT);
			}
		});
		this.addSlot(new BasePotResultSlot(inv.player, be, this.inventory, BasePotBlockEntity.OUTPUT_SLOT, 121, 54));
		startPlayerInvY = startY * 4 + 12;

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(inv, 9 + row * 9 + column, startX + column * borderSlotSize, startPlayerInvY + row * borderSlotSize));
			}
		}

		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(inv, column, startX + column * borderSlotSize, 142));
		}

		this.addDataSlots(data);
	}

	private static BasePotBlockEntity getTileEntity(Inventory playerInventory, @Nullable FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
		if (tileAtPos instanceof BasePotBlockEntity pot) {
			return pot;
		} else {
			throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
		}
	}

	public abstract Block getBlock();

	public boolean stillValid(Player playerIn) {
		return stillValid(this.canInteractWithCallable, playerIn, getBlock());
	}

	public ItemStack quickMoveStack(Player playerIn, int index) {
		int indexMealDisplay = BasePotBlockEntity.MEAL_DISPLAY_SLOT;
		int indexContainerInput = BasePotBlockEntity.CONTAINER_SLOT;
		int indexOutput = BasePotBlockEntity.OUTPUT_SLOT;
		int startPlayerInv = indexOutput + 1;
		int endPlayerInv = startPlayerInv + 36;
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			slotStackCopy = slotStack.copy();
			if (index == indexOutput) {
				if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index <= indexOutput) {
				if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				boolean isValidContainer = slotStack.is(ModTags.SERVING_CONTAINERS) || slotStack.is(this.blockEntity.getContainer().getItem());
				if (isValidContainer && !this.moveItemStackTo(slotStack, indexContainerInput, indexContainerInput + 1, false)) {
					return ItemStack.EMPTY;
				}

				if (!this.moveItemStackTo(slotStack, 0, indexMealDisplay, false)) {
					return ItemStack.EMPTY;
				}

				if (!this.moveItemStackTo(slotStack, indexContainerInput, indexOutput, false)) {
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
		int i = this.cookingPotData.get(0);
		int j = this.cookingPotData.get(1);
		return j != 0 && i != 0 ? i * 1.0 / j : 0;
	}

	public boolean isHeated() {
		return this.blockEntity.isHeated();
	}

	public void fillCraftSlotsStackedContents(StackedContents helper) {
		for (int i = 0; i < this.inventory.getSlots(); ++i) {
			helper.accountSimpleStack(this.inventory.getStackInSlot(i));
		}

	}

	public void clearCraftingContent() {
		for (int i = 0; i < BasePotBlockEntity.MEAL_DISPLAY_SLOT; ++i) {
			this.inventory.setStackInSlot(i, ItemStack.EMPTY);
		}

	}

	public boolean recipeMatches(Recipe<? super RecipeWrapper> recipe) {
		return recipe.matches(new RecipeWrapper(this.inventory), this.level);
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
		return slot < this.getGridWidth() * this.getGridHeight();
	}

}
