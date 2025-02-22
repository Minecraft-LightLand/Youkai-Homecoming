package dev.xkmc.youkaishomecoming.content.block.combined;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CombinedBlockEntity extends BlockEntity {

	private String a = null, b = null;

	public CombinedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void set(@Nullable String a, @Nullable String b) {
		this.a = a;
		this.b = b;
	}

	@Nullable
	public IBlockSet getA() {
		if (a == null) return null;
		return CombinedBlockSet.fetch(a);
	}

	@Nullable
	public IBlockSet getB() {
		if (b == null) return null;
		return CombinedBlockSet.fetch(b);
	}

	public void loadAdditional(CompoundTag tag, HolderLookup.Provider pvd) {
		super.loadAdditional(tag, pvd);
		if (tag.contains("BlockA", CompoundTag.TAG_STRING)) {
			a = tag.getString("BlockA");
		}
		if (tag.contains("BlockB", CompoundTag.TAG_STRING)) {
			b = tag.getString("BlockB");
		}
	}

	public void saveAdditional(CompoundTag tag, HolderLookup.Provider pvd) {
		super.saveAdditional(tag, pvd);
		if (a != null) tag.putString("BlockA", a);
		if (b != null) tag.putString("BlockB", b);
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public CompoundTag getUpdateTag(HolderLookup.Provider pvd) {
		CompoundTag ans = super.getUpdateTag(pvd);
		if (a != null) ans.putString("BlockA", a);
		if (b != null) ans.putString("BlockB", b);
		return ans;
	}

}
