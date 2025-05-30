package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.youkaishomecoming.content.entity.danmaku.IYHDanmaku;
import dev.xkmc.youkaishomecoming.content.item.food.IFleshFoodItem;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class TouhouHatItem extends ArmorItem {

	public static boolean showTooltip() {
		Player player = IFleshFoodItem.getPlayer();
		if (player == null) return false;
		if (player.getAbilities().instabuild)
			return true;
		return EffectEventHandlers.isCharacter(player);
	}

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public TouhouHatItem(Properties properties, TouhouMat mat) {
		super(mat, Type.HELMET, properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(super.getDefaultAttributeModifiers(type.getSlot()));
		addModifiers(builder);
		defaultModifiers = builder.build();
	}

	protected void addModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
	}

	public Multimap<Attribute, AttributeModifier> getAttributeModifiersForDisplay() {
		return defaultModifiers;
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		return pEquipmentSlot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return Math.min(amount, 1);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		tick(stack, level, player);
	}

	protected void tick(ItemStack stack, Level level, Player player) {
	}

	public boolean support(DyeColor color) {
		return false;
	}

	public DamageSource modifyDamageType(ItemStack stack, LivingEntity le, IYHDanmaku danmaku, DamageSource type) {
		return type;
	}

	public void onHurtTarget(ItemStack head, DamageSource source, LivingEntity target) {
	}

	protected static Component supportDesc(DyeColor col) {
		return YHLangData.DANMAKU_SUPPORT_1.get(
				Component.translatable(YoukaisHomecoming.MODID + ".custom_spell.color." + col.getName())
		);
	}

	protected static Component supportDesc(DyeColor a, DyeColor b) {
		return YHLangData.DANMAKU_SUPPORT_2.get(
				Component.translatable(YoukaisHomecoming.MODID + ".custom_spell.color." + a.getName()),
				Component.translatable(YoukaisHomecoming.MODID + ".custom_spell.color." + b.getName())
		);
	}

	protected static Component effectDesc(MobEffect eff) {
		return YHLangData.CONSTANT_EFFECT.get(Component.translatable(eff.getDescriptionId()));
	}

}
