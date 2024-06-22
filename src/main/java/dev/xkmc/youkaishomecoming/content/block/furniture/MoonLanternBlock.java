package dev.xkmc.youkaishomecoming.content.block.furniture;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoonLanternBlock extends LanternBlock {

	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(5,0,5,11,2,11),
			Block.box(4,2,4,12,7,12),
			Block.box(3,7,3,13,13,13),
			Block.box(6,13,6,10,16,10)
	);

	public MoonLanternBlock(Properties pProperties) {
		super(pProperties);
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildStates(DataGenContext<Block, MoonLanternBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + ctx.getName())))
				.texture("all", pvd.modLoc("block/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/mangrove_planks"))
				.renderType("cutout"));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.MOON_LANTERN_PLACE.get());
		list.add(YHLangData.MOON_LANTERN_HOLD.get());
	}

}
