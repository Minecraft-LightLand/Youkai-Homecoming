package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class VariantModelHolder extends FixedModelHolder {

	private final LinkedHashMap<String, VariantModelPart> parts = new LinkedHashMap<>();
	private final ResourceLocation path;

	public VariantModelHolder(ModelHolderManager manager, ResourceLocation id) {
		super(manager, id.withSuffix("/base"));
		this.path = id;
	}

	public VariantModelPart addPart(String name, int max) {
		var ans = new VariantModelPart(path.withSuffix("/" + name), max);
		parts.put(name, ans);
		return ans;
	}

	@Override
	public List<ResourceLocation> allModels() {
		List<ResourceLocation> ans = new ArrayList<>();
		ans.add(modelLoc());
		for (var ent : parts.entrySet()) {
			ent.getValue().addModels(ans);
		}
		return ans;
	}

	@Override
	public void build(TableModelProvider pvd) {
		super.build(pvd);
		for (var ent : parts.entrySet()) {
			ent.getValue().build(pvd);
		}
	}

}
