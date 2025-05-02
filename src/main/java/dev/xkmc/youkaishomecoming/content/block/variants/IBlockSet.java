package dev.xkmc.youkaishomecoming.content.block.variants;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IBlockSet {

	String getName();

	BlockBehaviour.Properties prop();

	Holder<Block> base();

	Holder<Block> stairs();

	Holder<Block> slab();

	Holder<Block> vertical();

	ResourceLocation top();

	ResourceLocation side();


}
