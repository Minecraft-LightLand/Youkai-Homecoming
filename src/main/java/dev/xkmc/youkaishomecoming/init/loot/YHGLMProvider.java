package dev.xkmc.youkaishomecoming.init.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import dev.xkmc.youkaishomecoming.mixin.AddLootTableModifierAccessor;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.tag.ModTags;

public class YHGLMProvider extends GlobalLootModifierProvider {

	public static final RegistryEntry<Codec<ReplaceItemModifier>> REPLACE_ITEM;
	public static final RegistryEntry<Codec<RemoveItemModifier>> REMOVE_ITEM;
	public static final RegistryEntry<LootItemConditionType> BIOME_CHECK;


	static {
		REPLACE_ITEM = YoukaisHomecoming.REGISTRATE.simple("replace_item",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ReplaceItemModifier.CODEC);
		REMOVE_ITEM = YoukaisHomecoming.REGISTRATE.simple("remove_item",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> RemoveItemModifier.CODEC);
		BIOME_CHECK = YoukaisHomecoming.REGISTRATE.simple("biome_check",
				Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new Serializer<BiomeTagCondition>() {

					@Override
					public void serialize(JsonObject json, BiomeTagCondition cond, JsonSerializationContext ctx) {
						json.addProperty("tag", cond.tag.location().toString());
					}

					@Override
					public BiomeTagCondition deserialize(JsonObject json, JsonDeserializationContext ctx) {
						return new BiomeTagCondition(TagKey.create(Registries.BIOME, ResourceLocation.tryParse(json.get("tag").getAsString())));
					}

				}));

	}

	public static void register() {

	}

	public YHGLMProvider(DataGenerator gen) {
		super(gen.getPackOutput(), YoukaisHomecoming.MODID);
	}

	@Override
	protected void start() {

		add("tuna_hunt", new RemoveItemModifier(
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.KILLER,
						EntityPredicate.Builder.entity().entityType(
								EntityTypePredicate.of(YHEntities.TUNA.get()))).build()
		));

		add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING).build(),
				new BiomeTagCondition(YHBiomeTagsProvider.LAMPREY)
		));
		add("fishing_crab", new ReplaceItemModifier(0.1f, YHFood.CRAB.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING).build(),
				new BiomeTagCondition(YHBiomeTagsProvider.CRAB_FISHING)
		));

		add("udumbara_ancient_city_loot", loot(YHLootGen.UDUMBARA_LOOT,
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY).build()));
		add("udumbara_ancient_city_ice_box_loot", loot(YHLootGen.UDUMBARA_LOOT,
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX).build()));
	}

	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return AddLootTableModifierAccessor.createAddLootTableModifier(cond, id);
	}

	private static LootItemCondition killer(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.KILLER,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}


	private static LootItemCondition entity(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}

	private static LootItemCondition frog(FrogVariant type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().of(EntityType.FROG)
						.subPredicate(EntitySubPredicate.variant(type))
		).build();
	}

	private static LootItemCondition entity(TagKey<EntityType<?>> tag) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(tag))).build();
	}

	public static LootItemCondition killedByKnife() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.KILLER,
				EntityPredicate.Builder.entity().equipment(
						EntityEquipmentPredicate.Builder.equipment().mainhand(
										ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
								.build()).build()).build();
	}

	private static LootItemCondition fire(boolean fire) {
		return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().flags(
						EntityFlagsPredicate.Builder.flags().setOnFire(fire)
								.build()).build()).build();
	}

	private static AddItemModifier create(Item item, int count, LootItemCondition... conditions) {
		var ans = AddItemModifierAccessor.create(conditions, item, count);
		assert ans != null;
		return ans;
	}

	private static <T> EntryHolder<T> vanilla(T obj, String id) {
		return new EntryHolder<>(obj, new ResourceLocation(id));
	}

	private static <T> EntryHolder<T> of(RegistryObject<T> obj) {
		return new EntryHolder<>(obj.get(), obj.getId());
	}

	private static <T> EntryHolder<T> of(RegistryEntry<T> obj) {
		return new EntryHolder<>(obj.get(), obj.getId());
	}

	private record EntryHolder<T>(T type, ResourceLocation id) {

	}

}
