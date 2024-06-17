package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class DanmakuRenderStates extends RenderType {


	public DanmakuRenderStates(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}

	protected static final RenderStateShard.ShaderStateShard LASER_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getPositionTexColorNormalShader);

	private static final Function<ResourceLocation, RenderType> SOLID = Util.memoize((rl) ->
			create("danmaku_solid",
					DefaultVertexFormat.POSITION_COLOR_TEX,
					VertexFormat.Mode.QUADS,
					256, true, false,
					CompositeState.builder()
							.setShaderState(POSITION_COLOR_TEX_SHADER)
							.setTextureState(new TextureStateShard(rl, false, false))
							.setTransparencyState(NO_TRANSPARENCY)
							.setCullState(NO_CULL)
							.createCompositeState(false)));

	private static final Function<ResourceLocation, RenderType> LASER = Util.memoize((rl) ->
			create("laser_solid",
					DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL,
					VertexFormat.Mode.QUADS,
					256, true, false,
					CompositeState.builder()
							.setShaderState(LASER_SHADER)
							.setTextureState(new TextureStateShard(rl, false, false))
							.setTransparencyState(NO_TRANSPARENCY)
							.setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
							.setCullState(CULL)
							.createCompositeState(false)));

	private static final Function<ResourceLocation, RenderType> TRANSPARENT = Util.memoize((rl) ->
			create("laser_transparent",
					DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL,
					VertexFormat.Mode.QUADS,
					256, true, false,
					CompositeState.builder()
							.setShaderState(LASER_SHADER)
							.setTextureState(new TextureStateShard(rl, false, false))
							.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
							.setWriteMaskState(RenderStateShard.COLOR_WRITE)
							.setCullState(CULL)
							.createCompositeState(false)));

	private static final Function<ResourceLocation, RenderType> ADDITIVE = Util.memoize((rl) ->
			create("laser_additive",
					DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL,
					VertexFormat.Mode.QUADS,
					256, true, false,
					CompositeState.builder()
							.setShaderState(LASER_SHADER)
							.setTextureState(new TextureStateShard(rl, false, false))
							.setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
							.setWriteMaskState(RenderStateShard.COLOR_WRITE)
							.setCullState(CULL)
							.createCompositeState(false)));

	public static RenderType danmaku(ResourceLocation rl) {
		return SOLID.apply(rl);
	}

	public static RenderType laser(ResourceLocation rl) {
		return LASER.apply(rl);
	}

	public static RenderType transparent(ResourceLocation rl) {
		return TRANSPARENT.apply(rl);
	}

	public static RenderType additive(ResourceLocation rl) {
		return ADDITIVE.apply(rl);
	}

}
