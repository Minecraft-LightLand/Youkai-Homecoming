package dev.xkmc.youkaihomecoming.content.item;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.youkaihomecoming.init.data.YHLangData;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
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
		Player player = Proxy.getPlayer();
		Component name;
		if (player != null && player.hasEffect(YHEffects.YOUKAIFIED.get())) {
			name = YHLangData.FLESH_NAME_YOUKAI.get();
		} else {
			name = YHLangData.FLESH_NAME_HUMAN.get();
		}
		return Component.translatable(this.getDescriptionId(pStack), name);
	}

}
