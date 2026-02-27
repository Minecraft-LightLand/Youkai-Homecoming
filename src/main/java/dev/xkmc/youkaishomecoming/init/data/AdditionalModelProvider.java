package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.youkaishomecoming.content.pot.table.item.TableItemManager;
import dev.xkmc.youkaishomecoming.content.pot.table.model.ClientModelManager;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class AdditionalModelProvider implements DataProvider {
	private final PackOutput output;
	private final String modid;

	public AdditionalModelProvider(PackOutput output, String modid) {
		this.output = output;
		this.modid = modid;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {

		Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
				.resolve(YoukaisHomecoming.MODID)
				.resolve("additional_models.json");

		var obj = ((ClientModelManager) TableItemManager.MANAGER).genModelList();

		return DataProvider.saveStable(cache, obj, path);
	}

	@Override
	public String getName() {
		return "Additional Models: " + modid;
	}

}
