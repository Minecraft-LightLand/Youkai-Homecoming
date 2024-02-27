package dev.xkmc.youkaihomecoming.content.pot;

import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class MokaMenu extends BasePotMenu {

	public MokaMenu(MenuType<MokaMenu> menu, int id, Inventory inv, @Nullable FriendlyByteBuf data) {
		super(menu, id, inv, data);
	}

	public MokaMenu(MenuType<MokaMenu> menu, int id, Inventory inv, BasePotBlockEntity be, ContainerData data) {
		super(menu, id, inv, be, data);
	}

	@Override
	public Block getBlock() {
		return YHBlocks.MOKA.get();
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return YoukaiHomecoming.MOKA;
	}

}
