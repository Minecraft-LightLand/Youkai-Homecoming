package dev.xkmc.youkaishomecoming.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.ghen.thirst.Thirst;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.youkaishomecoming.compat.terrablender.Terrablender;
import dev.xkmc.youkaishomecoming.compat.thirst.ThirstCompat;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.events.YHAttackListener;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.food.InitializationMarker;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import dev.xkmc.youkaishomecoming.mixin.ItemAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(YoukaisHomecoming.MODID)
@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YoukaisHomecoming {

	public static final String MODID = "youkaisfeasts";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			loc("main"), 2);

	public static final RegistryEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("block", "Youkais' Feasts - Blocks",
					e -> e.icon(YHItems.BLACK_TEA_BAG::asStack));

	public static final RegistryEntry<CreativeModeTab> FOOD =
			REGISTRATE.buildModCreativeTab("food", "Youkais' Feasts - Food and Ingredients",
					e -> e.icon(YHSushi.LORELEI_NIGIRI.item::asStack));

	public static final RecipeBookType KETTLE = RecipeBookType.create("KETTLE");

	public YoukaisHomecoming() {
		InitializationMarker.expectAndAdvance(0);
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHSounds.register();
		YHGLMProvider.register();
		YHCriteriaTriggers.register();
		YHModConfig.init();
		TableItemManager.init();
		FilterHolderSet.register();

		AttackEventHandler.register(3943, new YHAttackListener());
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			Terrablender.registerBiomes();
			for (var e : YHCrops.values())
				e.registerComposter();

			if (ModList.get().isLoaded(Thirst.ID)) {
				ThirstCompat.init();
			}

			YHEffects.registerBrewingRecipe();

			((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, YHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, YHLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, YHAdvGen::genAdv);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new YHConfigGen(gen));
		var reg = new YHDatapackRegistriesGen(output, pvd);
		gen.addProvider(server, reg);
		gen.addProvider(server, new YHBiomeTagsProvider(output, reg.getRegistryProvider(), helper));
		gen.addProvider(server, new YHGLMProvider(gen));
	}

	@SubscribeEvent
	public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
		event.register(YHEntities.LAMPREY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> pos.getY() >= 50 && pos.getY() <= 64,
				SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

	public static ResourceLocation loc(String id) {
		return new ResourceLocation(MODID, id);
	}
}
