package dev.xkmc.youkaishomecoming.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public class FluidRenderer {
	public FluidRenderer() {
	}

	public static VertexConsumer getFluidBuilder(MultiBufferSource buffer) {
		return buffer.getBuffer(FluidRenderer.RenderTypes.FLUID);
	}

	public static void renderWaterBox(float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, MultiBufferSource buffer, PoseStack ms, int light, int colorOverride) {
		renderFluidBox(new FluidStack(Fluids.WATER, 1000), xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false, colorOverride);
	}

	public static void renderFluidBox(FluidStack fluidStack, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom, int colorOverride) {
		renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, getFluidBuilder(buffer), ms, light, renderBottom, colorOverride);
	}

	public static void renderFluidBox(ResourceLocation tex, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom, int colorOverride) {
		renderFluidBox(tex, xMin, yMin, zMin, xMax, yMax, zMax, getFluidBuilder(buffer), ms, light, renderBottom, colorOverride);
	}

	public static void renderFluidBox(FluidStack fluidStack, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, VertexConsumer builder, PoseStack ms, int light, boolean renderBottom, int colorOverride) {
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
		FluidType fluidAttributes = fluid.getFluidType();
		var tex = clientFluid.getStillTexture(fluidStack);
		int color = clientFluid.getTintColor(fluidStack);
		if (colorOverride != 0) {
			color = colorOverride;
		}
		int blockLightIn = light >> 4 & 15;
		int luminosity = Math.max(blockLightIn, fluidAttributes.getLightLevel(fluidStack));
		light = light & 15728640 | luminosity << 4;
		renderFluidBox(tex, xMin, yMin, zMin, xMax, yMax, zMax, builder, ms, light, renderBottom, color);
	}

	public static void renderFluidBox(ResourceLocation tex, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax, VertexConsumer builder, PoseStack ms, int light, boolean renderBottom, int color) {
		TextureAtlasSprite fluidTexture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tex);
		ms.pushPose();
		for (Direction side : Direction.values()) {
			if (side != Direction.DOWN || renderBottom) {
				boolean positive = side.getAxisDirection() == AxisDirection.POSITIVE;
				if (side.getAxis().isHorizontal()) {
					if (side.getAxis() == Axis.X) {
						renderStillTiledFace(side, zMin, yMin, zMax, yMax, positive ? xMax : xMin, builder, ms, light, color, fluidTexture);
					} else {
						renderStillTiledFace(side, xMin, yMin, xMax, yMax, positive ? zMax : zMin, builder, ms, light, color, fluidTexture);
					}
				} else {
					renderStillTiledFace(side, xMin, zMin, xMax, zMax, positive ? yMax : yMin, builder, ms, light, color, fluidTexture);
				}
			}
		}

		ms.popPose();
	}

	public static void renderStillTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture) {
		renderTiledFace(dir, left, down, right, up, depth, builder, ms, light, color, texture, 1.0F);
	}

	public static void renderTiledFace(Direction dir, float left, float down, float right, float up, float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture, float textureScale) {
		boolean positive = dir.getAxisDirection() == AxisDirection.POSITIVE;
		boolean horizontal = dir.getAxis().isHorizontal();
		boolean x = dir.getAxis() == Axis.X;
		float shrink = texture.uvShrinkRatio() * 0.25F * textureScale;
		float centerU = texture.getU0() + (texture.getU1() - texture.getU0()) * 0.5F * textureScale;
		float centerV = texture.getV0() + (texture.getV1() - texture.getV0()) * 0.5F * textureScale;
		float x2 = 0.0F;
		float y2 = 0.0F;

		for (float x1 = left; x1 < right; x1 = x2) {
			float f = (float) Mth.floor(x1);
			x2 = Math.min(f + 1.0F, right);
			float u1;
			float u2;
			if (dir != Direction.NORTH && dir != Direction.EAST) {
				u1 = texture.getU((x1 - f) * 16.0F * textureScale);
				u2 = texture.getU((x2 - f) * 16.0F * textureScale);
			} else {
				f = (float) Mth.ceil(x2);
				u1 = texture.getU((f - x2) * 16.0F * textureScale);
				u2 = texture.getU((f - x1) * 16.0F * textureScale);
			}

			u1 = Mth.lerp(shrink, u1, centerU);
			u2 = Mth.lerp(shrink, u2, centerU);

			for (float y1 = down; y1 < up; y1 = y2) {
				f = (float) Mth.floor(y1);
				y2 = Math.min(f + 1.0F, up);
				float v1;
				float v2;
				if (dir == Direction.UP) {
					v1 = texture.getV((y1 - f) * 16.0F * textureScale);
					v2 = texture.getV((y2 - f) * 16.0F * textureScale);
				} else {
					f = (float) Mth.ceil(y2);
					v1 = texture.getV((f - y2) * 16.0F * textureScale);
					v2 = texture.getV((f - y1) * 16.0F * textureScale);
				}

				v1 = Mth.lerp(shrink, v1, centerV);
				v2 = Mth.lerp(shrink, v2, centerV);
				if (horizontal) {
					if (x) {
						putVertex(builder, ms, depth, y2, positive ? x2 : x1, color, u1, v1, dir, light);
						putVertex(builder, ms, depth, y1, positive ? x2 : x1, color, u1, v2, dir, light);
						putVertex(builder, ms, depth, y1, positive ? x1 : x2, color, u2, v2, dir, light);
						putVertex(builder, ms, depth, y2, positive ? x1 : x2, color, u2, v1, dir, light);
					} else {
						putVertex(builder, ms, positive ? x1 : x2, y2, depth, color, u1, v1, dir, light);
						putVertex(builder, ms, positive ? x1 : x2, y1, depth, color, u1, v2, dir, light);
						putVertex(builder, ms, positive ? x2 : x1, y1, depth, color, u2, v2, dir, light);
						putVertex(builder, ms, positive ? x2 : x1, y2, depth, color, u2, v1, dir, light);
					}
				} else {
					putVertex(builder, ms, x1, depth, positive ? y1 : y2, color, u1, v1, dir, light);
					putVertex(builder, ms, x1, depth, positive ? y2 : y1, color, u1, v2, dir, light);
					putVertex(builder, ms, x2, depth, positive ? y2 : y1, color, u2, v2, dir, light);
					putVertex(builder, ms, x2, depth, positive ? y1 : y2, color, u2, v1, dir, light);
				}
			}
		}

	}

	private static void putVertex(VertexConsumer builder, PoseStack ms, float x, float y, float z, int color, float u, float v, Direction face, int light) {
		Vec3i normal = face.getNormal();
		PoseStack.Pose peek = ms.last();
		int a = color >> 24 & 255;
		int r = color >> 16 & 255;
		int g = color >> 8 & 255;
		int b = color & 255;
		builder.vertex(peek.pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(peek.normal(), (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).endVertex();
	}

	private static class RenderTypes extends RenderStateShard {
		private static final RenderType FLUID;

		private static String createLayerName(String name) {
			return YoukaisHomecoming.MODID + ":" + name;
		}

		public RenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
			super(p_110161_, p_110162_, p_110163_);
		}

		static {
			FLUID = RenderType.create(createLayerName("fluid"), DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER).setTextureState(BLOCK_SHEET_MIPPED).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true));
		}
	}
}
