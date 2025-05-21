package dev.xkmc.youkaishomecoming.content.pot.table.model;

import dev.xkmc.l2library.base.L2Registrate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public class ModelHolderManager {

	public static ModelHolderManager createModelBuilderManager(L2Registrate reg) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return new ClientModelManager(reg);
		}
		return new ModelHolderManager(reg);
	}

	protected final L2Registrate owner;
	protected List<TableModelHolder> models = new ArrayList<>();

	ModelHolderManager(L2Registrate reg) {
		this.owner = reg;
	}

	public void register(TableModelHolder builder) {
		models.add(builder);
	}


}
