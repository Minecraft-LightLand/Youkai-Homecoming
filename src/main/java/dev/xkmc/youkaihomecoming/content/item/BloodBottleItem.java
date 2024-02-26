package dev.xkmc.youkaihomecoming.content.item;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.youkaihomecoming.init.data.YHLangData;
import dev.xkmc.youkaihomecoming.init.food.YHFood;
import dev.xkmc.youkaihomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodBottleItem extends Item {

	public BloodBottleItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		Player player = Proxy.getPlayer();
		if (player == null) return;
		boolean obtain = player.hasEffect(YHEffects.YOUKAIFIED.get()) ||
				player.hasEffect(YHEffects.YOUKAIFYING.get());
		if (this == YHFood.FLESH.item.get()) {
			Component obt;
			if (obtain) {
				var fying = Component.translatable(YHEffects.YOUKAIFYING.get().getDescriptionId());
				var fied = Component.translatable(YHEffects.YOUKAIFIED.get().getDescriptionId());
				obt = YHLangData.OBTAIN_BLOOD.get(fying, fied);
			} else {
				obt = YHLangData.UNKNOWN.get();
			}
			list.add(YHLangData.OBTAIN.get().append(obt));
		}
	}
}
