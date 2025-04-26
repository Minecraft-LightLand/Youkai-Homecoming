package dev.xkmc.youkaishomecoming.content.item.misc;

import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluid;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BloodBottleItem extends SakeBottleItem {

	public BloodBottleItem(Supplier<YHFluid> fluid, Properties pProperties) {
		super(fluid, pProperties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = TouhouHatItem.showTooltip();
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
