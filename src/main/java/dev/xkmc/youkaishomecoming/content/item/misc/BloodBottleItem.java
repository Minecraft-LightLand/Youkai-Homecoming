package dev.xkmc.youkaishomecoming.content.item.misc;

import dev.xkmc.youkaishomecoming.content.item.curio.hat.TouhouHatItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BloodBottleItem extends SakeBottleItem {

	public BloodBottleItem(Supplier<SakeFluid> fluid, Properties pProperties) {
		super(fluid, pProperties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = TouhouHatItem.showTooltip();
		Component obt;
		if (obtain) {
			obt = YHLangData.OBTAIN_BLOOD.get();
		} else {
			obt = YHLangData.UNKNOWN.get();
		}
		list.add(YHLangData.OBTAIN.get().append(obt));
	}
}
