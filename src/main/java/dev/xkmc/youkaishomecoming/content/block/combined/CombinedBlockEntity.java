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

	private static final String DEF = "oak";

	private String a = DEF, b = DEF;

	public CombinedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void set(String a, String b) {
		this.a = a;
		this.b = b;
	}

	@Nullable
	public IBlockSet getA() {
		return CombinedBlockSet.fetch(a);
	}

	@Nullable
	public IBlockSet getB() {
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
		tag.putString("BlockA", a);
		tag.putString("BlockB", b);
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public CompoundTag getUpdateTag(HolderLookup.Provider pvd) {
		CompoundTag ans = super.getUpdateTag(pvd);
		ans.putString("BlockA", a);
		ans.putString("BlockB", b);
		return ans;
	}

}
