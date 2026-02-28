package dev.xkmc.youkaishomecoming.content.pot.table.model;

import com.google.gson.*;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClientModelManager extends ModelHolderManager {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

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

	public JsonObject genModelList() {
		JsonObject ans = new JsonObject();
		JsonArray arr = new JsonArray();
		ans.add("entries", arr);
		for (var e : models) {
			for (var x : e.allModels()) {
				arr.add(x.toString());
			}
		}
		return ans;
	}

	private void onAdditionalModel(ModelEvent.RegisterAdditional event) {
		var index = YoukaisHomecoming.loc("additional_models.json");
		List<ResourceLocation> list = new ArrayList<>();
		var files = Minecraft.getInstance().getResourceManager().getResourceStack(index);
		for (Resource file : files) {
			try (Reader reader = new BufferedReader(new InputStreamReader(file.open(), StandardCharsets.UTF_8))) {
				JsonObject obj = GsonHelper.fromJson(GSON, reader, JsonObject.class);
				JsonArray entryList = obj.get("entries").getAsJsonArray();
				for (JsonElement entry : entryList) {
					ResourceLocation loc = new ResourceLocation(entry.getAsString());
					list.remove(loc);
					list.add(loc);
				}
			} catch (RuntimeException | IOException e) {
				YoukaisHomecoming.LOGGER.error("Couldn't read global additional model list {} in data pack {}", index, file.sourcePackId(), e);
			}
		}
		for (var e : list) {
			event.register(e);
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
