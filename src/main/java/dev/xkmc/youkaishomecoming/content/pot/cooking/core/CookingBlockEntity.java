package dev.xkmc.youkaishomecoming.content.pot.cooking.core;

import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.base.TimedRecipeBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.overlay.IHintableBlock;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public abstract class CookingBlockEntity extends TimedRecipeBlockEntity<PotCookingRecipe<?>, CookingInv>
		implements BlockContainer, HeatableBlockEntity, IHintableBlock, InfoTile {

	@SerialClass.SerialField
	public final CookingItemContainer items = new CookingItemContainer(12).add(this);

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(items));

	private boolean recheckSoup = true;
	private ResourceLocation soupCache = SoupBaseRecipe.DEF;
	private List<ItemStack> floatingItems = new ArrayList<>();

	private @Nullable Player lastPlayer;

	public CookingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ResourceLocation getSoupCache() {
		return soupCache;
	}

	public List<ItemStack> getFloatingItems() {
		return floatingItems;
	}

	public abstract Item container();

	@Override
	public void tick() {
		super.tick();
		if (level != null && level.isClientSide() && recheckSoup) {
			recheckSoup = false;
			var cont = createContainer();
			var list = level.getRecipeManager().getRecipesFor(YHBlocks.SOUP_RT.get(), cont, level);
			int max = 0;
			soupCache = SoupBaseRecipe.DEF;
			SoupBaseRecipe<?> recipe = null;
			for (var e : list) {
				if (e.getIngredientCount() > max) {
					max = e.getIngredientCount();
					soupCache = e.id;
					recipe = e;
				} else if (e.getIngredientCount() == max) {
					if (e.id.compareTo(soupCache) < 0) {
						soupCache = e.id;
						recipe = e;
					}
				}
			}
			floatingItems = new ArrayList<>(cont.list());
			if (recipe != null) {
				recipe.removeConsumed(floatingItems);
			}
		}
	}

	public void setLastPlayer(Player player) {
		lastPlayer = player;
	}

	public boolean tryAddItem(ItemStack stack) {
		if (level == null) return false;
		List<ItemStack> list = new ArrayList<>();
		boolean empty = false;
		for (var e : items.getAsList()) {
			if (!e.isEmpty()) list.add(e);
			else empty = true;
		}
		if (!empty) return false;
		list.add(stack.copyWithCount(1));
		var inv = new CookingInv(container(), list, false);
		var opt = level.getRecipeManager().getRecipeFor(YHBlocks.COOKING_RT.get(), inv, level);
		if (opt.isEmpty()) return false;
		if (!level.isClientSide()) {
			items.addItem(stack.copyWithCount(1));
		}
		return true;
	}

	@Override
	protected RecipeType<PotCookingRecipe<?>> getRecipeType() {
		return YHBlocks.COOKING_RT.get();
	}

	@Override
	protected boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	protected boolean shouldStopProcessing(Level level) {
		return !isHeated(level, getBlockPos());
	}

	@Override
	protected CookingInv createContainer() {
		return createContainer(true);
	}

	protected CookingInv createContainer(boolean isComplete) {
		List<ItemStack> list = new ArrayList<>();
		for (var e : items.getAsList()) {
			if (!e.isEmpty()) list.add(e);
		}
		return new CookingInv(container(), list, isComplete);
	}

	protected void finishRecipe(Level level, PotCookingRecipe<?> recipe) {
		var ans = recipe.assemble(createContainer(), level.registryAccess());
		if (ans.getItem() instanceof BlockItem block) {
			var state = block.getBlock().defaultBlockState();
			if (state.hasProperty(BlockProxy.HORIZONTAL_FACING)) {
				state = state.setValue(BlockProxy.HORIZONTAL_FACING, getBlockState().getValue(BlockProxy.HORIZONTAL_FACING));
			}
			items.clear();
			level.setBlockAndUpdate(getBlockPos(), state);
		} else {
			items.clear();
			Block.popResource(level, getBlockPos().above(), ans);
			notifyTile();
		}
		if (lastPlayer instanceof ServerPlayer sp && lastPlayer.isAlive()) {
			YHCriteriaTriggers.COOKING.trigger(sp);
		}
		lastPlayer = null;
	}

	@Override
	public List<Container> getContainers() {
		SimpleContainer copy = new SimpleContainer(items.getContainerSize());
		for (int i = 0; i < items.getContainerSize(); i++) {
			var stack = items.getItem(i);
			if (stack.isEmpty() || stack.hasCraftingRemainingItem()) continue;
			copy.addItem(stack.copy());
		}
		return List.of(copy);
	}

	public void dumpInventory() {
		if (level == null) return;
		for (var e : getContainers())
			Containers.dropContents(level, this.getBlockPos().above(), e);
		items.clear();
		notifyTile();
		lastPlayer = null;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	public static boolean isHeatedPos(Level level, BlockPos pos) {
		BlockState down = level.getBlockState(pos.below());
		if (down.is(ModTags.HEAT_SOURCES)) {
			return down.hasProperty(BlockStateProperties.LIT) ? down.getValue(BlockStateProperties.LIT) : true;
		} else {
			if (down.is(ModTags.HEAT_CONDUCTORS)) {
				BlockState low = level.getBlockState(pos.below(2));
				if (low.is(ModTags.HEAT_SOURCES)) {
					if (low.hasProperty(BlockStateProperties.LIT)) {
						return low.getValue(BlockStateProperties.LIT);
					}
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		recheckSoup = true;
	}

	@Override
	public @Nullable TileTooltip getImage(boolean shift, BlockHitResult hit) {
		return null;
	}

	@Override
	public List<Component> lines(boolean shift, BlockHitResult hit) {
		if (inProgress() > 0) {
			return List.of(YHLangData.COOKING_PROGRESS.get(Math.round(inProgress() * 100) + "%"));
		}
		return List.of();
	}

	@Override
	public List<Ingredient> getHints(Level level, BlockPos pos) {
		if (inProgress() > 0) return List.of();
		var cont = createContainer(false);
		var recipes = level.getRecipeManager().getRecipesFor(getRecipeType(), cont, level);
		List<Ingredient> ans = new ArrayList<>();
		for (var e : recipes) {
			ans.addAll(e.getHints(level, cont));
		}
		return ans;
	}

}
