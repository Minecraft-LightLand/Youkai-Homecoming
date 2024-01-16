package dev.xkmc.youkaihomecoming.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaihomecoming.init.data.*;
import dev.xkmc.youkaihomecoming.init.registrate.FDBlocks;
import dev.xkmc.youkaihomecoming.init.registrate.FDEffects;
import dev.xkmc.youkaihomecoming.init.registrate.FDItems;
import dev.xkmc.youkaihomecoming.init.registrate.FDMiscs;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(YoukaiHomecoming.MODID)
@Mod.EventBusSubscriber(modid = YoukaiHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YoukaiHomecoming {

	public static final String MODID = "youkaihomecoming";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final RegistryEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("youkai_homecoming", "Youkai Homecoming",
					e -> e.icon(() -> Items.IRON_INGOT.getDefaultInstance()));

	public YoukaiHomecoming() {
		FDItems.register();
		FDEffects.register();
		FDBlocks.register();
		FDMiscs.register();
		YHModConfig.init();

		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new YHConfigGen(gen));
	}

}
