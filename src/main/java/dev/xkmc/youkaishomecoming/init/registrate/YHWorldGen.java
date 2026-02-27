package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.content.world.NoisePlacement;
import dev.xkmc.youkaishomecoming.content.world.FlatStructure;
import dev.xkmc.youkaishomecoming.content.world.YHRuleProcessor;
import dev.xkmc.youkaishomecoming.content.world.YHSimplePiece;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class YHWorldGen {

	public static final RegistryEntry<StructureType<FlatStructure>> FLAT;
	public static final RegistryEntry<StructurePoolElementType<YHSimplePiece>> SIMPLE;
	public static final RegistryEntry<StructureProcessorType<YHRuleProcessor>> RULE;
	public static final RegistryEntry<PlacementModifierType<NoisePlacement>> NOISE;

	static {
		FLAT = YoukaisHomecoming.REGISTRATE.simple("flat_check", Registries.STRUCTURE_TYPE,
				() -> () -> FlatStructure.CODEC);
		SIMPLE = YoukaisHomecoming.REGISTRATE.simple("no_liquid", Registries.STRUCTURE_POOL_ELEMENT,
				() -> () -> YHSimplePiece.CODEC);
		RULE = YoukaisHomecoming.REGISTRATE.simple("set_nbt_rules", Registries.STRUCTURE_PROCESSOR,
				() -> () -> YHRuleProcessor.CODEC);

		NOISE = YoukaisHomecoming.REGISTRATE.simple("noise", Registries.PLACEMENT_MODIFIER_TYPE,
				() -> () -> NoisePlacement.CODEC);
	}

	public static void register() {

	}

}
