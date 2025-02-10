package dev.xkmc.youkaishomecoming.content.spell.shooter;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class BaseHealthEntity extends LivingEntity {

	protected BaseHealthEntity(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return List.of();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

}
