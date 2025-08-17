package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.world.FlatStructure;
import dev.xkmc.youkaishomecoming.content.world.YHSimplePiece;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class YHWorldGen {

	public static final RegistryEntry<StructureType<FlatStructure>> FLAT;
	public static final RegistryEntry<StructurePoolElementType<YHSimplePiece>> SIMPLE;

	static {
		FLAT = YoukaisHomecoming.REGISTRATE.simple("flat_check", Registries.STRUCTURE_TYPE,
				() -> () -> FlatStructure.CODEC);
		SIMPLE = YoukaisHomecoming.REGISTRATE.simple("no_liquid", Registries.STRUCTURE_POOL_ELEMENT,
				() -> () -> YHSimplePiece.CODEC);
	}

	public static void register() {

	}

}
