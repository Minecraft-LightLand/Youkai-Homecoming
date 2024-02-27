package dev.xkmc.youkaihomecoming.content.pot.kettle;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotMenu;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class KettleMenu extends BasePotMenu {

	public KettleMenu(MenuType<KettleMenu> menu, int windowId, Inventory playerInventory, @Nullable FriendlyByteBuf data) {
		super(menu, windowId, playerInventory, data);
	}

	public KettleMenu(MenuType<KettleMenu> menu, int id, Inventory inv, BasePotBlockEntity be, ContainerData data) {
		super(menu, id, inv, be, data);
	}

	@Override
	public Block getBlock() {
		return YHBlocks.KETTLE.get();
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return YoukaiHomecoming.KETTLE;
	}

}
