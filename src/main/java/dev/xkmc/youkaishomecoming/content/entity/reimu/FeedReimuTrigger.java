package dev.xkmc.youkaishomecoming.content.entity.reimu;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class FeedReimuTrigger extends SimpleCriterionTrigger<FeedReimuTrigger.TriggerInstance> {

	static final ResourceLocation ID = YoukaisHomecoming.loc("feed_reimu");

	public ResourceLocation getId() {
		return ID;
	}

	public FeedReimuTrigger.TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pDeserializationContext) {
		return new FeedReimuTrigger.TriggerInstance(pPredicate, ItemPredicate.fromJson(pJson.get("item")));
	}

	public void trigger(ServerPlayer pPlayer, ItemStack pItem) {
		this.trigger(pPlayer, (p_23687_) -> p_23687_.matches(pItem));
	}

	public static FeedReimuTrigger.TriggerInstance usedItem(ItemLike pItem) {
		return new FeedReimuTrigger.TriggerInstance(ContextAwarePredicate.ANY, new ItemPredicate(null, ImmutableSet.of(pItem.asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private final ItemPredicate item;

		public TriggerInstance(ContextAwarePredicate pPlayer, ItemPredicate pItem) {
			super(FeedReimuTrigger.ID, pPlayer);
			this.item = pItem;
		}


		public boolean matches(ItemStack pItem) {
			return this.item.matches(pItem);
		}

		public JsonObject serializeToJson(SerializationContext pConditions) {
			JsonObject jsonobject = super.serializeToJson(pConditions);
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}
}