package dev.xkmc.youkaishomecoming.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.youkaishomecoming.compat.gateway.GatewayEventHandlers;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMCompat;
import dev.xkmc.youkaishomecoming.content.capability.FrogGodCapability;
import dev.xkmc.youkaishomecoming.content.capability.FrogSyncPacket;
import dev.xkmc.youkaishomecoming.content.capability.KoishiAttackCapability;
import dev.xkmc.youkaishomecoming.content.capability.KoishiStartPacket;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
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

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			loc("main"), 1,
			e -> e.create(FrogSyncPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT),
			e -> e.create(KoishiStartPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public static final SimpleEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("youkais_homecoming", "Youkai's Homecoming",
					e -> e.icon(YHItems.SUWAKO_HAT::asStack));

	public YoukaisHomecoming() {
		YHItems.register();
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHSounds.register();
		YHGLMProvider.register();
		YHCriteriaTriggers.register();
		KoishiAttackCapability.register();
		FrogGodCapability.register();
		YHModConfig.init();

		if (ModList.get().isLoaded(Gateways.MODID)) {
			MinecraftForge.EVENT_BUS.register(GatewayEventHandlers.class);
		}
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {


			if (ModList.get().isLoaded(Thirst.ID)) {
				ThirstCompat.init();
			}

			var thrower = new AbstractProjectileDispenseBehavior() {
				protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
					return Util.make(new FrozenFrog(level, pos.x(), pos.y(), pos.z()), e -> e.setItem(stack));
				}
			};

			DispenserBlock.registerBehavior(YHItems.FROZEN_FROG_COLD.get(), thrower);
			DispenserBlock.registerBehavior(YHItems.FROZEN_FROG_WARM.get(), thrower);
			DispenserBlock.registerBehavior(YHItems.FROZEN_FROG_TEMPERATE.get(), thrower);

			DispenserBlock.registerBehavior(YHItems.FAIRY_ICE_CRYSTAL.get(), new AbstractProjectileDispenseBehavior() {
				protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
					return new FairyIce(level, pos.x(), pos.y(), pos.z());
				}
			});

		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
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
		gen.addProvider(server, new SlotGen(gen));
		new YHDamageTypes(output, pvd, helper).generate(server, gen);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}
}
