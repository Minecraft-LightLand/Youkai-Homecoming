package dev.xkmc.youkaishomecoming.init;

import dev.xkmc.youkaishomecoming.content.block.combined.CompositeModel;
import dev.xkmc.youkaishomecoming.content.client.YHRecipeCategories;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyModel;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleItem;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileClientTooltip;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = EventBusSubscriber.Bus.MOD)
public class YHClient {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(YHItems.SAKE_BOTTLE.get(), YoukaisHomecoming.loc("slip"),
					(stack, level, user, index) -> SlipBottleItem.texture(stack));
		});
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
