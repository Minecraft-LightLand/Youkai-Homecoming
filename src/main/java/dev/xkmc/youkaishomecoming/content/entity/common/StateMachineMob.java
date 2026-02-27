package dev.xkmc.youkaishomecoming.content.entity.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.List;

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

	default List<INotifyMoveGoal> notifiers() {
		return List.of();
	}

}
