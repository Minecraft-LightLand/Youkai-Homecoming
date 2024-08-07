package dev.xkmc.youkaishomecoming.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
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
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	public static final RecipeBookType MOKA = RecipeBookType.create("MOKA");
	public static final RecipeBookType KETTLE = RecipeBookType.create("KETTLE");

	public YoukaisHomecoming() {
		YHItems.register();
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHGLMProvider.register();
		YHBlocks.YHCriteriaTriggers.register();
		YHModConfig.init();
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		for (var e : YHSake.values()) {
			event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new SakeFluidWrapper(stack), e.item().get());
		}
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.MOKA_BE.get(), BasePotBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.KETTLE_BE.get(), KettleBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FermentationTankBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FermentationTankBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.RACK_BE.get(), DryingRackBlockEntity::getItemHandler);

	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

			YHEffects.registerBrewingRecipe();

			((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {

		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, YHTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, YHTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, YHTagGen::onEntityTagGen);
		REGISTRATE.addDataGenerator(YHTagGen.EFF_TAGS, YHTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, YHLangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, YHLootGen::genLoot);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, YHAdvGen::genAdv);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, YHCrops::registerComposters);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new YHConfigGen(gen));
		//var reg = new YHDatapackRegistriesGen(output, pvd);
		//gen.addProvider(server, reg);
		gen.addProvider(server, new YHBiomeTagsProvider(output, pvd, helper));
		gen.addProvider(server, new YHGLMProvider(output, pvd));
		//gen.addProvider(server, new SlotGen(gen));
		//new YHDamageTypes(output, pvd, helper).generate(server, gen);
	}

	@SubscribeEvent
	public static void onSpawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
		event.register(YHEntities.LAMPREY.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> pos.getY() >= 50 && pos.getY() <= 64,
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
