package dev.xkmc.youkaishomecoming.content.item.danmaku;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ISpellItem {

	boolean castSpell(ItemStack stack, Player player, boolean consume, boolean cooldown);

}
