package dev.xkmc.youkaishomecoming.content.block.donation;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@SerialClass
public class DonationBoxBlockEntity extends BaseBlockEntity {

	@SerialClass.SerialField
	private UUID lastPlayer = null;

	@SerialClass.SerialField
	private final HashMap<UUID, Donation> donations = new HashMap<>();

	public DonationBoxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void take(@Nullable Player player, ItemStack stack) {
		if (!(level instanceof ServerLevel sl)) return;
		if (player != null) {
			lastPlayer = player.getUUID();
		}
		if (lastPlayer == null) return;
		if (stack.is(Items.EMERALD) || stack.is(Items.GOLD_INGOT)) {
			int count = stack.getCount();
			var old = donations.computeIfAbsent(lastPlayer, k -> new Donation());
			stack.setCount(0);
			old.add(getBlockPos(), sl, player, count);
			setChanged();
		}
	}

	@SerialClass
	public static class Donation {

		@SerialClass.SerialField
		public int rewarded, donation;

		public void add(BlockPos pos, ServerLevel level, @Nullable Player player, int count) {
			donation += count;
			if (player == null) return;
			if (donation - rewarded >= 8) {
				rewarded += 8;
				var list = level.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
						player.getBoundingBox().inflate(32), LivingEntity::isAlive);
				if (list.isEmpty()) {
					var e = YHEntities.REIMU.create(level);
					if (e != null) {
						e.moveTo(Vec3.atCenterOf(pos.above()));
						TouhouSpellCards.setReimu(e);
						level.addFreshEntity(e);
					}
				} else {
					for (var e : list) {
						e.moveTo(Vec3.atCenterOf(pos.above()));
					}
				}
			}
		}

	}

}
