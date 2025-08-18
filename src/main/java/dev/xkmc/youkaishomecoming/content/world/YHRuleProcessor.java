package dev.xkmc.youkaishomecoming.content.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import dev.xkmc.youkaishomecoming.init.registrate.YHWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nullable;
import java.util.List;

public class YHRuleProcessor extends StructureProcessor {

	public static final Codec<YHRuleProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules")
			.xmap(YHRuleProcessor::new, e -> e.rules).codec();

	private final ImmutableList<ProcessorRule> rules;

	public YHRuleProcessor(List<? extends ProcessorRule> p_74296_) {
		this.rules = ImmutableList.copyOf(p_74296_);
	}

	@Nullable
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos absPos, BlockPos relPos, StructureTemplate.StructureBlockInfo rel, StructureTemplate.StructureBlockInfo old, StructurePlaceSettings settings) {
		RandomSource rand = RandomSource.create(Mth.getSeed(old.pos()));
		BlockState state = level.getBlockState(old.pos());

		for (ProcessorRule r : this.rules) {
			if (r.test(old.state(), state, rel.pos(), old.pos(), relPos, rand)) {
				return new StructureTemplate.StructureBlockInfo(old.pos(), old.state(), r.getOutputTag(rand, old.nbt()));
			}
		}

		return old;
	}

	protected StructureProcessorType<?> getType() {
		return YHWorldGen.RULE.get();
	}

}