package dev.xkmc.youkaishomecoming.init.data;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.l2library.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2library.serial.advancements.CriterionBuilder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.food.CoffeeDrinks;
import dev.xkmc.youkaishomecoming.init.food.CoffeeCrops;
import dev.xkmc.youkaishomecoming.init.registrate.CoffeeItems;
import net.minecraft.Util;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public class CoffeeAdvGen {

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		var gen = new AdvancementGenerator(pvd, YoukaisHomecoming.MODID);
		var b = gen.new TabBuilder("main");
		var root = b.root("coffee_lovers", CoffeeCrops.COFFEA.getSeed().getDefaultInstance(),
				CriterionBuilder.item(CoffeeCrops.COFFEA.getSeed()),
				"Coffee Lovers", "Welcome To Coffee's Flavors & Delight");
		root.create("coffea", CoffeeCrops.COFFEA.getSeed(),
						CriterionBuilder.one(ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(CoffeeTagGen.FARMLAND_COFFEA).build()),
								ItemPredicate.Builder.item().of(CoffeeCrops.COFFEA.getSeed()))),
						"Home Grown Coffea", "Plant Coffea on Podzol, Mud, or Soul Soil")
				.create("coffee_era", CoffeeItems.COFFEE_POWDER.asStack(),
						CriterionBuilder.item(CoffeeItems.COFFEE_POWDER.get()),
						"Coffee Era", "Get Coffee Powder")
				.create("fragrant", CoffeeDrinks.ESPRESSO.item.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(CoffeeDrinks.ESPRESSO.item.get())),
						"Fragrant!", "Drink Espresso")
				.create("q_grader", CoffeeDrinks.ESPRESSO.item.asStack(),
						Util.make(CriterionBuilder.and(), c -> Arrays.stream(CoffeeDrinks.values())
								.map(e -> e.item.get()).map(e -> Pair.of(e, ConsumeItemTrigger.TriggerInstance.usedItem(e)))
								.forEach(p -> c.add(ForgeRegistries.ITEMS.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Q Grader", "Drink all coffee variants")
				.type(FrameType.CHALLENGE, true, true, false);

		root.finish();
	}

}
