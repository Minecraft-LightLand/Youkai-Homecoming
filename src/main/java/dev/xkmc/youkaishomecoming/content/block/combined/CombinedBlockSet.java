package dev.xkmc.youkaishomecoming.content.block.combined;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Either;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CombinedBlockSet {

	private static final Table<String, String, CombinedBlockSet> TABLE = HashBasedTable.create();
	private static final Map<String, IBlockSet> NAME_2_SET = new LinkedHashMap<>();
	private static final Map<Block, IBlockSet> BLOCK_2_SET = new LinkedHashMap<>();

	public static CombinedBlockSet any() {
		return TABLE.values().stream().findFirst().orElseThrow();
	}

	private static boolean verify(IBlockSet set) {
		var id = set.getName();
		var old = NAME_2_SET.get(id);
		if (old != null && old != set) return false;
		if (old == null) NAME_2_SET.put(id, set);
		return true;
	}

	@Nullable
	public static IBlockSet fetch(Block block) {
		return BLOCK_2_SET.get(block);
	}

	@Nullable
	public static IBlockSet fetch(String block) {
		return NAME_2_SET.get(block);
	}

	public static void init() {
		for (var e : NAME_2_SET.values()) {
			BLOCK_2_SET.put(e.base().value(), e);
			BLOCK_2_SET.put(e.slab().value(), e);
			BLOCK_2_SET.put(e.stairs().value(), e);
			BLOCK_2_SET.put(e.vertical().value(), e);
		}
	}

	@Nullable
	public static CombinedBlockSet get(IBlockSet a, IBlockSet b) {
		var sa = a.getName();
		var sb = b.getName();
		if (sa.compareTo(sb) < 0) {
			return TABLE.get(sa, sb);
		} else {
			return TABLE.get(sb, sa);
		}
	}

	public synchronized static void add(L2Registrate reg, IBlockSet a, IBlockSet b) {
		if (a == b) return;
		if (!verify(a) || !verify(b)) return;
		var sa = a.getName();
		var sb = b.getName();
		if (sa.compareTo(sb) > 0) {
			var t = a;
			a = b;
			b = t;
			sa = a.getName();
			sb = b.getName();
		}
		var ans = TABLE.get(sa, sb);
		if (ans != null) return;
		TABLE.put(sa, sb, new CombinedBlockSet(reg, a, b));
	}

	public static boolean onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		var state = getStateToReplace(event);
		if (state == null) return false;
		var player = event.getEntity();
		if (state.left().isPresent())
			event.getLevel().setBlock(event.getPos(), state.left().get(), 18);
		if (state.right().isPresent()) {
			var complex = state.right().get();
			event.getLevel().setBlock(event.getPos(), complex.state(), 18);
			if (event.getLevel().getBlockEntity(event.getPos()) instanceof CombinedBlockEntity be) {
				be.set(complex.a().getName(), complex.b().getName());
			}
		}
		if (!player.getAbilities().instabuild) {
			event.getItemStack().shrink(1);
		}
		event.setCanceled(true);
		event.setCancellationResult(InteractionResult.CONSUME);
		return true;
	}

	@Nullable
	private static Either<BlockState, Complex> getStateToReplace(PlayerInteractEvent.RightClickBlock event) {
		var state = event.getLevel().getBlockState(event.getPos());
		var stack = event.getItemStack();
		if (!(stack.getItem() instanceof BlockItem bi)) return null;
		var a = CombinedBlockSet.fetch(state.getBlock());
		var b = CombinedBlockSet.fetch(bi.getBlock());
		if (a == null || b == null) return null;
		var inside = AABB.encapsulatingFullBlocks(event.getPos(), event.getPos())
				.deflate(0.01).contains(event.getHitVec().getLocation());
		if (!inside) return null;
		if (a == b) {
			if (state.is(a.vertical()) && b.vertical().value() == bi.getBlock()) {
				return Either.left(a.base().value().defaultBlockState());
			}
			if (state.is(a.stairs()) && b.vertical().value() == bi.getBlock()) {
				return Either.left(a.base().value().defaultBlockState());
			}
			return null;
		}
		var set = CombinedBlockSet.get(a, b);
		if (set != null) {
			if (state.is(a.slab()) && b.slab().value() == bi.getBlock()) {
				var type = state.getValue(SlabBlock.TYPE);
				if (type == SlabType.DOUBLE) return null;
				return Either.left(set.slab.get().defaultBlockState().setValue(CombinedSlabBlock.FACING,
						type == SlabType.TOP ^ a == set.a ? Direction.UP : Direction.DOWN));
			} else if (state.is(a.vertical()) && b.vertical().value() == bi.getBlock()) {
				var dir = state.getValue(HorizontalDirectionalBlock.FACING);
				if (a == set.a) dir = dir.getOpposite();
				return Either.left(set.slab.get().defaultBlockState().setValue(CombinedSlabBlock.FACING, dir));
			} else if (state.is(a.stairs()) && b.vertical().value() == bi.getBlock()) {
				var block = set.a == a ? set.stairA : set.stairB;
				return Either.left(block.get().defaultBlockState()
						.setValue(CombinedStairsBlock.FACING, state.getValue(StairBlock.FACING))
						.setValue(CombinedStairsBlock.HALF, state.getValue(StairBlock.HALF))
						.setValue(CombinedStairsBlock.SHAPE, state.getValue(StairBlock.SHAPE)));
			}
			return null;
		} else {
			if (state.is(a.slab()) && b.slab().value() == bi.getBlock()) {
				var type = state.getValue(SlabBlock.TYPE);
				if (type == SlabType.DOUBLE) return null;
				return Either.right(new Complex(YHBlocks.COMPLEX_SLAB.get().defaultBlockState().setValue(CombinedSlabBlock.FACING,
						type == SlabType.TOP ? Direction.UP : Direction.DOWN), a, b));
			} else if (state.is(a.vertical()) && b.vertical().value() == bi.getBlock()) {
				var dir = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
				return Either.right(new Complex(YHBlocks.COMPLEX_SLAB.get().defaultBlockState().setValue(CombinedSlabBlock.FACING, dir), a, b));
			} else if (state.is(a.stairs()) && b.vertical().value() == bi.getBlock()) {
				return Either.right(new Complex(YHBlocks.COMPLEX_STAIRS.get().defaultBlockState()
						.setValue(CombinedStairsBlock.FACING, state.getValue(StairBlock.FACING))
						.setValue(CombinedStairsBlock.HALF, state.getValue(StairBlock.HALF))
						.setValue(CombinedStairsBlock.SHAPE, state.getValue(StairBlock.SHAPE)), a, b));
			}
			return null;
		}
	}

	private record Complex(BlockState state, IBlockSet a, IBlockSet b) {

	}

	public final IBlockSet a, b;
	public final BlockEntry<CombinedSlabBlock> slab;
	public final BlockEntry<CombinedStairsBlock> stairA, stairB;

	private CombinedBlockSet(L2Registrate reg, IBlockSet a, IBlockSet b) {
		this.a = a;
		this.b = b;
		this.slab = reg.block(a.getName() + "_slab_with_" + b.getName(), p -> new CombinedSlabBlock(a.prop()))
				.blockstate((ctx, pvd) -> CombinedSlabBlock.buildStates(ctx, pvd, a, b))
				.loot((pvd, block) -> CombinedSlabBlock.buildLoot(pvd, block, a, b))
				.simpleItem()
				.register();

		this.stairA = reg.block(a.getName() + "_stairs_with_" + b.getName(), p -> new CombinedStairsBlock(a.prop()))
				.blockstate((ctx, pvd) -> CombinedStairsBlock.buildStates(ctx, pvd, a, b))
				.loot((pvd, block) -> CombinedStairsBlock.buildLoot(pvd, block, a, b))
				.simpleItem()
				.register();
		this.stairB = reg.block(b.getName() + "_stairs_with_" + a.getName(), p -> new CombinedStairsBlock(b.prop()))
				.blockstate((ctx, pvd) -> CombinedStairsBlock.buildStates(ctx, pvd, b, a))
				.loot((pvd, block) -> CombinedStairsBlock.buildLoot(pvd, block, b, a))
				.simpleItem()
				.register();
	}

}
