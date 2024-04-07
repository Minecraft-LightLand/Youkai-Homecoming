package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

public class YHEntities {

	public static final EntityEntry<LampreyEntity> LAMPREY = YoukaisHomecoming.REGISTRATE
			.entity("lamprey", LampreyEntity::new, MobCategory.WATER_AMBIENT)
			.properties(e -> e.sized(0.5F, 0.4F).clientTrackingRange(4))
			.attributes(LampreyEntity::createAttributes)
			.renderer(() -> LampreyRenderer::new)
			.spawnEgg(-3814463, -6646165).tab(YoukaisHomecoming.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type,
					LootTable.lootTable()
							.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
									.add(LootItem.lootTableItem(YHFood.RAW_LAMPREY.item.get())
											.apply(SmeltItemFunction.smelted()
													.when(LootItemEntityPropertyCondition.hasProperties(
															LootContext.EntityTarget.THIS,
															EntityPredicate.Builder.entity()
																	.flags(EntityFlagsPredicate.Builder.flags()
																			.setOnFire(true).build()))))))
							.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
									.add(LootItem.lootTableItem(Items.BONE_MEAL))
									.when(LootItemRandomChanceCondition.randomChance(0.05F)))
			)).register();


	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
