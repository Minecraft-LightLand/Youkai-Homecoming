package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.content.client.HatModel;
import dev.xkmc.youkaishomecoming.content.client.KoishiHatModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class KoishiHatItem extends TouhouHatItem {

	public KoishiHatItem(Properties properties) {
		super(properties, TouhouMat.KOISHI_HAT);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new HatModel(KoishiHatModel.HAT));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/models/koishi_hat.png";
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		if (player.getCooldowns().isOnCooldown(this)) return;
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.UNCONSCIOUS.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = showTooltip();
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_KOISHI_HAT.get(Component.literal("" + YHModConfig.COMMON.koishiAttackBlockCount.get()))));
			list.add(YHLangData.USAGE.get().append(YHLangData.USAGE_KOISHI_HAT.get(Component.translatable(YHEffects.UNCONSCIOUS.get().getDescriptionId()))));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.RED || color == DyeColor.BLUE;
	}

}
