package dev.xkmc.youkaihomecoming.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaihomecoming.init.data.*;
import dev.xkmc.youkaihomecoming.init.food.YHCrops;
import dev.xkmc.youkaihomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaihomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaihomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaihomecoming.init.registrate.YHItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
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
			REGISTRATE.buildModCreativeTab("youkai_homecoming", "Youkai's Homecoming",
					e -> e.icon(YHItems.SUWAKO_HAT::asStack));

	public static final RecipeBookType MOKA = RecipeBookType.create("MOKA");
	public static final RecipeBookType KETTLE = RecipeBookType.create("KETTLE");

	public YoukaiHomecoming() {
		YHItems.register();
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHGLMProvider.register();
		YHModConfig.init();

		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, YHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(YHTagGen.EFF_TAGS, YHTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, YHLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, YHAdvGen::genAdv);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			YHCrops.SOYBEAN.registerComposter();
			YHCrops.REDBEAN.registerComposter();
			YHCrops.COFFEA.registerComposter();
			YHCrops.TEA.registerComposter();
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
		var reg = new YHDatapackRegistriesGen(output, pvd);
		gen.addProvider(server, reg);
		gen.addProvider(server, new YHBiomeTagsProvider(output, pvd, helper));
		gen.addProvider(server, new YHGLMProvider(gen));
	}

	@SubscribeEvent
	public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
		event.register(YHEntities.LAMPREY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> pos.getY() >= 50 && pos.getY() <= 64,
				SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

}
