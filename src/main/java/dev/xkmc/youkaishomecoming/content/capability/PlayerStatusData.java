package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.Set;

@SerialClass
public class PlayerStatusData extends PlayerCapabilityTemplate<PlayerStatusData> {

	public static final Capability<PlayerStatusData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<PlayerStatusData> HOLDER = new PlayerCapabilityHolder<>(
			YoukaisHomecoming.loc("player_status"), CAPABILITY,
			PlayerStatusData.class, PlayerStatusData::new, PlayerCapabilityNetworkHandler::new
	);

	public enum Status {
		HUMAN,
		YOUKAIFYING, YOUKAIFIED,
		FAIRY,
		HAUNTED, GHOST,
		MAGICIAN,
		;

		public boolean is(LivingEntity e) {
			return e instanceof Player player && player.isAlive() &&
					HOLDER.get(player).status == this;
		}

		public boolean clearedBySober() {
			return Kind.YOUKAI.set.contains(this);
		}

	}

	public enum Kind {
		CHARACTER(Status.YOUKAIFYING, Status.YOUKAIFIED, Status.FAIRY, Status.HAUNTED, Status.GHOST, Status.MAGICIAN),
		TEMPTING(Status.YOUKAIFYING, Status.HAUNTED),
		WORTHY(Status.YOUKAIFIED, Status.GHOST, Status.MAGICIAN),
		YOUKAI(Status.YOUKAIFYING, Status.YOUKAIFIED);

		private final Set<Status> set;

		Kind(Status... set) {
			this.set = Set.of(set);
		}

		public boolean is(LivingEntity e) {
			return e instanceof Player player && player.isAlive() &&
					set.contains(HOLDER.get(player).status);
		}

	}

	@SerialClass.SerialField
	public Status status = Status.HUMAN;
	@SerialClass.SerialField
	public int time;

	private int lock;

	@Override
	public void onClone(boolean isWasDeath) {
		if (isWasDeath) {
			status = Status.HUMAN;
			lock = 0;
			time = 0;
			sync();
		}
	}

	@Override
	public void tick() {
		if (time > 0) {
			if (lock > 0)
				lock--;
			else {
				time--;
				if (time == 0) {
					status = Status.HUMAN;
					sync();
				}
			}
		}
		if (status.clearedBySober() && player.hasEffect(YHEffects.SOBER.get())) {
			status = Status.HUMAN;
			lock = 0;
			time = 0;
			sync();
		}
	}

	public void lock(Status ch) {
		if (status == Status.HUMAN) {
			if (player.level().isClientSide() || ch.clearedBySober() && player.hasEffect(YHEffects.SOBER.get()))
				return;
			status = ch;
			sync();
		}
		if (status != ch) return;
		lock = 2;
		if (time < 2) time = 2;
	}

	public void extend(Status ch, int duration) {
		if (status == Status.HUMAN) {
			if (ch.clearedBySober() && player.hasEffect(YHEffects.SOBER.get()))
				return;
			status = ch;
		}
		if (status != ch) return;
		if (time < duration) time = duration;
		sync();
	}

	public void triggerFlesh() {
		if (status == Status.YOUKAIFIED) {
			time += YHModConfig.COMMON.youkaifiedProlongation.get();
		} else if (status == Status.YOUKAIFYING) {
			time += YHModConfig.COMMON.youkaifyingTime.get();
			if (time > YHModConfig.COMMON.youkaifyingThreshold.get()) {
				status = Status.YOUKAIFIED;
				time = YHModConfig.COMMON.youkaifiedDuration.get();
			}
		} else if (status == Status.HUMAN) {
			if (player.getRandom().nextDouble() < YHModConfig.COMMON.youkaifyingChance.get()) {
				time = YHModConfig.COMMON.youkaifyingTime.get();
				int dur = YHModConfig.COMMON.youkaifyingConfusionTime.get();
				player.addEffect(new MobEffectInstance(MobEffects.CONFUSION,
						dur, 0, false, false, true));
			}
		}
	}

	public void sync() {
		if (player instanceof ServerPlayer sp)
			HOLDER.network.toClientSyncAll(sp);
	}

}
