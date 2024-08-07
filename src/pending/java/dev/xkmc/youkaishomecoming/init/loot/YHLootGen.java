package dev.xkmc.youkaishomecoming.init.loot;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.YHCrops;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class YHLootGen {


	public static final ResourceLocation NEST_CHEST = YoukaisHomecoming.loc("chests/youkai_nest/chest");
	public static final ResourceLocation NEST_BARREL = YoukaisHomecoming.loc("chests/youkai_nest/barrel");
	public static final ResourceLocation SHRINE_CHEST = YoukaisHomecoming.loc("chests/hakurei_shrine/chest");
	public static final ResourceLocation SHRINE_BARREL = YoukaisHomecoming.loc("chests/hakurei_shrine/barrel");
	public static final ResourceLocation SHRINE_CABINET = YoukaisHomecoming.loc("chests/hakurei_shrine/cabinet");

	private static ResourceKey<LootTable> loc(String str) {
		return ResourceKey.create(Registries.LOOT_TABLE, YoukaisHomecoming.loc(str));
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {

		{
			var bone = LootTableTemplate.getPool(1, 0)
					.add(LootTableTemplate.getItem(Items.BONE, 16));
			var misc = LootTableTemplate.getPool(5, 1)
					.add(LootTableTemplate.getItem(Items.BOWL, 1))
					.add(LootTableTemplate.getItem(Items.ROTTEN_FLESH, 8))
					.add(LootTableTemplate.getItem(Items.BAMBOO, 8))
					.add(LootTableTemplate.getItem(Items.POTATO, 8))
					.add(LootTableTemplate.getItem(Items.CARROT, 8))
					.add(LootTableTemplate.getItem(Items.COAL, 8));
			var dango = LootTableTemplate.getPool(3, 1)
					.add(LootTableTemplate.getItem(YHFood.MOCHI.item.get(), 8).setWeight(100))
					.add(LootTableTemplate.getItem(YHFood.SAKURA_MOCHI.item.get(), 4).setWeight(100))
					.add(LootTableTemplate.getItem(YHFood.MATCHA_MOCHI.item.get(), 4).setWeight(100))
					.add(LootTableTemplate.getItem(YHFood.KINAKO_DANGO.item.get(), 1).setWeight(50))
					.add(LootTableTemplate.getItem(YHFood.TSUKIMI_DANGO.item.get(), 1).setWeight(50))
					.add(LootTableTemplate.getItem(YHFood.ASSORTED_DANGO.item.get(), 1).setWeight(50))
					.add(LootTableTemplate.getItem(YHFood.MITARASHI_DANGO.item.get(), 1).setWeight(50))
					.add(LootTableTemplate.getItem(YHFood.YASHOUMA_DANGO.item.get(), 1).setWeight(50));
			var rice = LootTableTemplate.getPool(2, 1)
					.add(LootTableTemplate.getItem(YHFood.ONIGILI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.SENBEI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.SEKIBANKIYAKI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.YAKUMO_INARI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.BUN.item.get(), 1))
					.add(LootTableTemplate.getItem(YHFood.OYAKI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.PORK_RICE_BALL.item.get(), 1));
			var flesh = LootTableTemplate.getPool(1, 1)
					.add(LootTableTemplate.getItem(YHFood.FLESH.item.get(), 4).setWeight(100))
					.add(LootTableTemplate.getItem(YHFood.COOKED_FLESH.item.get(), 2).setWeight(50))
					.add(LootTableTemplate.getItem(YHFood.FLESH_ROLL.item.get(), 2).setWeight(20))
					.add(LootTableTemplate.getItem(YHFood.FLESH_STEW.item.get(), 1).setWeight(20));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(NEST_CHEST, LootTable.lootTable()
					.withPool(bone).withPool(misc).withPool(rice).withPool(flesh)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(NEST_BARREL, LootTable.lootTable()
					.withPool(bone).withPool(misc).withPool(dango).withPool(flesh)
			));
		}
		{
			var misc = LootTableTemplate.getPool(3, 1)
					.add(LootTableTemplate.getItem(Items.REDSTONE, 2).setWeight(2))
					.add(LootTableTemplate.getItem(Items.LAPIS_LAZULI, 2).setWeight(2))
					.add(LootTableTemplate.getItem(Items.GLOWSTONE_DUST, 2).setWeight(2))
					.add(LootTableTemplate.getItem(Items.AMETHYST_SHARD, 2).setWeight(2))
					.add(LootTableTemplate.getItem(Items.COAL, 2).setWeight(4))
					.add(LootTableTemplate.getItem(Items.EMERALD, 1))
					.add(LootTableTemplate.getItem(Items.IRON_INGOT, 2))
					.add(LootTableTemplate.getItem(Items.GOLD_NUGGET, 2));

			var crops = LootTableTemplate.getPool(2, 1)
					.add(LootTableTemplate.getItem(YHCrops.TEA.getSeed(), 4).setWeight(4))
					.add(LootTableTemplate.getItem(YHCrops.SOYBEAN.getSeed(), 4).setWeight(4))
					.add(LootTableTemplate.getItem(YHCrops.REDBEAN.getSeed(), 4).setWeight(4))
					.add(LootTableTemplate.getItem(YHCrops.COFFEA.getSeed(), 4).setWeight(2))
					.add(LootTableTemplate.getItem(YHCrops.UDUMBARA.getSeed(), 1).setWeight(1))
					.add(LootTableTemplate.getItem(ModItems.RICE.get(), 2).setWeight(8))
					.add(LootTableTemplate.getItem(ModItems.CABBAGE.get(), 2).setWeight(4))
					.add(LootTableTemplate.getItem(ModItems.TOMATO.get(), 2).setWeight(4))
					.add(LootTableTemplate.getItem(Items.CARROT, 2).setWeight(4))
					.add(LootTableTemplate.getItem(Items.POTATO, 2).setWeight(4));

			var tea = LootTableTemplate.getPool(2,1)
					.add(LootTableTemplate.getItem(YHCrops.TEA.getFruits(), 4))
					.add(LootTableTemplate.getItem(YHTea.BLACK.leaves.get(), 2))
					.add(LootTableTemplate.getItem(YHTea.WHITE.leaves.get(), 2))
					.add(LootTableTemplate.getItem(YHTea.OOLONG.leaves.get(), 2))
					.add(LootTableTemplate.getItem(YHTea.GREEN.leaves.get(), 2))
					.add(LootTableTemplate.getItem(YHItems.MATCHA.get(), 2));

			var rice = LootTableTemplate.getPool(3, 1)
					.add(LootTableTemplate.getItem(YHFood.ONIGILI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.SENBEI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.SEKIBANKIYAKI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.YAKUMO_INARI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.BUN.item.get(), 1))
					.add(LootTableTemplate.getItem(YHFood.OYAKI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.PORK_RICE_BALL.item.get(), 1))
					.add(LootTableTemplate.getItem(YHFood.OILY_BEAN_CURD.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.ROASTED_LAMPREY_FILLET.item.get(), 2));

			var dango = LootTableTemplate.getPool(1, 1)
					.add(LootTableTemplate.getItem(YHFood.MOCHI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.TSUKIMI_DANGO.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.COFFEE_MOCHI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.MATCHA_MOCHI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.SAKURA_MOCHI.item.get(), 2))
					.add(LootTableTemplate.getItem(YHFood.YASHOUMA_DANGO.item.get(), 2))
					;

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_CHEST, LootTable.lootTable()
					.withPool(misc)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_BARREL, LootTable.lootTable()
					.withPool(crops).withPool(tea)
			));

			pvd.addLootAction(LootContextParamSets.CHEST, cons -> cons.accept(SHRINE_CABINET, LootTable.lootTable()
					.withPool(rice).withPool(dango)
			));
		}
	}

}
