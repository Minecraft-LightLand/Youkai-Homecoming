package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public enum Saucer implements StringRepresentable {
	SAUCER_1(true, "plate", "sake"),
	SAUCER_2(true, "plate", "sake"),
	SAUCER_3(true, "plate", "sake", "bowl"),
	SAUCER_4(false, "large");

	public final boolean extra;
	public final String base;
	public final String[] tex;

	Saucer(boolean extra, String base, String... tex) {
		this.extra = extra;
		this.base = base;
		this.tex = tex;
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public BlockModelBuilder build(RegistrateBlockstateProvider pvd) {
		var builder = pvd.models().getBuilder("block/" + getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + getName())));
		builder.texture(base, "block/saucer_" + base);
		builder.texture("particle", "block/saucer_" + base);
		for (var e : tex) {
			builder.texture(e, "block/saucer_" + e);
		}
		builder.renderType("cutout");
		return builder;
	}

	@Override
	public String getSerializedName() {
		return getName();
	}

}
