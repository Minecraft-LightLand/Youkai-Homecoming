package dev.xkmc.youkaishomecoming.content.pot.base;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public abstract class TimedRecipeBlockEntity<
		T extends BaseRecipe<?, T, C> & TimedRecipe,
		C extends RecipeInput
		>
		extends BaseBlockEntity implements TickableBlockEntity, BaseContainerListener {

	@SerialField
	protected int totalTime = 0, recipeProgress = 0;
	@SerialField
	protected ResourceLocation recipeId = null;
	private boolean doRecipeSearch = true;
	private RecipeHolder<T> recipe = null;

	public TimedRecipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	protected abstract RecipeType<T> getRecipeType();

	protected abstract boolean isEmpty();

	protected abstract boolean shouldStopProcessing(Level level);

	protected abstract C createContainer();

	protected abstract void finishRecipe(Level level, T recipe);

	protected void animationTick(Level level) {
	}

	public void notifyTile() {
		setChanged();
		sync();
		doRecipeSearch = true;
	}

	public float inProgress() {
		return totalTime == 0 ? 0 : Mth.clamp(1f * recipeProgress / totalTime, 0, 1);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			if (totalTime > 0) {
				if (shouldStopProcessing(level)) {
					if (recipeProgress > 0) {
						recipeProgress--;
					}
				} else {
					recipeProgress++;
					animationTick(level);
				}
			}
			return;
		}
		if (doRecipeSearch) {
			// find recipes
			if (!isEmpty()) {
				var opt = level.getRecipeManager().getRecipeFor(getRecipeType(), createContainer(), level);
				if (opt.isPresent()) {
					recipe = opt.get();
					totalTime = recipe.value().getProcessTime();
					if (!recipe.id().equals(recipeId)) {
						recipeProgress = 0;
						recipeId = recipe.id();
					} else if (recipeProgress > totalTime) {
						recipeProgress = totalTime - 1;
					}
				} else {
					recipeId = null;
					recipe = null;
					totalTime = 0;
					recipeProgress = 0;
				}
				sync();
			}
			doRecipeSearch = false;
		}
		if (totalTime > 0) {
			if (shouldStopProcessing(level)) {
				if (recipeProgress > 0) {
					recipeProgress--;
				}
			} else {
				recipeProgress++;
			}
			if (recipeProgress >= totalTime) {
				if (recipe != null) {
					finishRecipe(level, recipe.value());
				}
				recipeProgress = 0;
				totalTime = 0;
				recipeId = null;
				recipe = null;
			}
		}
	}

}
