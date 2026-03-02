package dev.xkmc.youkaishomecoming.content.entity.common;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import java.util.List;

public interface StateMachineMob extends IEntityWithComplexSpawn {

	MobStateMachine<?, ?, ?> states();

	@Override
	default void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		states().write(buffer);
	}

	@Override
	default void readSpawnData(RegistryFriendlyByteBuf data) {
		states().read(data);
	}

	default List<INotifyMoveGoal> notifiers() {
		return List.of();
	}

}
