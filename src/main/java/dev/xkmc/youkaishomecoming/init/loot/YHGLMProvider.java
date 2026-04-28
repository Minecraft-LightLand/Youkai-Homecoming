package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.TagRef;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class YHGLMProvider extends GlobalLootModifierProvider implements RegistrateProvider {

	public static final ProviderType<YHGLMProvider> TYPE = ProviderType.registerServerData("yh_glm", YHGLMProvider::new);

	public static final CdcVal<ReplaceItemModifier> REPLACE_ITEM;
	public static final CdcVal<RemoveItemModifier> REMOVE_ITEM;
	public static final Val<LootItemConditionType> BIOME_CHECK;

	static {
		var CR = CdcReg.of(YoukaisHomecoming.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
		REPLACE_ITEM = CR.reg("replace_item", ReplaceItemModifier.CODEC);
		REMOVE_ITEM = CR.reg("remove_item", RemoveItemModifier.CODEC);
		BIOME_CHECK = SR.of(YoukaisHomecoming.REG, Registries.LOOT_CONDITION_TYPE)
				.reg("biome_check", () -> new LootItemConditionType(BiomeTagCondition.CODEC));

	}

	public static void register() {

	}

	public YHGLMProvider(AbstractRegistrate<?> parent, PackOutput out, CompletableFuture<HolderLookup.Provider> pvd) {
		super(out, pvd, parent.getModid());
	}

	public void gen() {
	}

	@Override
	protected void start() {
		var reg = registries.lookupOrThrow(Registries.BIOME);

		add("tuna_hunt", new RemoveItemModifier(
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.ATTACKER,
						EntityPredicate.Builder.entity().entityType(
								EntityTypePredicate.of(YHEntities.TUNA.get()))).build()
		));

		add("scavenging_roe", create(YHFood.ROE.item.get(), 1,
				killedByKnife(), fire(false), entity(EntityType.SALMON),
				LootItemRandomChanceCondition.randomChance(0.5f).build()));
		add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING.location()).build(),
				new BiomeTagCondition(reg.getOrThrow(YHBiomeTagsProvider.LAMPREY))
		));
		add("fishing_crab", new ReplaceItemModifier(0.1f, YHFood.CRAB.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING.location()).build(),
				new BiomeTagCondition(reg.getOrThrow(YHBiomeTagsProvider.CRAB_FISHING))
		));

		add("udumbara_ancient_city_loot", loot(YHLootGen.UDUMBARA_LOOT,
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()).build()));
		add("udumbara_ancient_city_ice_box_loot", loot(YHLootGen.UDUMBARA_LOOT,
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX.location()).build()));
	}

	private static LootModifier loot(ResourceKey<LootTable> id, LootItemCondition... cond) {
		return new AddTableLootModifier(cond, id);
	}

	private static LootItemCondition killer(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}


	private static LootItemCondition entity(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(type))).build();
	}

	private static LootItemCondition entity(TagKey<EntityType<?>> tag) {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(tag))).build();
	}

	public static LootItemCondition killedByKnife() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.ATTACKER,
				EntityPredicate.Builder.entity().equipment(
						EntityEquipmentPredicate.Builder.equipment().mainhand(
										ItemPredicate.Builder.item().of(TagRef.TOOLS_KNIFE))
								.build()).build()).build();
	}

	private static LootItemCondition fire(boolean fire) {
		return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().flags(
						EntityFlagsPredicate.Builder.flags().setOnFire(fire)
				).build()).build();
	}

	private static AddItemModifier create(Item item, int count, LootItemCondition... conditions) {
		var ans = AddItemModifierAccessor.create(conditions, item, count);
		assert ans != null;
		return ans;
	}

	private static <T> EntryHolder<T> vanilla(T obj, String id) {
		return new EntryHolder<>(obj, ResourceLocation.withDefaultNamespace(id));
	}

	@Override
	public LogicalSide getSide() {
		return LogicalSide.SERVER;
	}

	private record EntryHolder<T>(T type, ResourceLocation id) {

	}

}
