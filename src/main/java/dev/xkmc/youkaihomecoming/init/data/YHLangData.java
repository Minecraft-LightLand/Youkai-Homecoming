package dev.xkmc.youkaihomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.youkaihomecoming.init.YoukaiHomecoming;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum YHLangData {
	CHANCE_EFFECT("tooltip.chance", "%1$s with %2$s%% chance", 2, ChatFormatting.GRAY),
	PLACE("tooltip.place", "Can be placed on ground", 0, ChatFormatting.GRAY),
	FLESH_NAME_HUMAN("flesh_human", "Weird Meat", 0, null),
	FLESH_NAME_YOUKAI("flesh_youkai", "Flesh", 0, null),
	FLESH_TASTE_HUMAN("taste_human", "Unappealing smell...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_HALF_YOUKAI("taste_half_youkai", "Strange flavor...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_YOUKAI("taste_youkai", "Delicious!", 0, ChatFormatting.GRAY),
	OBTAIN("obtain", "Source: ", 0, ChatFormatting.GRAY),
	UNKNOWN("unknown", "???", 0, ChatFormatting.GRAY),
	USAGE("usage", "Effect: ", 0, ChatFormatting.GRAY),
	KETTLE_INFO("kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),

	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	JEI_KETTLE("jei.kettle", "Tea Brewing", 0, null),
	JEI_RACK("jei.rack", "Drying", 0, null),

	OBTAIN_FLESH("obtain_flesh", "Kill human mobs with knife while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_BLOOD("obtain_blood", "Kill human mobs with knife and have glass bottle in off hand while in %s or %s effect", 2, ChatFormatting.GRAY),
	;

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	YHLangData(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = YoukaiHomecoming.MODID + "." + key;
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

	public String key(){
		return key;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (YHLangData lang : YHLangData.values()) {
			pvd.add(lang.key, lang.def);
		}
	}


}
