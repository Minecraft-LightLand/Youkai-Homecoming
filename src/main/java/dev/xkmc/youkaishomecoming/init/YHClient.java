package dev.xkmc.youkaishomecoming.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMRenderHandler;
import dev.xkmc.youkaishomecoming.content.client.*;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoModel;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuModel;
import dev.xkmc.youkaishomecoming.content.entity.rumia.BlackBallModel;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaModel;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileClientTooltip;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileInfoDisplay;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.FrogRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YHClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		if (YoukaisHomecoming.ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			MinecraftForge.EVENT_BUS.register(TLMRenderHandler.class);
		}
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "info_tile", new TileInfoDisplay());
	}

	@SubscribeEvent
	public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(TileTooltip.class, TileClientTooltip::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LampreyModel.LAYER_LOCATION, LampreyModel::createBodyLayer);
		event.registerLayerDefinition(SuwakoHatModel.SUWAKO, SuwakoHatModel::createSuwakoHat);
		event.registerLayerDefinition(SuwakoHatModel.STRAW, SuwakoHatModel::createStrawHat);
		event.registerLayerDefinition(FrogStrawHatModel.STRAW, FrogStrawHatModel::createHat);
		event.registerLayerDefinition(KoishiHatModel.HAT, KoishiHatModel::createHat);
		event.registerLayerDefinition(RumiaModel.LAYER_LOCATION, RumiaModel::createBodyLayer);
		event.registerLayerDefinition(RumiaModel.HAIRBAND, RumiaModel::createHairbandLayer);
		event.registerLayerDefinition(BlackBallModel.LAYER_LOCATION, BlackBallModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.LAYER_LOCATION, ReimuModel::createBodyLayer);
		event.registerLayerDefinition(ReimuModel.HAT_LOCATION, ReimuModel::createHatLayer);
		event.registerLayerDefinition(CirnoModel.LAYER_LOCATION, CirnoModel::createBodyLayer);
		event.registerLayerDefinition(CirnoModel.HAT_LOCATION, CirnoModel::createHatLayer);
		event.registerLayerDefinition(CirnoModel.WINGS_LOCATION, CirnoModel::createWingsLayer);
	}

	@SubscribeEvent
	public static void registerRecipeTab(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(YoukaisHomecoming.MOKA, List.of(YHRecipeCategories.MOKA.get()));
		event.registerRecipeCategoryFinder(YHBlocks.MOKA_RT.get(), e -> YHRecipeCategories.MOKA.get());
		event.registerBookCategories(YoukaisHomecoming.KETTLE, List.of(YHRecipeCategories.KETTLE.get()));
		event.registerRecipeCategoryFinder(YHBlocks.KETTLE_RT.get(), e -> YHRecipeCategories.KETTLE.get());
	}

	@SubscribeEvent
	public static void addLayer(EntityRenderersEvent.AddLayers event) {
		if (event.getRenderer(EntityType.FROG) instanceof FrogRenderer r) {
			r.addLayer(new FrogHatLayer<>(r, event.getEntityModels()));
		}
		if (YoukaisHomecoming.ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			TLMRenderHandler.addLayers(event);
		}
	}

	@SubscribeEvent
	public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) resourceManager -> registerWingsLayer());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void registerWingsLayer() {
		EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
		Map<String, EntityRenderer<? extends Player>> skinMap = renderManager.getSkinMap();
		for (EntityRenderer<? extends Player> renderer : skinMap.values()) {
			if (renderer instanceof LivingEntityRenderer ler) {
				addLayer(renderManager, ler);
			}
		}
		renderManager.renderers.forEach((e, r) -> {
			if (r instanceof LivingEntityRenderer ler && ler.getModel() instanceof HumanoidModel<?>) {
				addLayer(renderManager, ler);
			}
		});
	}

	private static <T extends LivingEntity, M extends HumanoidModel<T>> void addLayer(EntityRenderDispatcher manager, LivingEntityRenderer<T, M> ler) {
		var mc = Minecraft.getInstance();
		ler.addLayer(new CirnoWingsLayer<>(ler, mc.getEntityModels()));
	}

}
