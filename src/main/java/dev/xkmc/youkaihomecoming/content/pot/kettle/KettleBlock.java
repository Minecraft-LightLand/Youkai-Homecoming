package dev.xkmc.youkaihomecoming.content.pot.kettle;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlock;
import dev.xkmc.youkaihomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaihomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.Lazy;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import javax.annotation.Nullable;
import java.util.Map;

public class KettleBlock extends BasePotBlock {

	protected static final VoxelShape SHAPE = box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

	protected static final Lazy<Map<Ingredient, Integer>> MAP = Lazy.of(() -> Map.of(
			Ingredient.of(Items.WATER_BUCKET), KettleBlockEntity.WATER_BUCKET,
			new PotionIngredient(Potions.WATER), KettleBlockEntity.WATER_BOTTLE));

	public KettleBlock(Properties prop) {
		super(prop);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		for (var e : MAP.get().entrySet()) {
			if (e.getKey().test(stack)) {
				if (!level.isClientSide()) {
					if (level.getBlockEntity(pos) instanceof KettleBlockEntity kettle) {
						if (kettle.getWater() < KettleBlockEntity.WATER_BUCKET) {
							kettle.addWater(e.getValue());
							if (!player.getAbilities().instabuild) {
								ItemStack remain = stack.getCraftingRemainingItem();
								stack.shrink(1);
								player.getInventory().placeItemBackInInventory(remain);
							}
							return InteractionResult.SUCCESS;
						}
					}
					return InteractionResult.FAIL;
				}
				return InteractionResult.CONSUME;
			}
		}
		return super.use(state, level, pos, player, hand, result);
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return YHBlocks.KETTLE_BE.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		return level.isClientSide ? createTickerHelper(blockEntity, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::animationTick) :
				createTickerHelper(blockEntity, YHBlocks.KETTLE_BE.get(), BasePotBlockEntity::cookingTick);
	}


	public static void buildModel(DataGenContext<Block, KettleBlock> ctx, RegistrateBlockstateProvider pvd) {
		var kettle = pvd.models().getBuilder("block/kettle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle")))
				.texture("kettle", pvd.modLoc("block/kettle"))
				.renderType("cutout");
		var handle = pvd.models().getBuilder("block/kettle_handle")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle_handle")))
				.texture("kettle", pvd.modLoc("block/kettle"))
				.texture("handle", pvd.modLoc("block/cooking_pot_handle"))
				.texture("chain", pvd.modLoc("block/chain"))
				.renderType("cutout");
		var tray = pvd.models().getBuilder("block/kettle_tray")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/kettle_tray")))
				.texture("kettle", pvd.modLoc("block/kettle"))
				.texture("tray_side", pvd.modLoc("block/cooking_pot_tray_side"))
				.texture("tray_top", pvd.modLoc("block/cooking_pot_tray_top"))
				.renderType("cutout");
		pvd.horizontalBlock(ctx.get(), state -> switch (state.getValue(SUPPORT)) {
			case NONE -> kettle;
			case HANDLE -> handle;
			case TRAY -> tray;
		});
	}

}
