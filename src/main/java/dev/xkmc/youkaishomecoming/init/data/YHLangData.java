package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public enum YHLangData {
	CHANCE_EFFECT("tooltip.chance", "%1$s with %2$s%% chance", 2, ChatFormatting.GRAY),
	PLACE("tooltip.place", "Can be placed on ground", 0, ChatFormatting.GRAY),
	KETTLE_INFO("tooltip.kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),
	DRYING_RACK("tooltip.drying_rack", "Only works directly under the sun", 0, ChatFormatting.GRAY),

	MOON_LANTERN_PLACE("tooltip.moon_lantern_place", "Udumbara within 3x3x3 blocks below this latern will grow as night as if they can see full moon.", 0, ChatFormatting.GRAY),
	MOON_LANTERN_HOLD("tooltip.moon_lantern_hold", "When holding in hand, Udumbara effect will always trigger at night regardless if player can see full moons", 0, ChatFormatting.GRAY),

	FERMENT_PROGRESS("fermenting_progress", "Fermenting: %s", 1, ChatFormatting.GRAY),
	CUISINE_ALLOW("cuisine_allow", "Next Step:", 0, ChatFormatting.GRAY),
	CUISINE_EXTRA("cuisine_extra", "And %s more...", 1, ChatFormatting.GRAY),

	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	JEI_KETTLE("jei.kettle", "Tea Brewing", 0, null),
	JEI_RACK("jei.rack", "Drying", 0, null),
	JEI_FERMENT("jei.ferment", "Fermenting", 0, null),
	JEI_BASIN("jei.basin", "Basin", 0, null),
	JEI_STEAM("jei.steam", "Steaming", 0, null),
	JEI_CUISINE("jei.cuisine", "Cuisine", 0, null),
	JEI_COOKING("jei.cooking", "Cooking", 0, null),

;
	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	YHLangData(String key, String def, int arg, @Nullable ChatFormatting format) {
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
		for (YHLangData lang : YHLangData.values()) {
			pvd.add(lang.key, lang.def);
		}

		List<Item> list = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW);
		for (RegistryEntry<? extends Potion> ent : YHEffects.POTION_LIST) {
			for (Item item : list) {
				String pref = item.getDescriptionId();
				String[] prefs = pref.split("\\.");
				String str = ent.get().getName(item.getDescriptionId() + ".effect.");
				String[] ids = ent.get().getEffects().get(0).getDescriptionId().split("\\.");
				String id = ids[ids.length - 1];
				String name = RegistrateLangProvider.toEnglishName(id);
				String pref_name = RegistrateLangProvider.toEnglishName(prefs[prefs.length - 1]);
				if (item == Items.TIPPED_ARROW) pref_name = "Arrow";
				pvd.add(str, pref_name + " of " + name);
			}
		}
	}


}
