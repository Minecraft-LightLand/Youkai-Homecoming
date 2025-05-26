package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.fastprojectileapi.collision.EntityStorageHelper;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.item.danmaku.DanmakuItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.ISpellItem;
import dev.xkmc.youkaishomecoming.content.item.danmaku.LaserItem;
import dev.xkmc.youkaishomecoming.content.spell.item.SpellContainer;
import dev.xkmc.youkaishomecoming.events.EffectEventHandlers;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

	private static final int MAX_GRAZE = 100, MAX_POWER = 4 * 100;
	private static final int INIT_BOMB = 2 * 5, INIT_LIFE = 5 * 2, MAX = 5 * 10, CYCLE = 3;
	private static final int LIFE_INVUL = 60, BOMB_INVUL = 30, WEAK = 60;

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
		life = Math.max(INIT_LIFE, life);
		bomb = Math.max(INIT_BOMB, bomb);
	}

	@Override
	public void tick() {
		boolean full = EffectEventHandlers.isFullCharacter(player);
		if (tempGraze > 0) {
			tempGraze--;
			consumeGraze();
			dirty = true;
		}
		if (invul > 0) invul--;
		if (weak > 0) weak = 0;
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
		if (tempGraze < 10)
			tempGraze++;
		boolean ans = player.tickCount != lastGraze;
		lastGraze = player.tickCount;
		return ans;
	}

	private void consumeGraze() {
		if (power < MAX_POWER) {
			power++;
			return;
		}
		if (sessions.isEmpty()) return;
		hidden++;
		if (hidden < MAX_GRAZE) return;
		hidden -= MAX_GRAZE;
		step++;
		if (step == CYCLE) {
			if (life < MAX)
				life++;
			else if (bomb < MAX)
				bomb++;
			step = 0;
		} else {
			if (bomb < MAX)
				bomb++;
		}
	}

	public HitType performErase(YoukaiEntity e) {
		if (!EffectEventHandlers.isFullCharacter(player)) return HitType.NONE;
		if (!sessions.containsKey(e.getUUID())) return HitType.ERASE;
		if (invul > 0) return HitType.INVUL;
		if (useBomb()) {
			return HitType.BOMB;
		}
		int loss = Math.min(power / 2, MAX_GRAZE);
		power -= loss;
		dirty = true;
		invul = LIFE_INVUL;
		if (player instanceof ServerPlayer sp) {
			YoukaisHomecoming.HANDLER.toClientPlayer(new GrazeHelper.GrazeToClient().set(1), sp);
			SpellContainer.clear(sp);
		}
		if (life < 5) {
			sessions.clear();
			weak = WEAK;
			return HitType.LAST;
		}
		life -= 5;
		bomb = INIT_BOMB;
		return HitType.LIFE;
	}

	public boolean useBomb() {
		if (bomb < 5) return false;
		bomb -= 5;
		invul = BOMB_INVUL;
		dirty = true;
		return true;
	}

	public float powerBonus() {
		if (!EffectEventHandlers.isFullCharacter(player)) return 0;
		int support = power / 100;
		return support * 0.25f;
	}

	public List<InfoLine> getInfoLines() {
		if (!EffectEventHandlers.isFullCharacter(player)) return List.of();
		var icon = new InfoIcon(
				YoukaisHomecoming.loc("textures/gui/elements.png"),
				20, 20
		);
		if (sessions.isEmpty()) {
			boolean holding = isDanmaku(player.getMainHandItem()) || isDanmaku(player.getOffhandItem());
			boolean bypass = player.getAbilities().instabuild && player.isShiftKeyDown();
			if (!holding) return List.of();
			if (!bypass)
				return List.of(new InfoLine("%.2f".formatted(power * 0.01), icon, 10, 10));

		}
		return List.of(
				new InfoLine("%.1f".formatted(life * 0.2), icon, 0, 10),
				new InfoLine("%.1f".formatted(bomb * 0.2), icon, 0, 0),
				new InfoLine("%.2f".formatted(power * 0.01), icon, 10, 10),
				new InfoLine("%.2f".formatted(hidden * 0.01), icon, 10, 0)
		);
	}

	private static boolean isDanmaku(ItemStack stack) {
		return stack.getItem() instanceof DanmakuItem || stack.getItem() instanceof LaserItem ||
				stack.getItem() instanceof ISpellItem;
	}

	public void initSession(YoukaiEntity youkai) {
		sessions.put(youkai.getUUID(), new CombatSession().init(youkai));
		youkai.targets.add(player);
		dirty = true;
	}

	public boolean shouldHurt(LivingEntity le) {
		if (!EffectEventHandlers.isFullCharacter(player)) return true;
		if (le instanceof YoukaiEntity youkai) {
			if (weak > 0) return false;
			if (sessions.containsKey(youkai.getUUID())) return true;
			if (youkai.targets.contains(player)) return true;
			if (sessions.isEmpty()) {
				initStatus();
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

	public boolean forbidDanmaku() {
		return weak > 0 || invul > 0;
	}

	public boolean isInvul() {
		return invul > 0;
	}

	public boolean isWeak() {
		return weak > 0;
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

		public boolean resetTarget() {
			return this == LAST;
		}

	}

}
