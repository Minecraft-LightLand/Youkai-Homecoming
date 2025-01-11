package dev.xkmc.youkaishomecoming.content.block.combined;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
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
		event.getLevel().setBlockAndUpdate(event.getPos(), state);
		if (!player.getAbilities().instabuild) {
			event.getItemStack().shrink(1);
		}
		event.setCanceled(true);
		event.setCancellationResult(InteractionResult.CONSUME);
		return true;
	}

	@Nullable
	private static BlockState getStateToReplace(PlayerInteractEvent.RightClickBlock event) {
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
				return a.base().value().defaultBlockState();
			}
			//TODO stair of same kind
			return null;
		}
		var set = CombinedBlockSet.get(a, b);
		if (set == null) return null;
		if (state.is(a.slab()) && b.slab().value() == bi.getBlock()) {
			var type = state.getValue(SlabBlock.TYPE);
			if (type == SlabType.DOUBLE) return null;
			return set.slab.get().defaultBlockState().setValue(CombinedSlabBlock.FACING,
					type == SlabType.TOP ^ a == set.a ? Direction.UP : Direction.DOWN);
		} else if (state.is(a.vertical()) && b.vertical().value() == bi.getBlock()) {
			var dir = state.getValue(HorizontalDirectionalBlock.FACING);
			if (a == set.a) dir = dir.getOpposite();
			return set.slab.get().defaultBlockState().setValue(CombinedSlabBlock.FACING, dir);
		}
		//TODO stair filling
		return null;
	}

	public final IBlockSet a, b;
	public final BlockEntry<CombinedSlabBlock> slab;
	//public final BlockEntry<CombinedStairsBlock> stairA, stairB;

	private CombinedBlockSet(L2Registrate reg, IBlockSet a, IBlockSet b) {
		this.a = a;
		this.b = b;
		this.slab = reg.block(a.getName() + "_slab_with_" + b.getName(), p -> new CombinedSlabBlock(a.prop()))
				.blockstate((ctx, pvd) -> CombinedSlabBlock.buildStates(ctx, pvd, a, b))
				.loot((pvd, block) -> CombinedSlabBlock.buildLoot(pvd, block, a, b))
				.register();
		/* TODO
		this.stairA = reg.block(a.name() + "_stairs" + "_with_" + b.name(), p -> new CombinedStairsBlock(a.prop()))
				.blockstate((ctx, pvd) -> CombinedStairsBlock.buildStates(ctx, pvd, a, b))
				.loot((pvd, block) -> CombinedStairsBlock.buildLoot(pvd, block, a, b))
				.register();
		this.stairB = reg.block(b.name() + "_stairs" + "_with_" + a.name(), p -> new CombinedStairsBlock(b.prop()))
				.blockstate((ctx, pvd) -> CombinedStairsBlock.buildStates(ctx, pvd, b, a))
				.loot((pvd, block) -> CombinedStairsBlock.buildLoot(pvd, block, b, a))
				.register();

		 */
	}

}
