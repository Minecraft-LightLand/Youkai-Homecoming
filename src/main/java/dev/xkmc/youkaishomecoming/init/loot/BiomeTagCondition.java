package dev.xkmc.youkaishomecoming.init.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BiomeTagCondition implements LootItemCondition {

	public static final MapCodec<BiomeTagCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
					RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(e -> e.tag))
			.apply(i, BiomeTagCondition::new));

	public final HolderSet<Biome> tag;

	public BiomeTagCondition(HolderSet<Biome> tag) {
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
		return tag.contains(ctx.getLevel().getBiome(BlockPos.containing(pos)));
	}
}
