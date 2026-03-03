package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class SteamTrigger extends SimpleCriterionTrigger<SteamTrigger.TriggerInstance> {

	public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
			EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			ItemPredicate.CODEC.fieldOf("item").forGetter(TriggerInstance::item),
			MinMaxBounds.Ints.CODEC.fieldOf("rack").forGetter(TriggerInstance::rack)
	).apply(i, TriggerInstance::new));

	@Override
	public Codec<TriggerInstance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack, int layer) {
		this.trigger(player, (ins) -> ins.matches(stack, layer));
	}

	public Criterion<SteamTrigger.TriggerInstance> steam(TagKey<Item> tag) {
		return new Criterion<>(this, new SteamTrigger.TriggerInstance(Optional.empty(),
				ItemPredicate.Builder.item().of(tag).build(),
				MinMaxBounds.Ints.ANY));
	}

	public Criterion<SteamTrigger.TriggerInstance> steam(MinMaxBounds.Ints layer) {
		return new Criterion<>(this, new SteamTrigger.TriggerInstance(Optional.empty(), ItemPredicate.Builder.item().build(), layer));
	}

	public record TriggerInstance(
			Optional<ContextAwarePredicate> player, ItemPredicate item, MinMaxBounds.Ints rack
	) implements SimpleInstance {

		public boolean matches(ItemStack pItem, int layer) {
			return this.item.test(pItem) && rack.matches(layer);
		}
	}

}