package dev.xkmc.youkaishomecoming.content.item.food;

import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FleshBlockItem extends BlockItem {

	public FleshBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
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
