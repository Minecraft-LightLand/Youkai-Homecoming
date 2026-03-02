package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ReplaceItemModifier extends LootModifier {

	public static final MapCodec<ReplaceItemModifier> CODEC = RecordCodecBuilder.mapCodec(i -> codecStart(i).and(i.group(
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result)))
			.apply(i, ReplaceItemModifier::new));

	public final double chance;
	public final ItemStack result;

	public ReplaceItemModifier(double chance, ItemStack result, LootItemCondition... conditionsIn) {
		super(conditionsIn);
		this.chance = chance;
		this.result = result;
	}

	private ReplaceItemModifier(LootItemCondition[] conditionsIn, double chance, ItemStack result) {
		super(conditionsIn);
		this.chance = chance;
		this.result = result;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (context.getRandom().nextDouble() < chance) {
			list.clear();
			list.add(result.copy());
		}
		return list;
	}

	@Override
	public MapCodec<ReplaceItemModifier> codec() {
		return CODEC;
	}

}
