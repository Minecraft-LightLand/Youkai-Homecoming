package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum CoffeeLang {
	CHANCE_EFFECT("tooltip.chance", "%1$s with %2$s%% chance", 2, ChatFormatting.GRAY),
	PLACE("tooltip.place", "Can be placed on ground", 0, ChatFormatting.GRAY),
	KETTLE_INFO("tooltip.kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),
	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	;

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	CoffeeLang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = YoukaisHomecoming.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public String key() {
		return key;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (CoffeeLang lang : CoffeeLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}


}
