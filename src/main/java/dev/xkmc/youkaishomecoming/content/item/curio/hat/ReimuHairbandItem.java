package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.content.client.ReimuHairbandModel;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.entity.reimu.ReimuModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ReimuHairbandItem extends TouhouHatItem {

	public ReimuHairbandItem(Properties properties) {
		super(properties, TouhouMat.REIMU_HAIRBAND);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new ReimuHairbandModel(ReimuModel.HAT_LOCATION));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/entities/reimu.png";
	}

	@Override
	protected void addModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
		super.addModifiers(builder);
		var ID = MathHelper.getUUIDFromString("reimu_hairband");
		builder.put(YHAttributes.HITBOX.get(), new AttributeModifier(ID, "Reimu Hairband", -0.1, AttributeModifier.Operation.ADDITION));
		builder.put(YHAttributes.INITIAL_RESOURCE.get(), new AttributeModifier(ID, "Reimu Hairband", 1, AttributeModifier.Operation.ADDITION));
	}

	@Override
	public DamageSource modifyDamageType(ItemStack stack, LivingEntity le, IYHDanmaku danmaku, DamageSource type) {
		return YHDamageTypes.abyssal(danmaku);
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		FlyingToken.tickFlying(player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = showTooltip();
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_REIMU_HAIRBAND.get()));
			list.add(YHLangData.USAGE.get());
			list.add(YHLangData.USAGE_REIMU_HAIRBAND.get());
			list.add(supportDesc(DyeColor.RED));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

	@Override
	public boolean support(DyeColor color) {
		return color == DyeColor.RED;
	}

}
