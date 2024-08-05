package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import dev.xkmc.l2library.capability.conditionals.*;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.player.Player;

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
		ConditionalData.HOLDER.get(player).getOrCreateData(HOLDER, HOLDER).update(player);
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
			if (!player.getAbilities().mayfly) {
				player.getAbilities().mayfly = true;
				player.onUpdateAbilities();
			}
			return false;
		} else {
			if (player.getAbilities().mayfly || player.getAbilities().flying) {
				if (player.isCreative() || player.isSpectator()) {
					return true;
				} else {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.onUpdateAbilities();
				}
			}
			return true;
		}
	}

}
