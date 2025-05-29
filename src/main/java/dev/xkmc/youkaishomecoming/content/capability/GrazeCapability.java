package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.fastprojectileapi.collision.EntityStorageHelper;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SerialClass
public class GrazeCapability extends PlayerCapabilityTemplate<GrazeCapability> {

	public static final Capability<GrazeCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<GrazeCapability> HOLDER = new PlayerCapabilityHolder<>(
			YoukaisHomecoming.loc("graze"), CAPABILITY,
			GrazeCapability.class, GrazeCapability::new, PlayerCapabilityNetworkHandler::new
	);

	private static final int MAX_GRAZE = 100, SHARD = 5, CYCLE = 3;
	private static final int WEAK = 60, GRAZE_CACHE = 10;

	@SerialClass.SerialField
	private int power, hidden, step, bomb, life, invul, weak;
	@SerialClass.SerialField
	private Map<UUID, CombatSession> sessions = new LinkedHashMap<>();

	private boolean dirty = false;
	private int tempGraze = 0;
	private int lastGraze = 0;

	@Override
	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			power = 0;
			hidden = 0;
			step = 0;
			invul = 0;
			life = 0;
			bomb = 0;
			weak = 0;
			dirty = true;
		}
	}

	public void initStatus() {
		int initResource = GrazeHelper.getInitialResource(player) * SHARD;
		int initPower = GrazeHelper.getInitialPower(player) * MAX_GRAZE;
		life = Math.max(initResource, life);
		bomb = Math.max(initResource, bomb);
		power = Math.max(initPower, power);
	}

	@Override
	public void tick() {
		boolean full = EffectEventHandlers.isFullCharacter(player);
		if (tempGraze > 0) {
			tempGraze--;
			double val = GrazeHelper.getGrazeEffectiveness(player);
			int count = (int) val;
			if (player.getRandom().nextFloat() < val - count) count++;
			for (int i = 0; i < count; i++)
				consumeGraze();
			dirty = true;
		}
		if (invul > 0) invul--;
		if (weak > 0) weak--;
		int maxPower = GrazeHelper.getMaxPower(player) * MAX_GRAZE;
		int maxResource = GrazeHelper.getMaxResource(player) * SHARD;
		if (power > maxPower) power = maxPower;
		if (life > maxResource) life = maxResource;
		if (bomb > maxResource) bomb = maxResource;
		if (player.level() instanceof ServerLevel sl) {
			if (!full) {
				dirty = !sessions.isEmpty();
				sessions.clear();
			} else {
				for (var ent : new ArrayList<>(sessions.entrySet())) {
					if (ent.getValue().youkai == null) dirty = true;
					if (ent.getValue().shouldRemove(sl, player)) {
						sessions.remove(ent.getKey());
						dirty = true;
					}
				}
			}
			if (dirty)
				sync();
		}
		dirty = false;
		if (player.level().isClientSide) {
			GrazeHelper.globalInvulTime = invul;
			GrazeHelper.globalForbidTime = Math.max(invul, weak);
		}
	}

	public boolean graze() {
		if (invul > 0) return false;
		if (!EffectEventHandlers.isFullCharacter(player)) return false;
		if (tempGraze < GRAZE_CACHE)
			tempGraze++;
		boolean ans = player.tickCount != lastGraze;
		lastGraze = player.tickCount;
		return ans;
	}

	private void consumeGraze() {
		if (power < GrazeHelper.getMaxPower(player) * MAX_GRAZE) {
			power++;
			return;
		}
		if (sessions.isEmpty()) return;
		hidden++;
		if (hidden < MAX_GRAZE) return;
		hidden -= MAX_GRAZE;
		step++;
		int max = GrazeHelper.getMaxResource(player) * SHARD;
		if (step == CYCLE) {
			if (life < max) {
				life++;
				step = 0;
			} else if (bomb < max) {
				bomb++;
				step--;
			} else {
				step--;
			}
		} else {
			if (bomb < max) bomb++;
			else if (life < max) step++;
		}
	}

	public HitType performErase(YoukaiEntity e) {
		if (!EffectEventHandlers.isFullCharacter(player)) return HitType.NONE;
		if (!sessions.containsKey(e.getUUID())) return HitType.ERASE;
		if (invul > 0) return HitType.INVUL;
		for (var s : sessions.values()) {
			s.eraseDanmaku(player);
		}
		if (useBomb()) {
			return HitType.BOMB;
		}
		int maxLoss = (int) (YHModConfig.COMMON.maxPowerLossOnMiss.get() * MAX_GRAZE);
		int loss = Math.min(power / 2, maxLoss);
		power -= loss;
		dirty = true;
		invul = YHModConfig.COMMON.missInvulTime.get();
		if (player instanceof ServerPlayer sp) {
			YoukaisHomecoming.HANDLER.toClientPlayer(new GrazeHelper.GrazeToClient().set(1), sp);
			SpellContainer.clear(sp);
		}
		if (life < SHARD) {
			for (var s : sessions.values()) {
				s.resetTarget(player);
			}
			sessions.clear();
			weak = WEAK;
			return HitType.LAST;
		}
		life -= SHARD;
		bomb = GrazeHelper.getInitialResource(player) * SHARD;
		return HitType.LIFE;
	}

	public boolean useBomb() {
		if (bomb < SHARD) return false;
		bomb -= SHARD;
		invul = YHModConfig.COMMON.bombInvulTime.get();
		dirty = true;
		return true;
	}

	public float powerBonus() {
		if (!EffectEventHandlers.isFullCharacter(player)) return 0;
		int support = power / MAX_GRAZE;
		return support * YHModConfig.COMMON.danmakuPowerBonus.get().floatValue();
	}

	public List<InfoLine> getInfoLines() {
		if (!EffectEventHandlers.isFullCharacter(player)) return List.of();
		var icon = new InfoIcon(
				YoukaisHomecoming.loc("textures/gui/elements.png"),
				20, 20
		);
		if (sessions.isEmpty()) {
			boolean holding = player.getMainHandItem().is(YHTagGen.DANMAKU_SHOOTER) ||
					player.getOffhandItem().is(YHTagGen.DANMAKU_SHOOTER);
			boolean bypass = player.getAbilities().instabuild && player.isShiftKeyDown();
			if (!holding) return List.of();
			if (!bypass) {
				return List.of(new InfoLine("%.2f".formatted(power * 0.01), icon, 10, 10));
			}

		}
		return List.of(
				new InfoLine("%.1f".formatted(life * 1d / SHARD), icon, 0, 10),
				new InfoLine("%.1f".formatted(bomb * 1d / SHARD), icon, 0, 0),
				new InfoLine("%.2f".formatted(power * 1d / MAX_GRAZE), icon, 10, 10),
				new InfoLine("%.2f".formatted(hidden * 1d / MAX_GRAZE), icon, 10, 0)
		);
	}

	public boolean isInSession(UUID uuid) {
		return sessions.containsKey(uuid);
	}

	public void initSession(YoukaiEntity youkai) {
		if (sessions.containsKey(youkai.getUUID())) return;
		if (sessions.isEmpty()) initStatus();
		sessions.put(youkai.getUUID(), new CombatSession().init(youkai));
		youkai.targets.add(player);
		dirty = true;
	}

	public void stopSession(UUID uuid) {
		if (!sessions.containsKey(uuid)) return;
		sessions.remove(uuid);
		if (sessions.isEmpty() && player instanceof ServerPlayer sp) {
			SpellContainer.clear(sp);
		}
		dirty = true;
	}

	public boolean shouldHurt(LivingEntity le) {
		if (!EffectEventHandlers.isFullCharacter(player)) return true;
		if (le instanceof YoukaiEntity youkai) {
			if (weak > 0) return false;
			if (sessions.containsKey(youkai.getUUID())) return true;
			if (youkai.targets.contains(player)) return true;
			if (sessions.isEmpty()) {
				initSession(youkai);
				return true;
			}
			return false;
		}
		return sessions.isEmpty() || le instanceof Mob mob && mob.getTarget() == player;
	}

	public Optional<LivingEntity> findAny(Player player) {
		return sessions.values().stream().findAny().map(e -> e.getTarget(player));
	}

	public void remove(UUID uuid) {
		sessions.remove(uuid);
	}

	public void setLife(int i) {
		life = i;
		dirty = true;
	}

	public void setBomb(int i) {
		bomb = i;
		dirty = true;
	}

	public void setPower(int i) {
		power = i;
		dirty = true;
	}

	public boolean isInvul() {
		return invul > 0;
	}

	public boolean isWeak() {
		return weak > 0;
	}

	public int getLife() {
		return life;
	}

	public int getBomb() {
		return bomb;
	}

	public int getPower() {
		return power;
	}

	public void sync() {
		if (player instanceof ServerPlayer sp)
			HOLDER.network.toClientSyncAll(sp);
	}

	public static void register() {
	}

	@SerialClass
	public static class CombatSession {

		@SerialClass.SerialField
		private UUID uuid;
		@SerialClass.SerialField
		private int uid;

		private YoukaiEntity youkai;

		public CombatSession init(YoukaiEntity e) {
			uuid = e.getUUID();
			uid = e.getId();
			youkai = e;
			return this;
		}

		public boolean shouldRemove(ServerLevel sl, Player player) {
			if (youkai == null) {
				if (sl.getEntity(uuid) instanceof YoukaiEntity e) {
					youkai = e;
					uid = youkai.getId();
				} else return true;
			}
			if (!youkai.isAlive() || !EntityStorageHelper.isPresent(youkai))
				return true;
			return !youkai.targets.contains(player);
		}

		@Nullable
		public LivingEntity getTarget(Player player) {
			if (youkai != null) return youkai;
			return player.level().getEntity(uid) instanceof LivingEntity le ? le : null;
		}

		protected void resetTarget(Player player) {
			if (getTarget(player) instanceof YoukaiEntity e) {
				e.resetTarget(player);
			}
		}

		protected void eraseDanmaku(Player player) {
			if (getTarget(player) instanceof YoukaiEntity e) {
				e.eraseAllDanmaku(player);
			}
		}

	}

	public record InfoLine(String text, InfoIcon icon, int x, int y) {

	}

	public record InfoIcon(ResourceLocation loc, int w, int h) {

	}

	public enum HitType {
		NONE, INVUL, BOMB, LIFE, ERASE, LAST;

		public boolean skipDamage() {
			return this == BOMB || this == LIFE || this == INVUL || this == LAST;
		}

		public boolean erase() {
			return this == BOMB || this == LIFE || this == ERASE || this == LAST;
		}

	}

}
