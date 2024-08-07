package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TLMRenderHandler {

	public static boolean render(Mob e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		if (RENDERER == null) return false;
		RENDERER.render(e, yaw, pTick, pose, buffer, light);
		return true;
	}

	private static EntityMaidRenderer RENDERER;

	@SubscribeEvent
	public static void onMaidConvert(ConvertMaidEvent event) {
		if (!(event.getEntity() instanceof GeneralYoukaiEntity mob)) return;
		String id = mob.getModelId();
		if (id != null) event.setMaid(new MaidWrapper(mob, id));
	}

	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		RENDERER = new EntityMaidRenderer(event.getContext());
		RENDERER.addLayer(new SpellCircleLayer<>(RENDERER));
	}

	private record MaidWrapper(Mob mob, String id) implements IMaid {

		@Override
		public String getModelId() {
			return id;
		}

		@Override
		public Mob asEntity() {
			return mob;
		}

	}


}
