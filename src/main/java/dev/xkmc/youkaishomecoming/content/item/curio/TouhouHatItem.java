package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.youkaishomecoming.content.item.food.FleshFoodItem;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TouhouHatItem extends ArmorItem {

	public static boolean showTooltip() {
		Player player = FleshFoodItem.getPlayer();
		if (player == null) return false;
		if (player.getAbilities().instabuild)
			return true;
		if (player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			return true;
		} else return player.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	public TouhouHatItem(Properties properties, TouhouMat mat) {
		super(mat, Type.HELMET, properties);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		tick(stack, level, player);
	}

	protected void tick(ItemStack stack, Level level, Player player) {
	}

	public boolean support(DyeColor color) {
		return false;
	}

}
