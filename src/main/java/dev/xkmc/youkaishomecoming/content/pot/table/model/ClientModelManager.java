package dev.xkmc.youkaishomecoming.content.pot.table.model;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class ClientModelManager extends ModelHolderManager {

	public ClientModelManager(L2Registrate reg) {
		super(reg);
		reg.addDataGenerator(ProviderType.ITEM_MODEL, this::genModel);
		reg.getModEventBus().addListener(this::onAdditionalModel);
	}

	private void genModel(RegistrateItemModelProvider pvd) {
		var wrapped = new Provider(pvd);
		for (var e : models) {
			e.build(wrapped);
		}
	}

	private void onAdditionalModel(ModelEvent.RegisterAdditional event) {
		for (var e : models) {
			for (var x : e.allModels()) {
				event.register(x);
			}
		}
	}

	private static class Provider implements TableModelProvider {

		private final RegistrateItemModelProvider pvd;

		private Provider(RegistrateItemModelProvider pvd) {
			this.pvd = pvd;
		}

		@Override
		public TableModelBuilder create(ResourceLocation id, ResourceLocation parent) {
			return new Builder(pvd, id, parent);
		}

	}

	private static class Builder implements TableModelBuilder {

		private final ItemModelBuilder builder;

		private Builder(RegistrateItemModelProvider pvd, ResourceLocation id, ResourceLocation parent) {
			this.builder = pvd.getBuilder(id.toString())
					.parent(new ModelFile.UncheckedModelFile(parent))
					.renderType("cutout");
		}

		@Override
		public void tex(String layer, ResourceLocation tex) {
			builder.texture(layer, tex);
		}
	}

}
