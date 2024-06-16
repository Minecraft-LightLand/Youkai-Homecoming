package dev.xkmc.youkaishomecoming.content.block.furniture;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;

public class TemplateModelHandler<T extends ModelBuilder<T>> {

	private static final String BLOCK_FOLDER = "template";

	private final ModelProvider<T> pvd;

	public TemplateModelHandler(ModelProvider<T> pvd) {
		this.pvd = pvd;
	}

	private T singleTexture(String name, String path, ResourceLocation texture) {
		return pvd.singleTexture(name, pvd.modLoc(path), texture);
	}

	public T trapdoorOrientableBottom(String name, ResourceLocation texture) {
		return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture);
	}

	public T trapdoorOrientableTop(String name, ResourceLocation texture) {
		return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture);
	}

	public T trapdoorOrientableOpen(String name, ResourceLocation texture) {
		return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture);
	}

	private T door(String name, String model, ResourceLocation bottom, ResourceLocation top) {
		return pvd.withExistingParent(name, pvd.modLoc(BLOCK_FOLDER + "/" + model))
				.texture("bottom", bottom)
				.texture("top", top);
	}

	public T doorBottomLeft(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_bottom_left", bottom, top);
	}

	public T doorBottomLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_bottom_left_open", bottom, top);
	}

	public T doorBottomRight(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_bottom_right", bottom, top);
	}

	public T doorBottomRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_bottom_right_open", bottom, top);
	}

	public T doorTopLeft(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_top_left", bottom, top);
	}

	public T doorTopLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_top_left_open", bottom, top);
	}

	public T doorTopRight(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_top_right", bottom, top);
	}

	public T doorTopRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
		return door(name, "door_top_right_open", bottom, top);
	}

}
