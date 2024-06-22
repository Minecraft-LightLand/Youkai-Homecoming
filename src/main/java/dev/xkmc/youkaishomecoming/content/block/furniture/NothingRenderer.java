package dev.xkmc.youkaishomecoming.content.block.furniture;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NothingRenderer<T extends Entity> extends EntityRenderer<T> {

	public NothingRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public boolean shouldRender(T e, Frustum f, double x, double y, double z) {
		return false;
	}

	public ResourceLocation getTextureLocation(T e) {
		return null;
	}
}
