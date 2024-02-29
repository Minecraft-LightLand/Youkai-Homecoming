package dev.xkmc.youkaishomecoming.content.block.plant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.ForgeHooks;

import java.util.function.Supplier;

public class YHBushBlock extends BushBlock {

	public static final BooleanProperty FLOWERING = BlockStateProperties.BLOOM;
	private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	protected final Supplier<Item> fruit;

	public YHBushBlock(Properties pProperties, Supplier<Item> fruit) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FLOWERING, false));
		this.fruit = fruit;
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public boolean isRandomlyTicking(BlockState state) {
		return !state.getValue(FLOWERING);
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(FLOWERING)) {
			if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
				BlockState next = state.setValue(FLOWERING, true);
				level.setBlock(pos, next, 2);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(next));
				ForgeHooks.onCropsGrowPost(level, pos, state);
			}
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FLOWERING);
	}

	public static void buildModel(DataGenContext<Block, ? extends YHBushBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			String tex = ctx.getName() + (state.getValue(YHBushBlock.FLOWERING) ? "_flowering" : "");
			return ConfiguredModel.builder().modelFile(pvd.models()
					.cross(tex, pvd.modLoc("block/" + tex))).build();
		});
	}
}
