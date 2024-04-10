package dev.xkmc.youkaishomecoming.content.item.curio;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TouhouHatItem extends ArmorItem {

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
