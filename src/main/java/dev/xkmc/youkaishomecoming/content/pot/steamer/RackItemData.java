package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SerialClass
public class RackItemData {

	@SerialClass.SerialField
	public ItemStack stack = ItemStack.EMPTY;

	@Nullable
	@SerialClass.SerialField
	public ResourceLocation id;

	@SerialClass.SerialField
	public double progress;

	private boolean dirty = true;

	@Nullable
	private SteamingRecipe cache = null;

	public boolean tick(SteamerBlockEntity be, Level level, double heat) {
		updateRecipe(level);
		if (cache != null) {
			progress += heat;
			if (progress >= cache.getCookingTime()) {
				var cont = new SimpleContainer(1);
				cont.setItem(0, stack);
				setStack(be, cache.assemble(cont, level.registryAccess()));
				id = null;
				cache = null;
				progress = 0;
			}
			return true;
		}
		return false;
	}

	public void setStack(SteamerBlockEntity be, ItemStack stack) {
		this.stack = stack;
		be.notifyTile();
		setChanged();
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
		Optional<SteamingRecipe> recipe;
		if (stack.isEmpty()) {
			recipe = Optional.empty();
		} else {
			var cont = new SimpleContainer(1);
			cont.setItem(0, stack);
			recipe = level.getRecipeManager().getRecipeFor(YHBlocks.STEAM_RT.get(), cont, level);
		}
		if (id == null && recipe.isEmpty()) {
			progress = 0;
		} else if (recipe.isEmpty()) {
			id = null;
			progress = 0;
		} else if (!recipe.get().getId().equals(id)) {
			id = recipe.get().getId();
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
