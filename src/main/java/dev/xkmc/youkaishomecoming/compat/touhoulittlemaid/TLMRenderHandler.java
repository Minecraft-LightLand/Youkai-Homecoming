package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Function;

public class TLMRenderHandler {

	public static boolean render(Mob e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		if (RENDERER == null) return false;
		RENDERER.render(e, yaw, pTick, pose, buffer, light);
		return true;
	}

	private static LivingEntityRenderer<Mob, ?> RENDERER;

	@SubscribeEvent
	public static void onMaidConvert(ConvertMaidEvent event) {
		if (!(event.getEntity() instanceof GeneralYoukaiEntity mob)) return;
		event.setMaid(new MaidWrapper(mob));
	}

	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		RENDERER = new EntityMaidRenderer(event.getContext());
		addCircle(RENDERER, SpellCircleLayer::new);
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void addCircle(LivingEntityRenderer<T, M> renderer, Function<LivingEntityRenderer<T, M>, RenderLayer<T, M>> factory) {
		renderer.addLayer(factory.apply(renderer));
	}

	private record MaidWrapper(GeneralYoukaiEntity mob) implements IMaid {

		@Override
		public String getModelId() {
			return mob.getModelId();
		}

		@Override
		public Mob asEntity() {
			return mob;
		}

	}


}
