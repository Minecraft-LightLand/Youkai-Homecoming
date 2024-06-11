package dev.xkmc.youkaishomecoming.content.item.curio;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Locale;
import java.util.function.Supplier;

public enum TouhouMat implements ArmorMaterial {
	SUWAKO_HAT(60, 10, 3, 3, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	STRAW_HAT(10, 1, 1, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	KOISHI_HAT(60, 10, 5, 3, () -> Ingredient.of(Items.IRON_INGOT), SoundEvents.ARMOR_EQUIP_IRON),
	RUMIA_HAIRBAND(60, 10, 1, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	;

	private static final int[] DURABILITY = {13, 15, 16, 11};
	private static final int[] DEFENSE = {0, 2, 3, 1};

	private final int durability, enchantment, defense, tough;
	private final Lazy<Ingredient> repair;
	private final SoundEvent sound;

	TouhouMat(int durability, int enchantment, int defense, int tough, Supplier<Ingredient> repair, SoundEvent sound) {
		this.durability = durability;
		this.enchantment = enchantment;
		this.defense = defense;
		this.tough = tough;
		this.repair = Lazy.of(repair);
		this.sound = sound;
	}

	public int getDurabilityForType(ArmorItem.Type type) {
		return DURABILITY[type.getSlot().getIndex()] * durability;
	}

	public int getDefenseForType(ArmorItem.Type type) {
		return DEFENSE[type.getSlot().getIndex()] + defense;
	}

	public int getEnchantmentValue() {
		return enchantment;
	}

	public SoundEvent getEquipSound() {
		return sound;
	}

	public Ingredient getRepairIngredient() {
		return repair.get();
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public float getToughness() {
		return tough;
	}

	public float getKnockbackResistance() {
		return 0.0F;
	}

}
