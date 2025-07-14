package dev.xkmc.youkaishomecoming.content.entity.animal.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public interface StateMachineMob extends IEntityAdditionalSpawnData {

	MobStateMachine<?, ?, ?> states();

	@Override
	default void writeSpawnData(FriendlyByteBuf buffer) {
		states().write(buffer);
	}

	@Override
	default void readSpawnData(FriendlyByteBuf data) {
		states().read(data);
	}

}
