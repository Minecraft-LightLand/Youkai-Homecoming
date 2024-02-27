package dev.xkmc.youkaihomecoming.init;

import dev.xkmc.youkaihomecoming.content.client.SuwakoHatModel;
import dev.xkmc.youkaihomecoming.content.client.YHRecipeCategories;
import dev.xkmc.youkaihomecoming.content.entity.LampreyModel;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaiHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YHClient {

	@SubscribeEvent
	public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
	}

	@SubscribeEvent
	public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(LampreyModel.LAYER_LOCATION, LampreyModel::createBodyLayer);
		event.registerLayerDefinition(SuwakoHatModel.LAYER_LOCATION, SuwakoHatModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerRecipeTab(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(YoukaiHomecoming.MOKA, List.of(YHRecipeCategories.MOKA.get()));
		event.registerRecipeCategoryFinder(YHBlocks.MOKA_RT.get(), e -> YHRecipeCategories.MOKA.get());
		event.registerBookCategories(YoukaiHomecoming.KETTLE, List.of(YHRecipeCategories.KETTLE.get()));
		event.registerRecipeCategoryFinder(YHBlocks.KETTLE_RT.get(), e -> YHRecipeCategories.KETTLE.get());
	}

}
