package dev.xkmc.fastprojectileapi.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public abstract class DanmakuRenderStates extends RenderType {


	public DanmakuRenderStates(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}

	protected static final ShaderStateShard DANMAKU_SHADER = new ShaderStateShard(GameRenderer::getPositionTexColorShader);

	private static RenderType create(String name, ResourceLocation tex, boolean cull, DisplayType type) {
		return create(name,
				DefaultVertexFormat.POSITION_TEX_COLOR,
				VertexFormat.Mode.QUADS,
				256, true, type != DisplayType.SOLID,
				CompositeState.builder()
						.setShaderState(DANMAKU_SHADER)
						.setTextureState(new TextureStateShard(tex, false, false))
						.setTransparencyState(switch (type) {
							case SOLID -> NO_TRANSPARENCY;
							case TRANSPARENT -> TRANSLUCENT_TRANSPARENCY;
							case ADDITIVE -> ADDITIVE_TRANSPARENCY;
						})
						.setCullState(cull ? CULL : NO_CULL)
						.createCompositeState(false));
	}

	private static final BiFunction<ResourceLocation, DisplayType, RenderType> DANMAKU =
			Util.memoize((rl, type) -> create("danmaku_" + type.getName(), rl, false, type));
	private static final BiFunction<ResourceLocation, DisplayType, RenderType> LASER =
			Util.memoize((rl, type) -> create("laser_" + type.getName(), rl, true, type));

	public static RenderType danmaku(ResourceLocation rl, DisplayType type) {
		return DANMAKU.apply(rl, type);
	}

	public static RenderType laser(ResourceLocation rl, DisplayType type) {
		return LASER.apply(rl, type);
	}

}
