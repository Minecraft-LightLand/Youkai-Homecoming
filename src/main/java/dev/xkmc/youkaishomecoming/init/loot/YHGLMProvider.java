package dev.xkmc.youkaishomecoming.init.loot;

import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.mixin.AddItemModifierAccessor;
import dev.xkmc.youkaishomecoming.mixin.AddLootTableModifierAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;

import java.util.concurrent.CompletableFuture;

public class YHGLMProvider extends GlobalLootModifierProvider {

	public static final CdcVal<ReplaceItemModifier> REPLACE_ITEM =
			CdcReg.of(YoukaisHomecoming.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS)
					.reg("replace_item", ReplaceItemModifier.CODEC);

	public static void register() {

	}

	public YHGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, YoukaisHomecoming.MODID);
	}

	@Override
	protected void start() {
		add("fishing_lamprey", new ReplaceItemModifier(0.1f, YHFood.RAW_LAMPREY.item.asStack(),
				LootTableIdCondition.builder(BuiltInLootTables.FISHING.location()).build()
		));

		add("udumbara_ancient_city_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()).build()));
		add("udumbara_ancient_city_ice_box_loot", loot(YHLootGen.UDUMBARA_LOOT.location(),
				LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX.location()).build()));
	}

	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return AddLootTableModifierAccessor.createAddLootTableModifier(cond, id);
	}

	private static AddItemModifier create(Item item, int count, LootItemCondition... conditions) {
		var ans = AddItemModifierAccessor.create(conditions, item, count);
		assert ans != null;
		return ans;
	}

	private static <T> EntryHolder<T> vanilla(T obj, String id) {
		return new EntryHolder<>(obj, ResourceLocation.withDefaultNamespace(id));
	}

	private record EntryHolder<T>(T type, ResourceLocation id) {

	}

}
