package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FleshSimpleItem extends Item {

	public FleshSimpleItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public Component getName(ItemStack pStack) {
		Player player = FleshFoodItem.getPlayer();
		Component name;
		if (player != null && PlayerStatusData.Status.YOUKAIFIED.is(player)) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(this.getDescriptionId(pStack), name);
	}

}
