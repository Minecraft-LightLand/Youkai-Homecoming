package dev.xkmc.youkaishomecoming.events;

import dev.xkmc.youkaishomecoming.compat.curios.CuriosManager;
import dev.xkmc.youkaishomecoming.content.block.furniture.LeftClickBlock;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluidWrapper;
import dev.xkmc.youkaishomecoming.content.item.fluid.SlipFluidWrapper;
import dev.xkmc.youkaishomecoming.content.pot.base.FluidItemTile;
import dev.xkmc.youkaishomecoming.content.pot.basin.BasinBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.ferment.FermentationTankBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.steamer.SteamerBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.tank.CopperTankBlockEntity;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = YoukaisHomecoming.MODID)
public class GeneralEventHandlers {

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.FluidHandler.ITEM, SakeFluidWrapper::new, Items.GLASS_BOTTLE, Items.BOWL);
		event.registerItem(Capabilities.FluidHandler.ITEM, SakeFluidWrapper::new, SakeFluidWrapper.LIST.toArray(Item[]::new));
		event.registerItem(Capabilities.FluidHandler.ITEM, SlipFluidWrapper::new, SlipFluidWrapper.LIST.toArray(Item[]::new));
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.TANK_BE.get(), CopperTankBlockEntity::getFluidCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.STEAMER_BE.get(), SteamerBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.BASIN_BE.get(), BasinBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.BASIN_BE.get(), FluidItemTile::getFluidCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FermentationTankBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.FERMENT_BE.get(), FluidItemTile::getFluidCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.KETTLE_BE.get(), KettleBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, YHBlocks.KETTLE_BE.get(), FluidItemTile::getFluidCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.SMALL_POT_BE.get(), CookingBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.MID_POT_BE.get(), CookingBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.LARGE_POT_BE.get(), CookingBlockEntity::getItemCap);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, YHBlocks.RACK_BE.get(), DryingRackBlockEntity::getItemCap);

	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getItemStack().is(Items.DEBUG_STICK)) return;
		var level = event.getLevel();
		var pos = event.getPos();
		var state = level.getBlockState(pos);
		LeftClickBlock block;
		if (state.getBlock() instanceof LeftClickBlock b)
			block = b;
		else if (level.getBlockEntity(pos) instanceof LeftClickBlock b)
			block = b;
		else return;
		if (block.leftClick(state, level, pos, event.getEntity())) {
			event.setCanceled(true);
		}
	}

	public static boolean preventPhantomSpawn(ServerPlayer player) {
		return player.hasEffect(YHEffects.SOBER) || CuriosManager.hasHead(player, YHItems.CAMELLIA.get(), false);
	}

	public static boolean supressVibration(Entity self) {
		if (self instanceof TraceableEntity item) {
			if (item.getOwner() instanceof LivingEntity le) {
				self = le;
			}
		}
		if (self instanceof LivingEntity le) {
			return le.hasEffect(YHEffects.UDUMBARA);
		}
		return false;
	}

}
