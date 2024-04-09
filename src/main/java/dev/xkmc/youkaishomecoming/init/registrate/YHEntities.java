package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import dev.xkmc.youkaishomecoming.content.entity.damaku.ItemDamakuEntity;
import dev.xkmc.youkaishomecoming.content.entity.damaku.ItemDamakuRenderer;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyEntity;
import dev.xkmc.youkaishomecoming.content.entity.lampery.LampreyRenderer;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaEntity;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaRenderer;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.levelgen.Heightmap;
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
			.spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
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

	public static final EntityEntry<RumiaEntity> RUMIA = YoukaisHomecoming.REGISTRATE
			.entity("rumia", RumiaEntity::new, MobCategory.MONSTER)
			.properties(e -> e.sized(0.4F, 1.7f).clientTrackingRange(10))
			.spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RumiaEntity::checkRumiaSpawnRules)
			.attributes(RumiaEntity::createAttributes)
			.renderer(() -> RumiaRenderer::new)
			.spawnEgg(0x000000, 0x000000).tab(YoukaisHomecoming.TAB.getKey()).build()
			.loot((pvd, type) -> pvd.add(type,
					LootTable.lootTable()
							.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDamaku.SIMPLE.get(DyeColor.RED).get(), 8, 16)))
							.withPool(LootPool.lootPool().add(LootTableTemplate.getItem(YHDamaku.SIMPLE.get(DyeColor.BLACK).get(), 4, 8)))
			)).register();

	public static final EntityEntry<ItemDamakuEntity> ITEM_DAMAKU = YoukaisHomecoming.REGISTRATE
			.<ItemDamakuEntity>entity("item_damaku", ItemDamakuEntity::new, MobCategory.MISC)
			.properties(e -> e.sized(0.4f, 0.4f).clientTrackingRange(4))
			.renderer(() -> ItemDamakuRenderer::new)
			.register();

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return YoukaisHomecoming.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
