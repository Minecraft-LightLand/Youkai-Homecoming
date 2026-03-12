package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.Nullable;

public interface IFluidPostEffect {

	void onConsumeSlip(LivingEntity user);

	@Nullable
	FoodProperties buildFoodProperties();

}
