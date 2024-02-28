package dev.xkmc.youkaihomecoming.mixin;

import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.loot.function.CopyMealFunction;

@Mixin(CopyMealFunction.class)
public class CopyMealFunctionMixin {

	@Inject(at = @At("HEAD"), method = "run", cancellable = true)
	public void run(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		BlockEntity tile = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
		if (tile instanceof BasePotBlockEntity be) {
			CompoundTag tag = be.writeMeal(new CompoundTag());
			if (!tag.isEmpty()) {
				stack.addTagElement("BlockEntityTag", tag);
			}
			cir.setReturnValue(stack);
		}
	}

}
