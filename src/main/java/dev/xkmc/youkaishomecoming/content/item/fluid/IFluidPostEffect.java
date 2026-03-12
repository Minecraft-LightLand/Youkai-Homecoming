package dev.xkmc.youkaishomecoming.content.item.fluid;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IFluidPostEffect {

	void onConsumeSlip(LivingEntity user);

	@Nullable
	FoodProperties buildFoodProperties();

	default void appendHoverText(List<Component> list) {

	}

}
