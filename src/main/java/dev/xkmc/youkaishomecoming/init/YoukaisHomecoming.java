package dev.xkmc.youkaishomecoming.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.ghen.thirst.Thirst;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.serialization.custom_handler.CodecHandler;
import dev.xkmc.youkaishomecoming.compat.thirst.ThirstCompat;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluidWrapper;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.food.YHSake;
import dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import dev.xkmc.youkaishomecoming.mixin.ItemAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(YoukaisHomecoming.MODID)
@EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = EventBusSubscriber.Bus.MOD)
public class YoukaisHomecoming {

	static final boolean ENABLE_TLM = true;

	public static final String MODID = "youkaishomecoming";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(MODID, 1);

	public static final SimpleEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("youkais_homecoming", "Youkai's Homecoming",
					e -> e.icon(YHItems.OOLONG_TEA_BAG::asStack));

	public static final RecipeBookType MOKA = Enum.valueOf(RecipeBookType.class, "YOUKAISHOMECOMING_MOKA");
	public static final RecipeBookType KETTLE = Enum.valueOf(RecipeBookType.class, "YOUKAISHOMECOMING_KETTLE");

	public YoukaisHomecoming() {
		YHItems.register();
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHGLMProvider.register();
		YHModConfig.init();

		if (ModList.get().isLoaded(Thirst.ID)) {
			ThirstCompat.init();
		}
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		for (var e : YHSake.values()) {
			event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SakeFluidWrapper(stack), e.item().get());
		}
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SakeFluidWrapper(stack), YHItems.SOY_SAUCE_BOTTLE.item().get());

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.MOKA_BE.get(), BasePotBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.KETTLE_BE.get(), KettleBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FermentationTankBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FermentationTankBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.RACK_BE.get(), DryingRackBlockEntity::getItemHandler);

	}

	private static void init(){
		((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);
		new CodecHandler<>(FluidIngredient.class, FluidIngredient.CODEC, FluidIngredient.STREAM_CODEC);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(YoukaisHomecoming::init);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		init();

		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, YHTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, YHLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, YHAdvGen::genAdv);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, YHCrops::registerComposters);
		var init = REGISTRATE.getDataGenInitializer();
		init.add(Registries.CONFIGURED_FEATURE, ctx -> Arrays.stream(YHCrops.values()).forEach(e -> e.registerConfigs(ctx)));
		init.add(Registries.PLACED_FEATURE, ctx -> Arrays.stream(YHCrops.values()).forEach(e -> e.registerPlacements(ctx)));
		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, YHDynamicGen::registerBiomeModifiers);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new YHConfigGen(gen, pvd));
		gen.addProvider(server, new YHBiomeTagsProvider(output, pvd, helper));
		gen.addProvider(server, new YHGLMProvider(output, pvd));
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
