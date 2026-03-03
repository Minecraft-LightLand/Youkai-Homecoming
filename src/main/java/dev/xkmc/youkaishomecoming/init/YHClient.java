package dev.xkmc.youkaishomecoming.init;

import dev.xkmc.youkaishomecoming.content.client.CamelliaHeadDeco;
import dev.xkmc.youkaishomecoming.content.client.CamelliaHeadLayer;
import dev.xkmc.youkaishomecoming.content.client.YHRecipeCategories;
import dev.xkmc.youkaishomecoming.content.entity.boar.BoarModel;
import dev.xkmc.youkaishomecoming.content.entity.boar.BoarModelData;
import dev.xkmc.youkaishomecoming.content.entity.crab.CrabModel;
import dev.xkmc.youkaishomecoming.content.entity.crab.CrabModelData;
import dev.xkmc.youkaishomecoming.content.entity.deer.DeerModel;
import dev.xkmc.youkaishomecoming.content.entity.deer.DeerModelData;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import dev.xkmc.youkaishomecoming.content.entity.tuna.TunaModel;
import dev.xkmc.youkaishomecoming.content.item.fluid.BottleTexture;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.pot.overlay.HintOverlay;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileClientTooltip;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileInfoDisplay;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.List;
import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID)
public class YHClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(YHItems.SAKE_BOTTLE.get(), YoukaisHomecoming.loc("slip"),
					(stack, level, user, index) -> SlipBottleItem.texture(stack));
			ItemProperties.register(YHItems.SAKE_BOTTLE.get(), YoukaisHomecoming.loc("bottle"),
					(stack, level, user, index) -> BottleTexture.texture(stack));
		});

	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, YoukaisHomecoming.loc("info_tile"), new TileInfoDisplay());
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, YoukaisHomecoming.loc("cuisine_hint"), new HintOverlay());
	}

	@SubscribeEvent
	public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(TileTooltip.class, TileClientTooltip::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CamelliaHeadDeco.LAYER_LOCATION, CamelliaHeadDeco::createBodyLayer);
		event.registerLayerDefinition(LampreyModel.LAYER_LOCATION, LampreyModel::createBodyLayer);
		event.registerLayerDefinition(TunaModel.LAYER_LOCATION, TunaModel::createBodyLayer);
		event.registerLayerDefinition(DeerModel.LAYER_LOCATION, DeerModelData::createBodyLayer);
		event.registerLayerDefinition(BoarModel.LAYER_LOCATION, BoarModelData::createBodyLayer);
		event.registerLayerDefinition(CrabModel.LAYER_LOCATION, CrabModelData::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerRecipeTab(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(YoukaisHomecoming.KETTLE, List.of(YHRecipeCategories.KETTLE.get()));
		event.registerRecipeCategoryFinder(YHBlocks.KETTLE_RT.get(), e -> YHRecipeCategories.KETTLE.get());
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
		ler.addLayer(new CamelliaHeadLayer<>(ler, mc.getEntityModels()));
	}

}
