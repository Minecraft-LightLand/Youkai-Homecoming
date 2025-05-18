package dev.xkmc.youkaishomecoming.content.pot.table.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdditionalModelHolder implements TableModelHolder {

	private final Map<String, VariantModelPart> parts = new LinkedHashMap<>();
	private final ResourceLocation path;

	public AdditionalModelHolder(ModelHolderManager manager, ResourceLocation path) {
		manager.register(this);
		this.path = path;
	}

	public VariantModelPart addPart(String name, int max) {
		var ans = new VariantModelPart(name, path.withSuffix("/" + name), max);
		parts.put(name, ans);
		return ans;
	}

	@Override
	public List<ResourceLocation> allModels() {
		List<ResourceLocation> ans = new ArrayList<>();
		for (var ent : parts.entrySet()) {
			ent.getValue().addModels(ans);
		}
		return ans;
	}

	@Override
	public void build(TableModelProvider pvd) {
		for (var ent : parts.entrySet()) {
			ent.getValue().build(pvd);
		}
	}

	public void buildContents(List<ResourceLocation> ans, List<ItemStack> list) {
		var partList = new ArrayList<>(parts.entrySet());
		int[] counter = new int[partList.size()];
		for (var stack : list) {
			for (int i = 0; i < partList.size(); i++) {
				var part = partList.get(i).getValue();
				var id = part.find(stack);
				if (id != null) {
					int index = counter[i];
					counter[i]++;
					if (index < part.max) {
						ans.add(part.modelAt(id, index));
					}
					break;
				}
			}
		}
	}

}
