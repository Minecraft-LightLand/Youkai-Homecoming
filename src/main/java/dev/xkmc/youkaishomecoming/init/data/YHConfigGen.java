package dev.xkmc.youkaishomecoming.init.data;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.youkaishomecoming.content.item.fluid.YHFluidHandler;
import dev.xkmc.youkaishomecoming.content.pot.table.item.ModelIngredientData;
import dev.xkmc.youkaishomecoming.content.pot.table.model.VariantModelPart;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

public class YHConfigGen extends ConfigDataProvider {

	public YHConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Youkai Homecoming Config");
	}

	@Override
	public void add(Collector collector) {
		ModelIngredientData ans = new ModelIngredientData();
		for (var ent : VariantModelPart.getAll().entrySet()) {
			var map = new LinkedHashMap<String, Ingredient>();
			for (var pair : ent.getValue().ingredients.entrySet()) {
				map.put(pair.getKey(), pair.getValue().getIngredient());
			}
			ans.ingredients.put(ent.getKey(), map);
		}
		collector.add(YoukaisHomecoming.INGREDIENT, YoukaisHomecoming.loc("default"), ans);

		YHFluidHandler.Config map = new YHFluidHandler.Config();
		map.simpleFluidItems.put(Fluids.LAVA, new YHFluidHandler.Config.FluidItem(
				Items.LAVA_BUCKET, 1000, 0xffCC4628));
		collector.add(YoukaisHomecoming.FLUID_MAP, YoukaisHomecoming.loc("vanilla"), map);

		if (ModList.get().isLoaded(Create.ID)) {
			map = new YHFluidHandler.Config();
			map.simpleFluidItems.put(AllFluids.TEA.getSource(), new YHFluidHandler.Config.FluidItem(
					AllItems.BUILDERS_TEA.get(), 250, 0xffCC7459));
			collector.add(YoukaisHomecoming.FLUID_MAP, ResourceLocation.fromNamespaceAndPath(Create.ID, "regular"), map);
		}
	}

}
