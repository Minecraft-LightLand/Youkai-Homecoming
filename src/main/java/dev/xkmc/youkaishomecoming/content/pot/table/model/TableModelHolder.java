package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface TableModelHolder {

	ResourceLocation id();

	ResourceLocation modelLoc();

	List<ResourceLocation> allModels();

	void build(TableModelProvider pvd);

}
