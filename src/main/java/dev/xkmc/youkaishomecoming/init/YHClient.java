package dev.xkmc.youkaishomecoming.init;

import dev.xkmc.youkaishomecoming.content.client.CfRecipeCategories;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YHClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
		});

	}

	@SubscribeEvent
	public static void registerRecipeTab(RegisterRecipeBookCategoriesEvent event) {
		event.registerBookCategories(YoukaisHomecoming.MOKA, List.of(CfRecipeCategories.MOKA.get()));
		event.registerRecipeCategoryFinder(CoffeeBlocks.MOKA_RT.get(), e -> CfRecipeCategories.MOKA.get());
	}


}
