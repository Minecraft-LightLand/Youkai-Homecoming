package dev.xkmc.youkaishomecoming.content.pot.cooking.soup;

import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoupHolder {

	private List<SoupBaseRecipe<?>> recipes = new ArrayList<>();

	public SoupData current = SoupData.DEF;
	public SoupData next = SoupData.DEF;
	public List<ItemEntry> floatingItems = new ArrayList<>();

	private int lastTime = 0, nextTime = 0;

	public void recheckSoup(CookingBlockEntity be, Level level) {
		var cont = be.createContainer();
		recipes = level.getRecipeManager().getRecipesFor(YHBlocks.SOUP_RT.get(), cont, level);
		current = SoupData.DEF;
		next = SoupData.DEF;
		floatingItems = new ArrayList<>();
		nextTime = 0;
	}

	public void tickSoup(CookingBlockEntity be, int time) {
		lastTime = time;
		if (time < nextTime) return;
		nextTime = Integer.MAX_VALUE;
		for (var e : recipes) {
			if (e.time > time) {
				nextTime = Math.min(nextTime, e.time);
			}
		}
		var r0 = findSoup(time);
		var r1 = findSoup(nextTime);
		current = r0 == null ? SoupData.DEF : new SoupData(r0.id, r0.color, r0.time);
		next = r1 == null || r0 == r1 ? current : new SoupData(r1.id, r1.color, r1.time);
		var currentItems = new ArrayList<>(be.createContainer().list());
		var nextItems = new ArrayList<>(currentItems);
		if (r0 != null) {
			r0.removeConsumed(currentItems);
			if (r1 != null) {
				r1.removeConsumed(nextItems);
			} else nextItems = currentItems;
		}
		floatingItems = new ArrayList<>();
		for (int i = 0; i < currentItems.size(); i++) {
			if (currentItems.get(i).isEmpty()) {
				floatingItems.add(new ItemEntry(ItemStack.EMPTY, 0));
				continue;
			}
			if (r1 != null && nextItems.get(i).isEmpty()) {
				floatingItems.add(new ItemEntry(currentItems.get(i), r1.time));
			} else floatingItems.add(new ItemEntry(currentItems.get(i), -1));

		}
	}

	private @Nullable SoupBaseRecipe<?> findSoup(int time) {
		int max = 0;
		var current = SoupBaseRecipe.DEF;
		SoupBaseRecipe<?> recipe = null;
		for (var e : recipes) {
			if (e.time > time) continue;
			if (e.getIngredientCount() > max) {
				max = e.getIngredientCount();
				current = e.id;
				recipe = e;
			} else if (e.getIngredientCount() == max) {
				if (e.id.compareTo(current) < 0) {
					current = e.id;
					recipe = e;
				}
			}
		}
		return recipe;
	}

	public float getProgress(float pTick) {
		return (lastTime + pTick - current.time) / (next.time - current.time);
	}

	public SoupData getCurrentLayer(float pTick) {
		if (current == next || current.time == next.time) return current;
		float time = getProgress(pTick);
		if (time < 0.5f || time > 1)
			return current;
		int rgb = current.color & 0xFFFFFF;
		int alpha = (current.color >> 24) & 0xFF;
		int an = Math.min(alpha, (int) (alpha * (2 - time * 2)));
		return new SoupData(current.id, an << 24 | rgb, 0);
	}

	@Nullable
	public SoupData getNextLayer(float pTick) {
		if (current == next || current.time == next.time) return null;
		float time = getProgress(pTick);
		if (time > 0.5f || time < 0)
			return next;
		int rgb = next.color & 0xFFFFFF;
		int alpha = (next.color >> 24) & 0xFF;
		int an = Math.min(alpha, (int) (alpha * time * 2));
		return new SoupData(next.id, an << 24 | rgb, 0);
	}

	public record SoupData(ResourceLocation id, int color, int time) {
		public static final SoupData DEF = new SoupData(SoupBaseRecipe.DEF, -1, 0);
	}

	public record ItemEntry(ItemStack stack, int life) {

		public float getAmount(float time) {
			if (stack.isEmpty()) return 0;
			return life < 0 ? 1 : 1 - time;
		}
	}


}
