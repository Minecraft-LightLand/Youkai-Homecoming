package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class VariantModelHolder extends FixedModelHolder {

	private final AdditionalModelHolder additional;

	public VariantModelHolder(ModelHolderManager manager, ResourceLocation path) {
		super(manager, path.withSuffix("/base"));
		this.additional = new AdditionalModelHolder(manager, path);
	}

	public VariantModelPart addPart(String name, int max) {
		return additional.addPart(name, max);
	}

	public void buildContents(List<ResourceLocation> ans, List<ItemStack> list) {
		additional.buildContents(ans, list);
	}

}
