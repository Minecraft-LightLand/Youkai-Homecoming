package dev.xkmc.youkaishomecoming.content.pot.steamer;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.item.food.FoodSaucerItem;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class RackData {

	public static final double UPWARD_EFF = 0.01, DOWNWARD_EFF = 0.05;

	@SerialClass.SerialField
	public final RackItemData[] list = new RackItemData[4];

	@SerialClass.SerialField
	public double upwardHeat = 0;

	@SerialClass.SerialField
	public double downwardHeat = 0;

	public void tick(SteamerBlockEntity be, Level level) {
		int count = 0;
		for (var e : list) {
			double heat = upwardHeat * UPWARD_EFF + downwardHeat * DOWNWARD_EFF;
			if (e != null && e.tick(be, level, heat)) {
				count++;
			}
		}
		upwardHeat -= count * UPWARD_EFF;
		downwardHeat -= count * DOWNWARD_EFF;
	}

	public void popItems(Level level, BlockPos pos, int height) {
		if (level.isClientSide()) return;
		for (int i = 0; i < 4; i++) {
			var item = list[i];
			if (item == null || item.stack.isEmpty()) continue;
			popResource(level, pos.above(), item.stack, height * 0.25);
			list[i] = null;
		}
	}

	public boolean tryAddItem(SteamerBlockEntity be, Level level, ItemStack stack) {
		for (int i = 0; i < 4; i++) {
			if (tryAddItemAt(be, level, stack, i))
				return true;
		}
		return false;
	}

	public boolean tryAddItemAt(SteamerBlockEntity be, Level level, ItemStack stack, Vec3 hit) {
		return tryAddItemAt(be, level, stack, getIndex(hit));
	}

	protected boolean tryAddItemAt(SteamerBlockEntity be, Level level, ItemStack stack, int index) {
		if (stack.getItem() instanceof FoodSaucerItem) {
			if (index != 0) return false;
			for (int i = 0; i < 4; i++) {
				if (list[index] != null && !list[index].stack.isEmpty())
					return false;
			}
		}
		if (index < 0 || index >= 4) return false;
		var item = list[index];
		if (item != null && !item.stack.isEmpty()) return false;
		if (item == null) list[index] = item = new RackItemData();
		if (!item.stack.isEmpty()) return false;
		if (!level.isClientSide()) {
			item.setStack(be, stack.split(1));
		}
		return true;
	}

	public boolean tryTakeItem(SteamerBlockEntity be, Level level, Player player, InteractionHand hand) {
		for (int i = 3; i >= 0; i--) {
			if (tryTakeItemAt(be, level, i, player, hand))
				return true;
		}
		return false;
	}

	public boolean tryTakeItemAt(SteamerBlockEntity be, Level level, Vec3 hit, Player player, InteractionHand hand) {
		return tryTakeItemAt(be, level, getIndex(hit), player, hand);
	}

	protected boolean tryTakeItemAt(SteamerBlockEntity be, Level level, int index, Player player, InteractionHand hand) {
		if (index < 0 || index >= 4) return false;
		var item = list[index];
		if (item == null || item.stack.isEmpty()) return false;
		if (!level.isClientSide()) {
			player.getInventory().placeItemBackInInventory(item.stack);
			item.setStack(be, ItemStack.EMPTY);
		}
		return true;
	}

	public static boolean isValid(Level level, ItemStack stack) {
		if (stack.getFoodProperties(null) != null) return true;
		var cont = new SimpleContainer(1);
		cont.setItem(0, stack);
		return level.getRecipeManager().getRecipeFor(YHBlocks.STEAM_RT.get(), cont, level).isPresent();
	}

	private static int getIndex(Vec3 hit) {
		boolean sx = Mth.positiveModulo(hit.x, 1) > 0.5;
		boolean sz = Mth.positiveModulo(hit.z, 1) > 0.5;
		return sx ? sz ? 2 : 1 : sz ? 3 : 0;
	}

	private static void popResource(Level level, BlockPos pos, ItemStack stack, double height) {
		double x = pos.getX() + 0.5 + Mth.nextDouble(level.random, -0.1, 0.1);
		double h = pos.getY() + height;
		double z = pos.getZ() + 0.5 + Mth.nextDouble(level.random, -0.1, 0.1);
		if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
			ItemEntity itementity = new ItemEntity(level, x, h, z, stack);
			itementity.setDefaultPickUpDelay();
			level.addFreshEntity(itementity);
		}
	}

}
