package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SerialClass
public class RackItemData {

	@SerialField
	public ItemStack stack = ItemStack.EMPTY;

	@Nullable
	@SerialField
	public ResourceLocation id;

	@SerialField
	public double progress;

	private boolean dirty = true;

	@Nullable
	ServerPlayer lastInteractPlayer;
	public int height = 0;

	@Nullable
	private RecipeHolder<SteamingRecipe> cache = null;

	public boolean tick(SteamerBlockEntity be, Level level, double heat) {
		updateRecipe(level);
		if (cache != null) {
			progress += heat;
			if (progress >= cache.value().getCookingTime()) {
				var cont = new SingleRecipeInput(stack);
				setStack(be, cache.value().assemble(cont, level.registryAccess()));
				id = null;
				cache = null;
				progress = 0;
				if (lastInteractPlayer != null) {
					YHCriteriaTriggers.STEAM.get().trigger(lastInteractPlayer, stack, height);
				}
				lastInteractPlayer = null;
			}
			return true;
		}
		return false;
	}

	public void setStack(SteamerBlockEntity be, ItemStack stack) {
		this.stack = stack;
		be.notifyTile();
		setChanged();
		if (stack.isEmpty())
			lastInteractPlayer = null;
	}

	public void setChanged() {
		dirty = true;
	}

	private void updateRecipe(Level level) {
		if (!dirty) {
			if (id == null) {
				cache = null;
				progress = 0;
				return;
			}
			if (cache != null) return;
		}
		dirty = false;
		Optional<RecipeHolder<SteamingRecipe>> recipe;
		if (stack.isEmpty()) {
			recipe = Optional.empty();
		} else {
			var cont = new SingleRecipeInput(stack);
			recipe = level.getRecipeManager().getRecipeFor(YHBlocks.STEAM_RT.get(), cont, level);
		}
		if (id == null && recipe.isEmpty()) {
			progress = 0;
		} else if (recipe.isEmpty()) {
			id = null;
			progress = 0;
		} else if (!recipe.get().id().equals(id)) {
			id = recipe.get().id();
			progress = 0;
			cache = recipe.get();
		} else {
			cache = recipe.get();
		}
	}

	public boolean mayExtract() {
		return !stack.isEmpty() && id == null && !dirty;
	}

}
