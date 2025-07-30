package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.xkmc.l2library.capability.conditionals.*;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class FlyingToken extends ConditionalToken {

	private static final TokenKey<FlyingToken> KEY = TokenKey.of(YoukaisHomecoming.loc("flying"));

	private record TokenHolder() implements TokenProvider<FlyingToken, TokenHolder>, Context {

		@Override
		public FlyingToken getData(TokenHolder tokenHolder) {
			return new FlyingToken();
		}

		@Override
		public TokenKey<FlyingToken> getKey() {
			return KEY;
		}
	}

	private static final TokenHolder HOLDER = new TokenHolder();

	public static void tickFlying(Player player) {
		if (YHModConfig.COMMON.reimuHairbandFlightEnable.get())
			ConditionalData.HOLDER.get(player).getOrCreateData(HOLDER, HOLDER).update(player);
	}

	public static boolean flyTravel(Player player, Vec3 vec3, Operation<Void> original) {
		if (!player.getAbilities().flying || player.isPassenger() ||
				ConditionalData.HOLDER.get(player).getData(KEY) == null)
			return false;
		var pos = player.position;
		player.moveRelative(1, vec3);
		var vel = player.getDeltaMovement();
		player.move(MoverType.SELF, vel.multiply(0.3, 1.33, 0.3));
		player.setDeltaMovement(vel.multiply(0.4, 0.6, 0.4));
		return true;
	}

	@SerialClass.SerialField
	private int life = 0;

	public void update(Player player) {
		life = 2;
	}

	@Override
	public boolean tick(Player player) {
		life--;
		if (life > 0) {
			if (!player.level().isClientSide() && !player.getAbilities().mayfly) {
				player.getAbilities().mayfly = true;
				player.onUpdateAbilities();
			}
			return false;
		} else {
			if (player.getAbilities().mayfly || player.getAbilities().flying) {
				if (player.isCreative() || player.isSpectator()) {
					return true;
				} else if (!player.level().isClientSide()) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.onUpdateAbilities();
				}
			}
			return true;
		}
	}

}
