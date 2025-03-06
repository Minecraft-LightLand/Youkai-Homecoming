package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.ConvertMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.YsmMaidInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
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
		event.setMaid(new MaidWrapper(mob));
	}

	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		RENDERER = new EntityMaidRenderer(event.getContext());
		RENDERER.addLayer(new SpellCircleLayer<>(RENDERER));
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

		@Override
		public boolean isYsmModel() {
			return mob.getModelId().startsWith("{YSM}");
		}

		private YsmMaidInfo getInfo() {
			if (!isYsmModel()) return YsmMaidInfo.EMPTY;
			try {
				var tag = TagParser.parseTag(mob.getModelId().substring(5));
				return YsmCompat.getYsmMaidInfo(tag);
			} catch (Throwable ignored) {

			}
			return YsmMaidInfo.EMPTY;
		}

		@Override
		public String getYsmModelId() {
			var info = getInfo();
			if (!info.isYsmModel()) return "";
			return info.modelId();
		}

		@Override
		public String getYsmModelTexture() {
			var info = getInfo();
			if (!info.isYsmModel()) return "";
			return info.textureId();
		}

		@Override
		public Component getYsmModelName() {
			var info = getInfo();
			if (!info.isYsmModel()) return Component.empty();
			return info.name();
		}

	}

}
