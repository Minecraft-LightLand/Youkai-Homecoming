package dev.xkmc.youkaishomecoming.content.pot.table.food;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.pot.table.model.FixedModelHolder;
import dev.xkmc.youkaishomecoming.content.pot.table.model.TableModelHolder;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FoodModelHelper {

	private static FixedModelHolder roll(String id) {
		return new FixedModelHolder(TableItemManager.MANAGER, YoukaisHomecoming.loc("roll/" + id));
	}

	public static FixedModelHolder futomaki(String id) {
		return roll("futomaki")
				.put("kelp", YoukaisHomecoming.loc("block/table/roll_kelp"))
				.put("content", YoukaisHomecoming.loc("block/table/roll/" + id));
	}

	public static FixedModelHolder hosomaki(String id) {
		return roll("hosomaki")
				.put("kelp", YoukaisHomecoming.loc("block/table/roll_kelp"))
				.put("content", YoukaisHomecoming.loc("block/table/roll/" + id));
	}

	public static <T extends Item> void buildModel(
			FixedModelHolder model, DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd
	) {
		pvd.getBuilder("item/" + ctx.getName()).parent(new ModelFile.UncheckedModelFile(model.modelLoc()));
	}

	private static final Map<ResourceLocation, FixedModelHolder> MAP = new ConcurrentHashMap<>();

	public static void map(ResourceLocation item, FixedModelHolder model) {
		MAP.put(item, model);
	}

	public static @Nullable TableModelHolder find(ItemStack current) {
		return MAP.get(current.getItemHolder().unwrapKey().orElseThrow().location());
	}

	static {
		map(ModItems.KELP_ROLL.getId(), hosomaki("carrot_hosomaki"));
	}

}
