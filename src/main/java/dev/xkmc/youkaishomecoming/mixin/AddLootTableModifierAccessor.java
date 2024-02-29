package dev.xkmc.youkaishomecoming.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;

@Mixin(AddLootTableModifier.class)
public interface AddLootTableModifierAccessor {

	@Invoker("<init>")
	static AddLootTableModifier createAddLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
		throw new UnsupportedOperationException();
	}

}
