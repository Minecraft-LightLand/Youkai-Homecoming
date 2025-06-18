package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class RemoveItemModifier extends LootModifier {

	public static final Codec<RemoveItemModifier> CODEC = RecordCodecBuilder.create(
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
	public Codec<? extends IGlobalLootModifier> codec() {
		return YHGLMProvider.REMOVE_ITEM.get();
	}

}
