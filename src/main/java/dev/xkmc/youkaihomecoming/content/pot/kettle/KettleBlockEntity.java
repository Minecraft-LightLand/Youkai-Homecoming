package dev.xkmc.youkaihomecoming.content.pot.kettle;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotRecipe;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BasePotBlockEntity {

	public KettleBlockEntity(BlockEntityType<KettleBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
		return new KettleMenu(YHBlocks.KETTLE_MT.get(), id, player, this, cookingPotData);
	}

	@Override
	public RecipeType<? extends BasePotRecipe> getRecipeType() {
		return YHBlocks.KETTLE_RT.get();
	}

}