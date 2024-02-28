package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;

public class YHGLMProvider extends GlobalLootModifierProvider {

	public static final RegistryEntry<Codec<ReplaceItemModifier>> REPLACE_ITEM;


	static {
		REPLACE_ITEM = YoukaisHomecoming.REGISTRATE.simple("replace_item",
				ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ReplaceItemModifier.CODEC);
	}

	public static void register() {

	}

	public YHGLMProvider(DataGenerator gen) {
		super(gen.getPackOutput(), YoukaisHomecoming.MODID);
	}

	@Override
	protected void start() {
		add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING_FISH).build()
		));
		add("scavenging_flesh", Objects.requireNonNull(create(YHFood.FLESH.item.get(), 1,
				killedByKnife(), fire(false), isFleshSource(), killedByYoukai())));

		add("scavenging_flesh_cooked", Objects.requireNonNull(create(YHFood.COOKED_FLESH.item.get(), 1,
				killedByKnife(), fire(true), isFleshSource(), killedByYoukai())));
	}

	private static LootItemCondition isFleshSource() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().entityType(
						EntityTypePredicate.of(YHTagGen.FLESH_SOURCE))).build();
	}

	private static LootItemCondition killedByKnife() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.KILLER,
				EntityPredicate.Builder.entity().equipment(
						EntityEquipmentPredicate.Builder.equipment().mainhand(
										ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
								.build()).build()).build();
	}

	private static LootItemCondition killedByYoukai() {
		return LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.KILLER,
				EntityPredicate.Builder.entity().effects(MobEffectsPredicate.effects().and(YHEffects.YOUKAIFYING.get()))
		).or(LootItemEntityPropertyCondition.hasProperties(
				LootContext.EntityTarget.KILLER,
				EntityPredicate.Builder.entity().effects(MobEffectsPredicate.effects().and(YHEffects.YOUKAIFIED.get()))
		)).build();
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
