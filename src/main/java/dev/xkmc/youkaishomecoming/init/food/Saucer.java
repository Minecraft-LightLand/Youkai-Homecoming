package dev.xkmc.youkaishomecoming.init.food;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.Locale;

public enum Saucer implements StringRepresentable {
	SAUCER_1(2, 2, 6, "saucer_plate", "saucer_sake"),
	SAUCER_2(2, 2, 3, "saucer_plate", "saucer_sake"),
	SAUCER_3(2, 2, 3, "saucer_plate", "saucer_sake", "saucer_bowl"),
	SAUCER_4(2, 2, 1, "saucer_large"),
	CERAMIC(4, 3, 1, "ceramic"),
	PORCELAIN(1, 1, 1, "porcelain_plate_top", "porcelain_plate_bottom"),
	;

	public final String[] tex;
	public final int x, z, height;

	Saucer(int x, int z, int height, String... tex) {
		this.x = x;
		this.z = z;
		this.height = height;
		this.tex = tex;
	}

	private String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public BlockModelBuilder build(RegistrateBlockstateProvider pvd) {
		var builder = pvd.models().getBuilder("block/" + getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/dish/" + getName())));
		for (var e : tex) {
			builder.texture(e, "block/dish/" + e);
		}
		builder.texture("particle", "block/dish/" + tex[0]);
		builder.renderType("cutout");
		return builder;
	}

	@Override
	public String getSerializedName() {
		return getName();
	}

}
