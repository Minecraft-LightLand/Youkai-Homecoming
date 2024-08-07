package dev.xkmc.youkaishomecoming.content.pot.base;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.client.gui.CookingPotRecipeBookComponent;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePotScreen<T extends BasePotMenu<R>, R extends BasePotRecipe> extends AbstractContainerScreen<T> implements RecipeUpdateListener {
	private static final WidgetSprites RECIPE_BUTTON = new WidgetSprites(ResourceLocation.withDefaultNamespace("recipe_book/button"), ResourceLocation.withDefaultNamespace("recipe_book/button"));
	private final CookingPotRecipeBookComponent book = new CookingPotRecipeBookComponent();
	private boolean widthTooNarrow;

	public BasePotScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	public abstract Rectangle getHeatIcon();

	public abstract Rectangle getProgressArrow();

	public abstract ResourceLocation getBackgroundTexture();

	public void init() {
		super.init();
		widthTooNarrow = width < 379;
		titleLabelX = 28;
		book.init(width, height, minecraft, widthTooNarrow, menu);
		leftPos = book.updateScreenPosition(width, imageWidth);
		if (Configuration.ENABLE_RECIPE_BOOK_COOKING_POT.get()) {
			addRenderableWidget(new ImageButton(leftPos + 5, height / 2 - 49, 20, 18, RECIPE_BUTTON, (button) -> {
				book.toggleVisibility();
				leftPos = book.updateScreenPosition(width, imageWidth);
				button.setPosition(leftPos + 5, height / 2 - 49);
			}));
		} else {
			book.hide();
			leftPos = book.updateScreenPosition(width, imageWidth);
		}

		addWidget(book);
		setInitialFocus(book);
	}

	protected void containerTick() {
		super.containerTick();
		book.tick();
	}

	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (book.isVisible() && widthTooNarrow) {
			renderBackground(gui, mouseX, mouseY, partialTicks);
			book.render(gui, mouseX, mouseY, partialTicks);
		} else {
			super.render(gui, mouseX, mouseY, partialTicks);
			book.render(gui, mouseX, mouseY, partialTicks);
			book.renderGhostRecipe(gui, leftPos, topPos, false, partialTicks);
		}

		renderMealDisplayTooltip(gui, mouseX, mouseY);
		renderHeatIndicatorTooltip(gui, mouseX, mouseY);
		book.renderTooltip(gui, leftPos, topPos, mouseX, mouseY);
	}

	private void renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (isHovering(getHeatIcon().x, getHeatIcon().y, getHeatIcon().width, getHeatIcon().height, mouseX, mouseY)) {
			String key = "container.cooking_pot." + (menu.isHeated() ? "heated" : "not_heated");
			gui.renderTooltip(font, TextUtils.getTranslation(key, menu), mouseX, mouseY);
		}

	}

	protected void renderMealDisplayTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (minecraft != null && minecraft.player != null && menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem()) {
			if (hoveredSlot.index == BasePotBlockEntity.MEAL_DISPLAY_SLOT) {
				List<Component> tooltip = new ArrayList<>();
				ItemStack mealStack = hoveredSlot.getItem();
				tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().getStyleModifier()));
				ItemStack containerStack = menu.blockEntity.getContainer();
				String container = !containerStack.isEmpty() ? containerStack.getItem().getDescription().getString() : "";
				tooltip.add(TextUtils.getTranslation("container.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
				gui.renderComponentTooltip(font, tooltip, mouseX, mouseY);
			} else {
				gui.renderTooltip(font, hoveredSlot.getItem(), mouseX, mouseY);
			}
		}

	}

	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		super.renderLabels(gui, mouseX, mouseY);
		gui.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 4210752, false);
	}

	protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (minecraft != null) {
			gui.blit(getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
			if (menu.isHeated()) {
				gui.blit(getBackgroundTexture(), leftPos + getHeatIcon().x, topPos + getHeatIcon().y, 176, 0, getHeatIcon().width, getHeatIcon().height);
			}

			int l = (int) (menu.getCookProgressionScaled() * getProgressArrow().width);
			gui.blit(getBackgroundTexture(), leftPos + getProgressArrow().x, topPos + getProgressArrow().y, 176, getHeatIcon().height, l + 1, getProgressArrow().height);
		}
	}

	protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
		return (!widthTooNarrow || !book.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
		if (book.mouseClicked(mouseX, mouseY, buttonId)) {
			setFocused(book);
			return true;
		} else {
			return widthTooNarrow && book.isVisible() || super.mouseClicked(mouseX, mouseY, buttonId);
		}
	}

	protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonIdx) {
		boolean flag = mouseX < x || mouseY < y || mouseX >= x + imageWidth || mouseY >= y + imageHeight;
		return flag && book.hasClickedOutside(mouseX, mouseY, leftPos, topPos, imageWidth, imageHeight, buttonIdx);
	}

	protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
		super.slotClicked(slot, mouseX, mouseY, clickType);
		book.slotClicked(slot);
	}

	public void recipesUpdated() {
		book.recipesUpdated();
	}

	@Nonnull
	public RecipeBookComponent getRecipeBookComponent() {
		return book;
	}

}
