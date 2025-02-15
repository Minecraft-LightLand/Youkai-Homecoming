package dev.xkmc.youkaishomecoming.content.spell.shooter;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record ShooterData(
		int health, float damage, int life,
		@Nullable ResourceLocation circle
) {

	public static final ShooterData EMPTY = new ShooterData(20, 4, 100, null);

}
