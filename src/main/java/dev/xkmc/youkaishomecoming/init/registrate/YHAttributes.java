package dev.xkmc.youkaishomecoming.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2damagetracker.init.L2DamageTracker.ATTR_TAGS;

public class YHAttributes {

	public static final RegistryEntry<Attribute> MAX_RESOURCE, MAX_POWER, INITIAL_RESOURCE, INITIAL_POWER, GRAZE_EFFECTIVENESS, HITBOX;

	static {
		var reg = YoukaisHomecoming.REGISTRATE;
		MAX_RESOURCE = reg(reg, "max_resource", 0, -10, 10, "Additional Max Resource");
		MAX_POWER = reg(reg, "max_power", 0, -10, 10, "Additional Max Power");
		INITIAL_RESOURCE = reg(reg, "initial_resource", 0, -10, 10, "Additional Initial Resource");
		INITIAL_POWER = reg(reg, "initial_power", 0, -10, 10, "Additional Initial Power");
		GRAZE_EFFECTIVENESS = reg(reg, "graze_effectiveness", 1, 0, 10, "Graze Multiplier", L2DamageTracker.PERCENTAGE);
		HITBOX = reg(reg, "hit_box", 0, -0.2, 1, "Hit Box Modification", L2DamageTracker.NEGATIVE);
	}

	@SafeVarargs
	public static RegistryEntry<Attribute> reg(L2Registrate reg, String id, double def, double min, double max, String name, TagKey<Attribute>... keys) {
		reg.addRawLang("attribute." + reg.getModid() + "." + id, name);
		return reg.generic(reg, id, ForgeRegistries.ATTRIBUTES.getRegistryKey(), () ->
				new RangedAttribute("attribute." + reg.getModid() + "." + id, def, min, max)
						.setSyncable(true)).tag(ATTR_TAGS, keys).register();
	}


	public static void register() {

	}

}
