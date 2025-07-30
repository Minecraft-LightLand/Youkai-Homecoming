package dev.xkmc.youkaishomecoming.init.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BiomeTagCondition implements LootItemCondition {

	public final TagKey<Biome> tag;

	public BiomeTagCondition(TagKey<Biome> tag) {
		this.tag = tag;
	}

	@Override
	public LootItemConditionType getType() {
		return YHGLMProvider.BIOME_CHECK.get();
	}

	@Override
	public boolean test(LootContext ctx) {
		if (!ctx.hasParam(LootContextParams.ORIGIN)) return false;
		var pos = ctx.getParam(LootContextParams.ORIGIN);
		return ctx.getLevel().getBiome(BlockPos.containing(pos)).is(tag);
	}
}
