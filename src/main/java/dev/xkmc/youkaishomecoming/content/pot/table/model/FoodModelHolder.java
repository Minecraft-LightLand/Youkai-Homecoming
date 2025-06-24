package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

public class FoodModelHolder extends FixedModelHolder {

	private final ResourceLocation self;

	public FoodModelHolder(ModelHolderManager manager, ResourceLocation parent, ResourceLocation self) {
		super(manager, parent);
		this.self = self;
	}

	public ResourceLocation modelLoc() {
		return self.withPrefix("cuisine/");
	}


}
