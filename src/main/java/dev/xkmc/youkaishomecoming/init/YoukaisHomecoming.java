package dev.xkmc.youkaishomecoming.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.ghen.thirst.Thirst;
import dev.shadowsoffire.gateways.Gateways;
import dev.xkmc.fastprojectileapi.collision.FastMapInit;
import dev.xkmc.fastprojectileapi.render.virtual.DanmakuToClientPacket;
import dev.xkmc.fastprojectileapi.render.virtual.EraseDanmakuToClient;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.youkaishomecoming.compat.gateway.GatewayEventHandlers;
import dev.xkmc.youkaishomecoming.compat.terrablender.Terrablender;
import dev.xkmc.youkaishomecoming.compat.thirst.ThirstCompat;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMCompat;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMRegistries;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy.FairySpellCards;
import dev.xkmc.youkaishomecoming.content.capability.*;
import dev.xkmc.youkaishomecoming.content.entity.misc.FairyIce;
import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
import dev.xkmc.youkaishomecoming.content.entity.youkai.CombatToClient;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipBottleIngredient;
import dev.xkmc.youkaishomecoming.content.pot.table.food.YHSushi;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.spell.custom.screen.SpellSetToServer;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.events.YHAttackListener;
import dev.xkmc.youkaishomecoming.init.data.*;
import dev.xkmc.youkaishomecoming.init.food.InitializationMarker;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import dev.xkmc.youkaishomecoming.init.loot.YHGLMProvider;
import dev.xkmc.youkaishomecoming.init.loot.YHLootGen;
import dev.xkmc.youkaishomecoming.init.registrate.*;
import dev.xkmc.youkaishomecoming.mixin.ItemAccessor;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(YoukaisHomecoming.MODID)
@Mod.EventBusSubscriber(modid = YoukaisHomecoming.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YoukaisHomecoming {

	static final boolean ENABLE_TLM = true;

	public static final String MODID = "youkaishomecoming";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			loc("main"), 2,
			e -> e.create(FrogSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(KoishiStartPacket.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(SpellSetToServer.class, NetworkDirection.PLAY_TO_SERVER),
			e -> e.create(CombatToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(GrazeHelper.GrazeToClient.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(DanmakuToClientPacket.class, NetworkDirection.PLAY_TO_CLIENT),
			e -> e.create(EraseDanmakuToClient.class, NetworkDirection.PLAY_TO_CLIENT)
	);

	public static final ConfigTypeEntry<SpellCircleConfig> SPELL = new ConfigTypeEntry<>(HANDLER, "spell_circle", SpellCircleConfig.class);

	public static final RegistryEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("block", "Youkai's Homecoming - Utensil and Tools",
					e -> e.icon(YHBlocks.STEAMER_POT::asStack));

	public static final RegistryEntry<CreativeModeTab> CROP =
			REGISTRATE.buildModCreativeTab("crop", "Youkai's Homecoming - Crops",
					e -> e.icon(YHItems.CAMELLIA::asStack));

	public static final RegistryEntry<CreativeModeTab> FOOD =
			REGISTRATE.buildModCreativeTab("food", "Youkai's Homecoming - Food and Ingredients",
					e -> e.icon(YHSushi.LORELEI_NIGIRI.item::asStack));

	public static final RegistryEntry<CreativeModeTab> DECO =
			REGISTRATE.buildModCreativeTab("deco", "Youkai's Homecoming - Furniture",
					e -> e.icon(YHBlocks.WoodType.OAK.seat::asStack));

	public static final RecipeBookType MOKA = RecipeBookType.create("MOKA");
	public static final RecipeBookType KETTLE = RecipeBookType.create("KETTLE");

	public YoukaisHomecoming() {
		Handlers.enableVanilla(Fluid.class, () -> ForgeRegistries.FLUIDS);
		InitializationMarker.expectAndAdvance(0);
		YHBlocks.register();
		YHEffects.register();
		YHEntities.register();
		YHAttributes.register();
		YHSounds.register();
		YHGLMProvider.register();
		YHWorldGen.register();
		YHBiomes.register();
		YHCriteriaTriggers.register();
		KoishiAttackCapability.register();
		FrogGodCapability.register();
		GrazeCapability.register();
		YHModConfig.init();
		TableItemManager.init();
		FilterHolderSet.register();

		AttackEventHandler.register(3943, new YHAttackListener());

		if (ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			TLMRegistries.init();
			MinecraftForge.EVENT_BUS.register(TLMCompat.class);
		}
		if (ModList.get().isLoaded(Gateways.MODID)) {
			MinecraftForge.EVENT_BUS.register(GatewayEventHandlers.class);
		}
	}

	@SubscribeEvent
	public static void registerRecipeSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(SlipBottleIngredient.INSTANCE.id(), SlipBottleIngredient.INSTANCE);
		}
	}

	@SubscribeEvent
	public static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, YHAttributes.INITIAL_POWER.get());
		event.add(EntityType.PLAYER, YHAttributes.INITIAL_RESOURCE.get());
		event.add(EntityType.PLAYER, YHAttributes.MAX_POWER.get());
		event.add(EntityType.PLAYER, YHAttributes.MAX_RESOURCE.get());
		event.add(EntityType.PLAYER, YHAttributes.GRAZE_EFFECTIVENESS.get());
		event.add(EntityType.PLAYER, YHAttributes.HITBOX.get());
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			Terrablender.registerBiomes();
			for (var e : YHCrops.values())
				e.registerComposter();
			ComposterBlock.COMPOSTABLES.put(YHItems.MATCHA, 0.8f);
			ComposterBlock.COMPOSTABLES.put(YHItems.CAMELLIA, 0.8f);

			if (ModList.get().isLoaded(Thirst.ID)) {
				ThirstCompat.init();
			}

			YHEffects.registerBrewingRecipe();

			((ItemAccessor) Items.POTION).setCraftingRemainingItem(Items.GLASS_BOTTLE);

			TouhouSpellCards.registerSpells();

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

			if (ENABLE_TLM && ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
				FairySpellCards.registerSpells();
			}

		});
		FastMapInit.init();
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
		gen.addProvider(server, new SlotGen(gen));
		new YHDamageTypes(output, pvd, helper).generate(server, gen);
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
