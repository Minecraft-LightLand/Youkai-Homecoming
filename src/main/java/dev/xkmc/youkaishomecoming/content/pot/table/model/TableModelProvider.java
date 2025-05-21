package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

public interface TableModelProvider {

	TableModelBuilder create(ResourceLocation id, ResourceLocation parent);

}
