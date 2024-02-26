package dev.xkmc.youkaihomecoming.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;

@Mixin(AddItemModifier.class)
public interface AddItemModifierAccessor {

	@Invoker("<init>")
	static AddItemModifier create(LootItemCondition[] conditions, Item item, int count) {
		return null;
	}

}
