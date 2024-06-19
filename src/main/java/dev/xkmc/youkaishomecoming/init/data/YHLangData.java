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
	FLESH_TASTE_HUMAN("tooltip.taste_human", "Unappealing smell...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_HALF_YOUKAI("tooltip.taste_half_youkai", "Strange flavor...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_YOUKAI("tooltip.taste_youkai", "Delicious!", 0, ChatFormatting.GRAY),
	DANMAKU_DAMAGE("tooltip.danmaku_damage", "Deals %s damage on hit", 1, ChatFormatting.BLUE),
	DANMAKU_BYPASS("tooltip.danmaku_bypass", "Bypasses entities", 0, ChatFormatting.DARK_AQUA),
	SPELL_TARGET("tooltip.spell_target", "Requires targeting an entity to activate", 0, ChatFormatting.RED),
	SPELL_COST("tooltip.spell_cost", "Costs 1 %s", 1, ChatFormatting.GRAY),
	KETTLE_INFO("tooltip.kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),
	DRYING_RACK("tooltip.drying_rack", "Only works directly under the sun", 0, ChatFormatting.GRAY),

	FLESH_NAME_HUMAN("flesh_human", "Weird Meat", 0, null),
	FLESH_NAME_YOUKAI("flesh_youkai", "Flesh", 0, null),

	FERMENT_PROGRESS("fermenting_progress", "Fermenting: %s", 1, ChatFormatting.GRAY),

	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	JEI_KETTLE("jei.kettle", "Tea Brewing", 0, null),
	JEI_RACK("jei.rack", "Drying", 0, null),
	JEI_FERMENT("jei.ferment", "Fermenting", 0, null),

	OBTAIN("obtain", "Source: ", 0, ChatFormatting.GRAY),
	UNKNOWN("unknown", "???", 0, ChatFormatting.GRAY),
	USAGE("usage", "Usage: ", 0, ChatFormatting.GRAY),

	OBTAIN_FLESH("obtain_flesh", "Kill human mobs with knife while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_BLOOD("obtain_blood", "Kill human mobs with knife and have glass bottle in off hand while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_FAIRY_ICE("obtain_fairy_ice", "Rarely dropped when you got hit by Cirno's Danmaku while wearing full leather suits. Dropped from Cirno.", 0, ChatFormatting.GRAY),
	USAGE_FAIRY_ICE("usage_fairy_ice", "Throw to deal damage and freeze target.", 0, ChatFormatting.GRAY),
	OBTAIN_FROZEN_FROG("obtain_frozen_frog", "Dropped when Cirno freezes a frog. Rarely dropped from Cirno when defeated with Danmaku.", 0, ChatFormatting.GRAY),
	USAGE_FROZEN_FROG("usage_frozen_frog", "Throw toward target to summon a frog.", 0, ChatFormatting.GRAY),
	USAGE_DANMAKU("usage_danmaku", "While in %s or %s effect, or equip touhou hats, you can shoot danmaku", 2, ChatFormatting.GRAY),

	USAGE_STRAW_HAT("usage_straw_hat", "While in %s or %s effect, you can equip it on frogs to allow them to eat raiders", 2, ChatFormatting.GRAY),
	OBTAIN_SUWAKO_HAT("obtain_suwako_hat", "Drops when frog with hat eats %s different kinds of raiders in front of villagers", 1, ChatFormatting.GRAY),
	USAGE_SUWAKO_HAT("usage_suwako_hat", "Grants constant %s. Allows using Cyan and Lime danmaku without consumption.", 1, ChatFormatting.GRAY),
	OBTAIN_KOISHI_HAT("obtain_koishi_hat", "Drops when blocking Koishi attacks %s times in a row", 1, ChatFormatting.GRAY),
	USAGE_KOISHI_HAT("usage_koishi_hat", "Grants constant %s. Allows using Blue and Red danmaku without consumption.", 1, ChatFormatting.GRAY),
	OBTAIN_RUMIA_HAIRBAND("obtain_rumia_hairband", "Drops when player defeat Ex. Rumia with Danmaku", 0, ChatFormatting.GRAY),
	USAGE_RUMIA_HAIRBAND("usage_rumia_hairband", "Grants constant %s when applicable. Drops heads when killing mobs. Flesh and blood drops no longer require knife (bonus when still using knife).", 1, ChatFormatting.GRAY),
	OBTAIN_REIMU_HAIRBAND("obtain_reimu_hairband", "Feed Reimu a variety of food", 0, ChatFormatting.GRAY),
	USAGE_REIMU_HAIRBAND("usage_reimu_hairband", "Enables Creative Flight. Your danmaku damage bypasses magical protection.", 0, ChatFormatting.GRAY),

	REIMU_FLESH("reimu_flesh", "Reimu: You shall not eat it. Last warning.", 0, ChatFormatting.RED),
	REIMU_WARN("reimu_warn", "Reimu: Drink some tea and keep your sanity. Last warning.", 0, ChatFormatting.RED),
	KOISHI_REIMU("koishi_reimu", "Reimu: ???", 0, ChatFormatting.RED),

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
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.koishi_ring", "Koishi Phone Call");
		pvd.add("death.attack.koishi_attack", "Koishi stabbed %s in the back");
		pvd.add("death.attack.koishi_attack.player", "%2$s stabbed %1$s in the back");
		pvd.add("death.attack.rumia_attack", "%s is eaten by Rumia");
		pvd.add("death.attack.rumia_attack.player", "%s is eaten by %s");
		pvd.add("death.attack.danmaku", "%s lost the danmaku battle");
		pvd.add("death.attack.danmaku.player", "%s lost the danmaku battle to %s");
		pvd.add("death.attack.abyssal_danmaku", "%s lost the danmaku battle");
		pvd.add("death.attack.abyssal_danmaku.player", "%s lost the danmaku battle to %s");


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
