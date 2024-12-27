package dev.xkmc.youkaishomecoming.content.spell.item;

import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.CardHolder;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.Ticker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

@SerialClass
public class ItemSpell {

	@SerialClass.SerialField
	private final ArrayList<Ticker<?>> tickers = new ArrayList<>();
	@SerialClass.SerialField
	private UUID targetId;
	@SerialClass.SerialField
	public Vec3 dir = new Vec3(1, 0, 0), targetPos;

	private LivingEntity targetCache;
	protected CardHolder holder;

	public void start(Player player, @Nullable LivingEntity target) {
		this.dir = RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 1);
		if (target != null) {
			targetId = target.getUUID();
			targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
			targetCache = target;
		}
	}

	@Nullable
	LivingEntity getTarget(ServerLevel sl) {
		if (targetCache != null) {
			if (targetCache.isAlive()) {
				return targetCache;
			} else {
				targetId = null;
			}
		}
		if (targetId == null) return null;
		var e = sl.getEntity(targetId);
		if (e instanceof LivingEntity le && le.isAlive()) {
			targetCache = le;
			return le;
		} else {
			targetId = null;
			return null;
		}
	}

	public boolean tick(Player player) {
		if (!(player instanceof ServerPlayer sp)) return true;
		var target = getTarget(sp.serverLevel());
		if (target != null) targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
		holder = new PlayerHolder(player, dir, this, target);
		tickers.removeIf(e -> e.tick(holder, Wrappers.cast(this)));
		return tickers.isEmpty();
	}

	protected <T extends ItemSpell> void addTicker(Ticker<T> tick) {
		tickers.add(tick);
	}

}
