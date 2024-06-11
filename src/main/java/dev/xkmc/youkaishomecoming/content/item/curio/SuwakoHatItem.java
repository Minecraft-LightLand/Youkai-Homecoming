package dev.xkmc.youkaishomecoming.content.item.curio;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.content.client.HatModel;
import dev.xkmc.youkaishomecoming.content.client.SuwakoHatModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SuwakoHatItem extends TouhouHatItem {

	private static final UUID ID = MathHelper.getUUIDFromString("suwako_hat");

	public SuwakoHatItem(Item.Properties properties) {
		super(properties, TouhouMat.SUWAKO_HAT);
	}

	@Override
	protected void addModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
		builder.put(L2DamageTracker.MAGIC_FACTOR.get(), new AttributeModifier(ID, "suwako_hat", 0.25, AttributeModifier.Operation.ADDITION));
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new HatModel(SuwakoHatModel.SUWAKO));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/models/straw_hat.png";
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.NATIVE.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = showTooltip();
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_SUWAKO_HAT.get(Component.literal("" + YHModConfig.COMMON.frogEatCountForHat.get()))));
			list.add(YHLangData.USAGE.get().append(YHLangData.USAGE_SUWAKO_HAT.get(Component.translatable(YHEffects.NATIVE.get().getDescriptionId()))));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.CYAN || color == DyeColor.LIME;
	}


}
