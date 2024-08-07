
package dev.xkmc.youkaishomecoming.util;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.text.NumberFormat;
import java.util.Optional;

public class JEIFluidStackRenderer {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final NumberFormat nf = NumberFormat.getIntegerInstance();
	private static final int TEXTURE_SIZE = 16;

	private final long capacity;
	private final int width;
	private final int height;

	public JEIFluidStackRenderer() {
		this(1000, TEXTURE_SIZE, TEXTURE_SIZE);
	}

	private JEIFluidStackRenderer(long capacity, int width, int height) {
		Preconditions.checkArgument(capacity > 0, "capacity must be > 0");
		Preconditions.checkArgument(width > 0, "width must be > 0");
		Preconditions.checkArgument(height > 0, "height must be > 0");
		this.capacity = capacity;
		this.width = width;
		this.height = height;
	}

	public void render(GuiGraphics guiGraphics, FluidStack fluidStack) {
		RenderSystem.enableBlend();
		drawFluid(guiGraphics, width, height, fluidStack);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.disableBlend();
	}

	private static Optional<TextureAtlasSprite> getStillFluidSprite(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);

		TextureAtlasSprite sprite = Minecraft.getInstance()
				.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(fluidStill);
		return Optional.of(sprite)
				.filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
	}

	private static int getColorTint(FluidStack ingredient) {
		Fluid fluid = ingredient.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		return renderProperties.getTintColor(ingredient);
	}

	private void drawFluid(GuiGraphics guiGraphics, final int width, final int height, FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		if (fluid.isSame(Fluids.EMPTY)) {
			return;
		}
		var sprite = getStillFluidSprite(fluidStack);
		if (sprite.isEmpty()) return;
		int fluidColor = getColorTint(fluidStack);
		long amount = fluidStack.getAmount();
		long scaledAmount = (amount * height) / capacity;
		if (amount > 0 && scaledAmount < 1) {
			scaledAmount = 1;
		}
		if (scaledAmount > height) {
			scaledAmount = height;
		}
		drawTiledSprite(guiGraphics, width, height, fluidColor, scaledAmount, sprite.get());

	}

	private static void drawTiledSprite(GuiGraphics guiGraphics, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		Matrix4f matrix = guiGraphics.pose().last().pose();
		setGLColorFromInt(color);

		final int xTileCount = tiledWidth / TEXTURE_SIZE;
		final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
		final long yTileCount = scaledAmount / TEXTURE_SIZE;
		final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

		final int yStart = tiledHeight;

		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
				long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
				int x = (xTile * TEXTURE_SIZE);
				int y = yStart - ((yTile + 1) * TEXTURE_SIZE);
				if (width > 0 && height > 0) {
					long maskTop = TEXTURE_SIZE - height;
					int maskRight = TEXTURE_SIZE - width;

					drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
				}
			}
		}
	}

	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		float alpha = ((color >> 24) & 0xFF) / 255F;

		RenderSystem.setShaderColor(red, green, blue, alpha);
	}

	private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
		float uMin = textureSprite.getU0();
		float uMax = textureSprite.getU1();
		float vMin = textureSprite.getV0();
		float vMax = textureSprite.getV1();
		uMax = uMax - (maskRight / 16F * (uMax - uMin));
		vMax = vMax - (maskTop / 16F * (vMax - vMin));

		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder wr = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		wr.addVertex(matrix, xCoord, yCoord + 16, zLevel).setUv(uMin, vMax);
		wr.addVertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).setUv(uMax, vMax);
		wr.addVertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).setUv(uMax, vMin);
		wr.addVertex(matrix, xCoord, yCoord + maskTop, zLevel).setUv(uMin, vMin);
		BufferUploader.drawWithShader(wr.buildOrThrow());
	}

}
