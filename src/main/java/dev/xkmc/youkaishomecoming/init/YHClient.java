package dev.xkmc.youkaishomecoming.init;

import dev.xkmc.youkaishomecoming.content.block.combined.CompositeModel;
import dev.xkmc.youkaishomecoming.content.client.YHRecipeCategories;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileClientTooltip;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileInfoDisplay;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = EventBusSubscriber.Bus.MOD)
public class YHClient {

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, YoukaisHomecoming.loc("info_tile"), new TileInfoDisplay());
	}

	@SubscribeEvent
	public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(TileTooltip.class, TileClientTooltip::new);
	}

	@SubscribeEvent
	public static void registerRecipeTab(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(YoukaisHomecoming.MOKA, List.of(YHRecipeCategories.MOKA));
		event.registerRecipeCategoryFinder(YHBlocks.MOKA_RT.get(), e -> YHRecipeCategories.MOKA);
		event.registerBookCategories(YoukaisHomecoming.KETTLE, List.of(YHRecipeCategories.KETTLE));
		event.registerRecipeCategoryFinder(YHBlocks.KETTLE_RT.get(), e -> YHRecipeCategories.KETTLE);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LampreyModel.LAYER_LOCATION, LampreyModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void addGeometry(ModelEvent.ModifyBakingResult event) {
		for (var state : YHBlocks.COMPLEX_SLAB.get().getStateDefinition().getPossibleStates()) {
			var loc = BlockModelShaper.stateToModelLocation(state);
			event.getModels().computeIfPresent(loc, (k, model) -> new CompositeModel(model, new ConcurrentHashMap<>()));
		}
		for (var state : YHBlocks.COMPLEX_STAIRS.get().getStateDefinition().getPossibleStates()) {
			var loc = BlockModelShaper.stateToModelLocation(state);
			event.getModels().computeIfPresent(loc, (k, model) -> new CompositeModel(model, new ConcurrentHashMap<>()));
		}
	}

}
