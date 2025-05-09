package dev.xkmc.fastprojectileapi.render.core;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import org.joml.Quaternionf;

public interface ProjectileRenderer<T extends SimplifiedProjectile> {

	Quaternionf cameraOrientation();

	void render(T e, float pTick, PoseStack pose);

	double fading(SimplifiedProjectile e);

}
