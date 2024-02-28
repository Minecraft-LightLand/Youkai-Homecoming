package dev.xkmc.youkaishomecoming.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {

	@Mutable
	@Accessor
	void setCraftingRemainingItem(Item craftingRemainingItem);
}
