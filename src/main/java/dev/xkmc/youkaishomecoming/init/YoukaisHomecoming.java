package dev.xkmc.youkaishomecoming.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import dev.ghen.thirst.Thirst;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.IngReg;
import dev.xkmc.l2core.init.reg.simple.IngVal;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.ConfigTypeEntry;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.youkaishomecoming.compat.terrablender.Terrablender;
import dev.xkmc.youkaishomecoming.compat.thirst.ThirstCompat;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleIngredient;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.content.pot.table.item.ModelIngredientData;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.events.YHAttackListener;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.food.InitializationMarker;
import dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import dev.xkmc.youkaishomecoming.mixin.ItemAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(YoukaisHomecoming.MODID)
@EventBusSubscriber(modid = YoukaisHomecoming.MODID)
public class YoukaisHomecoming {

	public static final String MODID = "youkaisfeasts";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static final Reg REG = new Reg(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(MODID, 2);

	public static final SimpleEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("block", "Youkais' Feasts - Utensil and Tools",
					e -> e.icon(YHBlocks.STEAMER_POT::asStack));

	public static final SimpleEntry<CreativeModeTab> CROP =
			REGISTRATE.buildModCreativeTab("crop", "Youkais' Feasts - Crops",
					e -> e.icon(YHItems.CAMELLIA::asStack));

	public static final SimpleEntry<CreativeModeTab> FOOD =
			REGISTRATE.buildModCreativeTab("food", "Youkais' Feasts - Food and Ingredients",
					e -> e.icon(YHSushi.LORELEI_NIGIRI.item::asStack));

	public static final SimpleEntry<CreativeModeTab> DECO =
			REGISTRATE.buildModCreativeTab("deco", "Youkais' Feasts - Furniture",
					e -> e.icon(YHBlocks.WoodType.OAK.seat::asStack));

	public static final RecipeBookType KETTLE = Enum.valueOf(RecipeBookType.class, "YOUKAISFEASTS_KETTLE");

	public static final ConfigTypeEntry<ModelIngredientData> INGREDIENT = new ConfigTypeEntry<>(HANDLER, "ingredient", ModelIngredientData.class);

	public static final IngVal<SlipBottleIngredient> ING_BOTTLE = IngReg.of(REG).reg("slip_bottle", SlipBottleIngredient.class);

	public YoukaisHomecoming() {
		Handlers.enableVanilla(Fluid.class, BuiltInRegistries.FLUID);
		InitializationMarker.expectAndAdvance(0);
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHSounds.register();
		YHGLMProvider.register();
		YHWorldGen.register();
		YHBiomes.register();
		YHCriteriaTriggers.register();
		YHModConfig.init();
		TableItemManager.init();
		FilterHolderSet.register();

		AttackEventHandler.register(3943, new YHAttackListener());
		HANDLER.addAfterReloadListener(() -> INGREDIENT.getMerged().onSync());
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			Terrablender.registerBiomes();

			if (ModList.get().isLoaded(Thirst.ID)) {
				ThirstCompat.init();
			}

			((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		TableItemManager.prepareData();
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, YHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, YHLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, YHAdvGen::genAdv);
		REGISTRATE.addDataGenerator(YHBiomeTagsProvider.TYPE, YHBiomeTagsProvider::genTag);
		REGISTRATE.addDataGenerator(YHGLMProvider.TYPE, YHGLMProvider::gen);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new YHConfigGen(gen, pvd));
		gen.addProvider(event.includeClient(), new AdditionalModelProvider(output, MODID));
		YHDatapackRegistriesGen.register();
		var init = REGISTRATE.getDataGenInitializer();
		init.addDependency(YHBiomeTagsProvider.TYPE, ProviderType.DYNAMIC);
		init.addDependency(YHGLMProvider.TYPE, YHBiomeTagsProvider.TYPE);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
