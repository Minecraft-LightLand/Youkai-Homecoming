package dev.xkmc.youkaishomecoming.content.item.fluid;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;

public abstract class BottleTexture {

	private static List<BottleTexture> LIST = new ArrayList<>();

	public final int index;

	public BottleTexture() {
		index = LIST.size();
		LIST.add(this);
	}

	public abstract String bottleModel();

	public static void buildBottleModel(DataGenContext<Item, SlipBottleItem> ctx, RegistrateItemModelProvider pvd) {
		var model = pvd.generated(ctx, pvd.modLoc("item/sake_bottle"));

		model.override()
				.predicate(YoukaisHomecoming.loc("slip"), 1 / 32f)
				.model(pvd.getBuilder(ctx.getName() + "_overlay")
						.parent(new ModelFile.UncheckedModelFile("item/generated"))
						.texture("layer0", pvd.modLoc("item/sake_bottle"))
						.texture("layer1", pvd.modLoc("item/sake_bottle_overlay")))
				.end();

		int n = LIST.size();
		for (var e : LIST) {
			model.override()
					.predicate(YoukaisHomecoming.loc("bottle"), (e.index + 0.5f) / n)
					.model(new ModelFile.UncheckedModelFile(pvd.modLoc("item/" + e.bottleModel())))
					.end();
		}
	}

	public static float texture(ItemStack stack) {
		var fluid = SlipBottleItem.getFluid(stack);
		if (fluid.isEmpty()) return 0;
		if (!(fluid.getFluid() instanceof YHFluid liquid)) return 0;
		var set = liquid.type.bottleSet();
		if (set == null) return 0;
		return (set.index + 1) * 1f / LIST.size();
	}

}
