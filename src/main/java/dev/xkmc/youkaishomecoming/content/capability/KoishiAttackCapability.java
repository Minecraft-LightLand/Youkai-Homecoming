package dev.xkmc.youkaishomecoming.content.capability;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHDamageTypes;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHCriteriaTriggers;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class KoishiAttackCapability extends PlayerCapabilityTemplate<KoishiAttackCapability> {

	public static final Capability<KoishiAttackCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<KoishiAttackCapability> HOLDER = new PlayerCapabilityHolder<>(
			YoukaisHomecoming.loc("koishi_attack"), CAPABILITY,
			KoishiAttackCapability.class, KoishiAttackCapability::new, PlayerCapabilityNetworkHandler::new
	);


	private static final int DELAY = 360, MARK_POS = 300;

	@SerialClass.SerialField
	private int tickRemain = 0;
	@SerialClass.SerialField
	private int attackCooldown = 0;
	@SerialClass.SerialField
	private int blockCount = 0;
	@SerialClass.SerialField
	private Vec3 source = null;

	@Override
	public void onClone(boolean isWasDeath) {
		blockCount = 0;
		tickRemain = 0;
	}

	protected void startParticle(Vec3 pos) {
		tickRemain = DELAY - MARK_POS;
		source = pos;
	}

	private boolean notValid() {
		return !player.level().dimension().equals(Level.NETHER) ||
				!player.canBeSeenAsEnemy() ||
				player.hasEffect(YHEffects.UNCONSCIOUS.get()) ||
				player.hasEffect(YHEffects.YOUKAIFIED.get()) ||
				!player.hasEffect(YHEffects.YOUKAIFYING.get());
	}

	@Override
	public void tick() {
		if (!YHModConfig.COMMON.koishiAttackEnable.get()) return;
		if (!(player instanceof ServerPlayer sp)) {
			if (source != null && tickRemain > 0 && tickRemain <= DELAY - MARK_POS) {
				ClientCapHandler.showParticle(player, source);
				tickRemain--;
				if (tickRemain == 0) source = null;
			}
			return;
		}
		if (tickRemain > 0) {
			tickRemain--;
			if (tickRemain == DELAY - MARK_POS) {
				source = RayTraceUtil.rayTraceBlock(player.level(), player, -2).getLocation();
				YoukaisHomecoming.HANDLER.toClientPlayer(new KoishiStartPacket(KoishiStartPacket.Type.PARTICLE, source), sp);
			}
			if (tickRemain == 0 && source != null) {
				attackCooldown = YHModConfig.COMMON.koishiAttackCoolDown.get();
				if (!notValid() && player.hurt(YHDamageTypes.koishi(player, source), YHModConfig.COMMON.koishiAttackDamage.get())) {
					blockCount = 0;
				}
				source = null;
			}
			return;
		}
		if (attackCooldown > 0) {
			attackCooldown--;
			return;
		}
		if (notValid()) {
			return;
		}
		if (player.getRandom().nextDouble() < YHModConfig.COMMON.koishiAttackChance.get()) {
			tickRemain = DELAY;
			YHCriteriaTriggers.KOISHI_RING.trigger(sp);
			YoukaisHomecoming.HANDLER.toClientPlayer(new KoishiStartPacket(KoishiStartPacket.Type.START, player.position()), sp);
		}

	}

	public void onBlock() {
		player.getCooldowns().addCooldown(player.getUseItem().getItem(), 100);
		blockCount++;
		if (blockCount >= YHModConfig.COMMON.koishiAttackBlockCount.get()) {
			blockCount = 0;
			player.spawnAtLocation(YHItems.KOISHI_HAT.get());
		}
	}

	public static void register() {
	}

}
