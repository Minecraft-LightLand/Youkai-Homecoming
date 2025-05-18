package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;

public class FixedModelHolder implements TableModelHolder {

	protected final ResourceLocation model;
	protected final LinkedHashMap<String, ResourceLocation> tex = new LinkedHashMap<>();

	public FixedModelHolder(ModelHolderManager manager, ResourceLocation model) {
		this.model = model;
		manager.register(this);
	}

	@Override
	public ResourceLocation id() {
		return model;
	}

	@Override
	public ResourceLocation modelLoc() {
		return model.withPrefix("cuisine/");
	}

	public FixedModelHolder put(String layer, ResourceLocation path) {
		tex.put(layer, path);
		return this;
	}

	public FixedModelHolder putDefault(String... layers) {
		for (var layer : layers)
			tex.put(layer, model.withPath("block/table/" + layer));
		return this;
	}

	@Override
	public List<ResourceLocation> allModels() {
		return List.of(modelLoc());
	}

	@Override
	public void build(TableModelProvider pvd) {
		var builder = pvd.create(modelLoc(), id().withPrefix("table/"));
		for (var ent : tex.entrySet()){
			builder.tex(ent.getKey(), ent.getValue());
		}
	}

}
