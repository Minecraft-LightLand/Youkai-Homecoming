package dev.xkmc.youkaishomecoming.content.pot.moka;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MokaMakerBlockEntity extends BasePotBlockEntity {

	public MokaMakerBlockEntity(BlockEntityType<MokaMakerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
		return new MokaMenu(CoffeeBlocks.MOKA_MT.get(), id, player, this, this);
	}

	@Override
	public RecipeType<? extends BasePotRecipe> getRecipeType() {
		return CoffeeBlocks.MOKA_RT.get();
	}

}
