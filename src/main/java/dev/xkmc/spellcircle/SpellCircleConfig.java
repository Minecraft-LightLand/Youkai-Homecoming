package dev.xkmc.spellcircle;

import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@SerialClass
public class SpellCircleConfig extends BaseConfig {

	@Nullable
	public static SpellComponent getFromConfig(ResourceLocation s) {
		return YoukaisHomecoming.SPELL.getMerged().map.get(s.toString());
	}

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<String, SpellComponent> map = new HashMap<>();

}
