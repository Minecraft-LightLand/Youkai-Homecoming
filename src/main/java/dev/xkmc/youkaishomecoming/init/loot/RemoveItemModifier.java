package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class RemoveItemModifier extends LootModifier {

	public static final MapCodec<RemoveItemModifier> CODEC = RecordCodecBuilder.mapCodec(
			i -> codecStart(i).apply(i, RemoveItemModifier::new));

	protected RemoveItemModifier(LootItemCondition... conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		list.clear();
		return list;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return YHGLMProvider.REMOVE_ITEM.get();
	}

}
