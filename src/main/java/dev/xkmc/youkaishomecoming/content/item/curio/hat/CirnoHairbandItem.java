package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.content.client.CirnoHairbandModel;
import dev.xkmc.youkaishomecoming.content.entity.fairy.CirnoModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CirnoHairbandItem extends TouhouHatItem {

	public CirnoHairbandItem(Properties properties) {
		super(properties, TouhouMat.CIRNO_HAIRBAND);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new CirnoHairbandModel(CirnoModel.HAT_LOCATION));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/entities/cirno.png";
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.FAIRY.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}

	@Override
	public void onHurtTarget(ItemStack head, DamageSource source, LivingEntity target) {
		if (source.is(L2DamageTypes.MAGIC)) {
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
			if (target.canFreeze()) {
				target.setTicksFrozen(target.getTicksFrozen() + 120);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = showTooltip();
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_CIRNO_HAIRBAND.get()));
			list.add(YHLangData.USAGE.get());
			list.add(YHLangData.USAGE_CIRNO_HAIRBAND.get());
			list.add(effectDesc(YHEffects.FAIRY.get()));
			list.add(supportDesc(DyeColor.LIGHT_BLUE));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.LIGHT_BLUE;
	}

}
