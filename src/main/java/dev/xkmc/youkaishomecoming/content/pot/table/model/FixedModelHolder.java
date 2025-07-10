package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;

public class FixedModelHolder implements TableModelHolder {

	protected final ResourceLocation model, parent, texture;
	protected final LinkedHashMap<String, ResourceLocation> tex = new LinkedHashMap<>();

	public FixedModelHolder(ModelHolderManager manager, ResourceLocation model, ResourceLocation parent, ResourceLocation tex) {
		this.model = model;
		this.parent = parent;
		this.texture = tex;
		manager.register(this);
	}

	public FixedModelHolder(ModelHolderManager manager, ResourceLocation model) {
		this(manager, model, model.withPrefix("table/"), model.withPath("block/table/"));
	}

	public ResourceLocation modelLoc() {
		return model.withPrefix("cuisine/");
	}

	public FixedModelHolder put(String layer, ResourceLocation path) {
		tex.put(layer, path);
		return this;
	}

	public FixedModelHolder put(String layer, String path) {
		tex.put(layer, texture.withSuffix(path));
		return this;
	}

	public FixedModelHolder putDefault(String... layers) {
		for (var layer : layers)
			tex.put(layer, texture.withSuffix(layer));
		return this;
	}

	@Override
	public List<ResourceLocation> allModels() {
		return List.of(modelLoc());
	}

	@Override
	public void build(TableModelProvider pvd) {
		var builder = pvd.create(modelLoc(), parent);
		for (var ent : tex.entrySet()) {
			builder.tex(ent.getKey(), ent.getValue());
		}
	}

}
