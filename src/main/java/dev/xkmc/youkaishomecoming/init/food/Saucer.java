package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public enum Saucer implements StringRepresentable {
	SAUCER_0("saucer"),
	SAUCER_1("saucer_plate", "saucer_sake"),
	SAUCER_2("saucer_plate", "saucer_sake"),
	SAUCER_3("saucer_plate", "saucer_sake", "saucer_bowl"),
	SAUCER_4("saucer_large"),
	;

	public final String base;
	public final String[] tex;

	Saucer(String base, String... tex) {
		this.base = base;
		this.tex = tex;
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public BlockModelBuilder build(RegistrateBlockstateProvider pvd) {
		var builder = pvd.models().getBuilder("block/" + getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + getName())));
		builder.texture(base, "block/" + base);
		builder.texture("particle", "block/" + base);
		for (var e : tex) {
			builder.texture(e, "block/" + e);
		}
		builder.renderType("cutout");
		return builder;
	}

	@Override
	public String getSerializedName() {
		return getName();
	}

}
