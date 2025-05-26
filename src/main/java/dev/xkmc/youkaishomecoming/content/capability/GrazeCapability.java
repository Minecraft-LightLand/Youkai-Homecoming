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
	public int power, hidden, step, bomb, life, invul, weak;
	@SerialClass.SerialField
	public Map<UUID, CombatSession> sessions = new LinkedHashMap<>();

	private boolean dirty = false;
	private int tempGraze = 0;

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
			if (!full) sessions.clear();
			else {
				for (var ent : new ArrayList<>(sessions.entrySet())) {
					if (ent.getValue().shouldRemove(sl, player)) {
						sessions.remove(ent.getKey());
					}
				}
			}
			if (dirty)
				sync();
		}
		dirty = false;
		if (player.level().isClientSide)
			GrazeHelper.globalInvulTime = invul;
	}

	public void graze() {
		if (invul > 0) return;
		if (!EffectEventHandlers.isFullCharacter(player)) return;
		if (tempGraze < 10)
			tempGraze++;
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
			if (isDanmaku(player.getMainHandItem()) || isDanmaku(player.getOffhandItem()))
				return List.of(new InfoLine("%.2f".formatted(power * 0.01), icon, 10, 10));
			else return List.of();
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

	public void sync() {
		if (player instanceof ServerPlayer sp)
			HOLDER.network.toClientSyncAll(sp);
	}

	public static void register() {
	}

	@SerialClass
	public static class CombatSession {

		@SerialClass.SerialField
		public UUID uuid;
		@SerialClass.SerialField
		public int uid;

		private YoukaiEntity youkai;

		public CombatSession init(YoukaiEntity e) {
			uuid = e.getUUID();
			uid = e.getId();
			youkai = e;
			return this;
		}

		public boolean shouldRemove(ServerLevel sl, Player player) {
			if (youkai == null) {
				if (sl.getEntity(uuid) instanceof YoukaiEntity e)
					youkai = e;
				else return true;
			}
			if (!youkai.isAlive() || !EntityStorageHelper.isPresent(youkai))
				return true;
			if (!youkai.targets.contains(player))
				return true;
			return false;
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
