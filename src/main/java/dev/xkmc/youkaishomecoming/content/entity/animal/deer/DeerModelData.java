package dev.xkmc.youkaishomecoming.content.entity.animal.deer;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * Made with Blockbench 4.12.5
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 *
 * @author Author
 */
public class DeerModelData {

	public static final AnimationDefinition SMELL, WALK, RUN, RELAX_START, RELAX, RELAX_END, ATTACK, EAT;

	// smell
	static {
		SMELL = AnimationDefinition.Builder.withLength(0.7083F)
				.addAnimation("up_mouth", new AnimationChannel(AnimationChannel.Targets.SCALE,
						new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("nose", new AnimationChannel(AnimationChannel.Targets.SCALE,
						new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.3F, 1.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3333F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.5417F, KeyframeAnimations.scaleVec(1.0F, 1.3F, 1.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// walk
	static {
		WALK = AnimationDefinition.Builder.withLength(1.5F).looping()
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-3.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(-1.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-3.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2917F, KeyframeAnimations.degreeVec(3.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-3.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(-3.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9167F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.3F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.3F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1667F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4583F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2083F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9583F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.3F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, 0.3F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.3F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.3F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}

	// run
	static {
		RUN = AnimationDefinition.Builder.withLength(1.3194F).looping()
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1042F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3819F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5903F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7986F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1806F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2847F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1042F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3819F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5903F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7986F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1806F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2847F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.degreeVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6597F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9722F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1389F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3125F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3819F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4514F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5556F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.625F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7639F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9375F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0069F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1806F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1389F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.2778F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3819F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.7639F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.9028F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1389F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4514F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5208F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7639F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8681F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9375F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1458F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.2431F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3125F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4514F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.5903F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.6944F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.9375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0764F, KeyframeAnimations.posVec(0.0F, 0.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.2153F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1042F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2083F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3125F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5903F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6597F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7639F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8681F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9722F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1458F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1042F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.1F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3125F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4861F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.5903F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.6597F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.7639F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8681F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.1F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.9722F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0764F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.1458F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.3194F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1042F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1736F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.2431F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3125F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3819F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4861F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5556F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6597F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7639F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9028F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9722F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0764F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1458F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.2153F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.3194F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1736F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.2431F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.3819F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.1F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.4861F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.9028F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.5F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.0764F, KeyframeAnimations.posVec(0.0F, 0.5F, -0.1F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(1.1458F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// relax start
	static {
		RELAX_START = AnimationDefinition.Builder.withLength(1.0714F)
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6548F, KeyframeAnimations.degreeVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0119F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.2381F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6548F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0119F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.1786F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7143F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9524F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.1786F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5357F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7738F, KeyframeAnimations.degreeVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0119F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8929F, KeyframeAnimations.degreeVec(-70.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.7143F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8929F, KeyframeAnimations.posVec(0.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-22.5F, 10.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8929F, KeyframeAnimations.degreeVec(-70.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.7143F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8929F, KeyframeAnimations.posVec(0.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.1786F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5357F, KeyframeAnimations.degreeVec(-50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8929F, KeyframeAnimations.degreeVec(-80.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.7143F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8929F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.1786F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5357F, KeyframeAnimations.degreeVec(-50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8929F, KeyframeAnimations.degreeVec(-80.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.7143F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.8929F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// relax
	static {
		RELAX = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// relax end
	static {
		RELAX_END = AnimationDefinition.Builder.withLength(1.1539F)
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1923F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3846F, KeyframeAnimations.degreeVec(5.37F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5769F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("root", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3846F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7692F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1923F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5128F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7692F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.1923F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5128F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8974F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-70.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5769F, KeyframeAnimations.degreeVec(-22.5F, 10.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0256F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg0", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1923F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-70.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5769F, KeyframeAnimations.degreeVec(-22.5F, -10.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0256F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg1", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1923F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3846F, KeyframeAnimations.degreeVec(-50.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7692F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg2", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1923F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-80.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3846F, KeyframeAnimations.degreeVec(-50.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.7692F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("leg3", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.1923F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// attack
	static {
		ATTACK = AnimationDefinition.Builder.withLength(0.0F)
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}

	// eat
	static {
		EAT = AnimationDefinition.Builder.withLength(1.6667F)
				.addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0417F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("down_mouth", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5556F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.6944F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.9722F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1111F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, -8.0F));

		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(56, 27).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 13).addBox(-3.5F, -7.0F, -4.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -1.0F));

		PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 11.0F));

		PartDefinition left_horn = horn.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(72, 51).addBox(-1.0F, -2.25F, -1.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 56).addBox(-2.0F, -8.245F, -2.25F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -16.175F, -9.75F, -0.1745F, 0.0F, 0.4363F));

		PartDefinition right_horn = horn.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(44, 75).addBox(-1.0F, -2.25F, -1.25F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(56, 0).addBox(-6.0F, -8.245F, -2.25F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -16.175F, -9.75F, -0.1745F, 0.0F, -0.4363F));

		PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(7.0F, 16.0F, 12.0F));

		PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(72, 43).addBox(0.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.775F, -21.4F, -9.675F, 0.0F, -0.1309F, -0.1745F));

		PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(72, 47).addBox(-4.0F, -1.5F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.225F, -21.4F, -9.675F, 0.0F, 0.1309F, 0.1745F));

		PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offset(1.0F, 18.0F, 11.7F));

		PartDefinition up_mouth = mouth.addOrReplaceChild("up_mouth", CubeListBuilder.create().texOffs(1, 70).addBox(-2.0F, -2.75F, -2.7F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -20.25F, -16.0F));

		PartDefinition nose = up_mouth.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(16, 78).addBox(-2.0F, -2.75F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.7F));

		PartDefinition down_mouth = mouth.addOrReplaceChild("down_mouth", CubeListBuilder.create().texOffs(16, 72).addBox(-2.0F, 0.25F, -3.7F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -20.25F, -16.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.0F, -8.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-5.0F, -3.0F, -8.0F, 10.0F, 10.0F, 18.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -4.0F, 9.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 72).addBox(-2.0F, -5.0F, -1.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.025F, 10.1F));

		PartDefinition leg0 = root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(30, 56).addBox(-1.375F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.625F, 3.0F, 17.0F));

		PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(56, 43).addBox(-3.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 17.0F));

		PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(46, 59).addBox(-1.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 2.0F));

		PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(62, 59).addBox(-3.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}