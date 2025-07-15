package dev.xkmc.youkaishomecoming.content.pot.cooking.large.mid;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.PotClick;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class LargeCookingPotBlock implements ShapeBlockMethod {

	public static final BlockMethod INS = new LargeCookingPotBlock();
	public static final BlockMethod BE = new BlockEntityBlockMethodImpl<>(YHBlocks.LARGE_POT_BE, LargeCookingPotBlockEntity.class);
	public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 15, 15);

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, INS, new PotClick(YHBlocks.IRON_POT), BE, BlockProxy.HORIZONTAL);
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildState(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/short/short_iron_pot")))
				.texture("top", pvd.modLoc("block/bowl/short/short_iron_pot_top"))
				.texture("side", pvd.modLoc("block/bowl/short/short_iron_pot_side"))
				.texture("bottom", pvd.modLoc("block/bowl/short/short_iron_pot_bottom"))
				.renderType("cutout")
		);
	}

	public static void buildPotFood(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/bowl/short/" + ctx.getName())))
				.texture("top", pvd.modLoc("block/bowl/short/short_iron_pot_top"))
				.texture("side", pvd.modLoc("block/bowl/short/short_iron_pot_side"))
				.texture("bottom", pvd.modLoc("block/bowl/short/short_iron_pot_bottom"))
				.texture("base", pvd.modLoc("block/bowl/short/" + ctx.getName()))
				.renderType("cutout")
		);
	}

}
