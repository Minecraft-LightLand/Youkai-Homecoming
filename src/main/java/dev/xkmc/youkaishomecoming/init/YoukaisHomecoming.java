package dev.xkmc.youkaishomecoming.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.ghen.thirst.Thirst;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.youkaishomecoming.compat.thirst.ThirstCompat;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeEffects;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import dev.xkmc.youkaishomecoming.mixin.ItemAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;
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
			REGISTRATE.buildModCreativeTab("coffea_flavors_delight", "Coffee's Flavors & Delight",
					e -> e.icon(CoffeeItems.COFFEE_BEAN_BAG::asStack));

	public static final RecipeBookType MOKA = RecipeBookType.create("MOKA");

	public YoukaisHomecoming() {
		CoffeeBlocks.register();
		CoffeeEffects.register();
		CoffeeConfig.init();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			for (var e : CoffeeCrops.values())
				e.registerComposter();

			if (ModList.get().isLoaded(Thirst.ID)) {
				ThirstCompat.init();
			}

			((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, CoffeeTagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, CoffeeTagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, YHRecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, CoffeeLang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, CoffeeAdvGen::genAdv);

		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var reg = new CoffeeDatapackRegistriesGen(output, pvd);
		gen.addProvider(server, reg);
		gen.addProvider(server, new CoffeeBiomeTagsProvider(output, reg.getRegistryProvider(), helper));
	}

	public static ResourceLocation loc(String id) {
		return new ResourceLocation(MODID, id);
	}
}
