package dev.xkmc.youkaishomecoming.content.pot.steamer;

import com.google.gson.JsonObject;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SteamTrigger extends SimpleCriterionTrigger<SteamTrigger.TriggerInstance> {

	static final ResourceLocation ID = YoukaisHomecoming.loc("steam");

	public ResourceLocation getId() {
		return ID;
	}

	public SteamTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate ctx, DeserializationContext des) {
		return new SteamTrigger.TriggerInstance(ctx,
				ItemPredicate.fromJson(json.get("item")),
				MinMaxBounds.Ints.fromJson(json.get("rack")));
	}

	public void trigger(ServerPlayer player, ItemStack stack, int layer) {
		this.trigger(player, (ins) -> ins.matches(stack, layer));
	}

	public static SteamTrigger.TriggerInstance steam(TagKey<Item> tag) {
		return new SteamTrigger.TriggerInstance(ContextAwarePredicate.ANY,
				ItemPredicate.Builder.item().of(tag).build(),
				MinMaxBounds.Ints.ANY);
	}

	public static SteamTrigger.TriggerInstance steam(MinMaxBounds.Ints layer) {
		return new SteamTrigger.TriggerInstance(ContextAwarePredicate.ANY,
				ItemPredicate.ANY, layer);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private final ItemPredicate item;
		private final MinMaxBounds.Ints rack;

		public TriggerInstance(ContextAwarePredicate pPlayer, ItemPredicate pItem, MinMaxBounds.Ints rack) {
			super(SteamTrigger.ID, pPlayer);
			this.item = pItem;
			this.rack = rack;
		}


		public boolean matches(ItemStack pItem, int layer) {
			return this.item.matches(pItem) && rack.matches(layer);
		}

		public JsonObject serializeToJson(SerializationContext pConditions) {
			JsonObject jsonobject = super.serializeToJson(pConditions);
			jsonobject.add("item", this.item.serializeToJson());
			jsonobject.add("rack", this.rack.serializeToJson());
			return jsonobject;
		}

	}

}