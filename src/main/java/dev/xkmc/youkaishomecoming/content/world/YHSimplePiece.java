package dev.xkmc.youkaishomecoming.content.world;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.youkaishomecoming.init.registrate.YHWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class YHSimplePiece extends SinglePoolElement {

	public static final Codec<YHSimplePiece> CODEC = RecordCodecBuilder.create((i) -> i.group(
			templateCodec(),
			processorsCodec(),
			projectionCodec()
	).apply(i, YHSimplePiece::new));

	protected YHSimplePiece(Either<ResourceLocation, StructureTemplate> id, Holder<StructureProcessorList> proc, StructureTemplatePool.Projection projection) {
		super(id, proc, projection);
	}

	public YHSimplePiece(
			ResourceLocation template,
			Holder<StructureProcessorList> list,
			StructureTemplatePool.Projection proj
	) {
		super(Either.left(template), list, proj);
	}

	@Override
	protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean jigsaw) {
		return super.getSettings(rotation, box, jigsaw).setKeepLiquids(false);
	}

	public StructurePoolElementType<?> getType() {
		return YHWorldGen.SIMPLE.get();
	}


}
