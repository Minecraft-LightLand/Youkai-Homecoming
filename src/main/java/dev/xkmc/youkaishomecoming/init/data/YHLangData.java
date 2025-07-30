package dev.xkmc.youkaishomecoming.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHDanmaku;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
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
	SPELL_COST("tooltip.spell_cost", "Costs %s %s", 2, ChatFormatting.GRAY),
	KETTLE_INFO("tooltip.kettle", "Right click with water bucket or water bottle to fill water", 0, ChatFormatting.GRAY),
	DRYING_RACK("tooltip.drying_rack", "Only works directly under the sun", 0, ChatFormatting.GRAY),
	CAMELLIA("tooltip.camellia", "Prevent Phantom spawn when equipped", 0, ChatFormatting.GRAY),
	FLASK_OF("flask.of", "Flask Of %s", 1, null),
	FLASK_INFO_DRINK("flask.info_drink", "Stores 4 bottles of drink. Consume 0.2 bottles per slip", 0, ChatFormatting.GRAY),
	FLASK_INFO_SAUCE("flask.info_sauce", "Stores 4 bottles of sauce. Consume 0.2 bottles per use on Pots and Cuisine Table", 0, ChatFormatting.GRAY),
	FLASK_CONTENT("flask.content", "Content: %s", 1, ChatFormatting.GRAY),
	FLASK_USE("flask.use", "Remaining Use: %s/%s", 2, ChatFormatting.GRAY),

	MOON_LANTERN_PLACE("tooltip.moon_lantern_place", "Udumbara within 3x3x3 blocks below this latern will grow as night as if they can see full moon.", 0, ChatFormatting.GRAY),
	MOON_LANTERN_HOLD("tooltip.moon_lantern_hold", "When holding in hand, Udumbara effect will always trigger at night regardless if player can see full moons", 0, ChatFormatting.GRAY),

	FLESH_NAME_HUMAN("flesh_human", "Weird Meat", 0, null),
	FLESH_NAME_YOUKAI("flesh_youkai", "Flesh", 0, null),

	FERMENT_PROGRESS("fermenting_progress", "Fermenting: %s", 1, ChatFormatting.GRAY),
	CUISINE_ALLOW("cuisine_allow", "Next Step:", 0, ChatFormatting.GRAY),
	CUISINE_EXTRA("cuisine_extra", "And %s more...", 1, ChatFormatting.GRAY),
	HEAT_PROGRESS("heat_progress", "Heating: %s", 1, ChatFormatting.GRAY),
	BREWING_PROGRESS("brewing_progress", "Brewing: %s", 1, ChatFormatting.GRAY),
	COOKING_PROGRESS("cooking_progress", "Cooking: %s", 1, ChatFormatting.GRAY),
	STEAMER_TOO_MANY("steamer.too_many", "Too many racks!", 0, ChatFormatting.RED),
	STEAMER_NO_WATER("steamer.no_water", "Next Step: Add water", 0, ChatFormatting.GRAY),
	STEAMER_NO_HEAT("steamer.no_heat", "Next Step: Put heat source beneath", 0, ChatFormatting.GRAY),
	STEAMER_NO_RACK("steamer.no_rack", "Next Step: Add steam racks", 0, ChatFormatting.GRAY),
	STEAMER_NO_CAP("steamer.no_cap", "Cap top rack to steam faster", 0, ChatFormatting.GRAY),

	JEI_MOKA("jei.moka", "Coffee Brewing", 0, null),
	JEI_KETTLE("jei.kettle", "Tea Brewing", 0, null),
	JEI_RACK("jei.rack", "Drying", 0, null),
	JEI_FERMENT("jei.ferment", "Fermenting", 0, null),
	JEI_BASIN("jei.basin", "Basin", 0, null),
	JEI_STEAM("jei.steam", "Steaming", 0, null),
	JEI_CUISINE("jei.cuisine", "Cuisine", 0, null),
	JEI_COOKING("jei.cooking", "Cooking", 0, null),

	OBTAIN("obtain", "Source: ", 0, ChatFormatting.GRAY),
	UNKNOWN("unknown", "???", 0, ChatFormatting.GRAY),
	USAGE("usage", "Usage: ", 0, ChatFormatting.GRAY),

	OBTAIN_FLESH("obtain_flesh", "Kill human mobs with knife while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_BLOOD("obtain_blood", "Kill human mobs with knife and have glass bottle in off hand while in %s or %s effect", 2, ChatFormatting.GRAY),
	OBTAIN_FAIRY_ICE("obtain_fairy_ice", "Rarely dropped when you got hit by Cirno's Danmaku while wearing full leather suits. Dropped from Cirno. Could be obtained by trading with Cirno as well.", 0, ChatFormatting.GRAY),
	USAGE_FAIRY_ICE("usage_fairy_ice", "Throw to deal damage and freeze target.", 0, ChatFormatting.GRAY),
	OBTAIN_FROZEN_FROG("obtain_frozen_frog", "Dropped when Cirno freezes a frog. Rarely dropped from Cirno when defeated with Danmaku.", 0, ChatFormatting.GRAY),
	USAGE_FROZEN_FROG("usage_frozen_frog", "Throw toward target to summon a frog.", 0, ChatFormatting.GRAY),
	USAGE_DANMAKU("usage_danmaku", "While in %s or %s effect, or equip touhou hats, you can shoot danmaku", 2, ChatFormatting.GRAY),

	USAGE_STRAW_HAT("usage_straw_hat", "While in %s or %s effect, you can equip it on frogs to allow them to eat raiders", 2, ChatFormatting.GRAY),
	OBTAIN_SUWAKO_HAT("obtain_suwako_hat", "Drops when frog with hat eats %s different kinds of raiders in front of villagers", 1, ChatFormatting.GRAY),
	OBTAIN_KOISHI_HAT("obtain_koishi_hat", "Drops when blocking Koishi attacks %s times in a row", 1, ChatFormatting.GRAY),
	OBTAIN_RUMIA_HAIRBAND("obtain_rumia_hairband", "Drops when player defeat Ex. Rumia with Danmaku", 0, ChatFormatting.GRAY),
	USAGE_RUMIA_HAIRBAND("usage_rumia_hairband", "Drops heads when killing mobs. Flesh and blood drops no longer require knife (bonus when still using knife).", 0, ChatFormatting.GRAY),
	OBTAIN_REIMU_HAIRBAND("obtain_reimu_hairband", "Feed Reimu a variety of food", 0, ChatFormatting.GRAY),
	USAGE_REIMU_HAIRBAND("usage_reimu_hairband", "Enables creative flight. Your danmaku damage bypasses magical protection.", 0, ChatFormatting.GRAY),
	OBTAIN_CIRNO_HAIRBAND("obtain_cirno_hairband", "Trade with Cirno", 0, ChatFormatting.GRAY),
	USAGE_CIRNO_HAIRBAND("usage_cirno_hairband", "Your magic damage freezes target (and frogs).", 0, ChatFormatting.GRAY),
	USAGE_FAIRY_WINGS("usage_fairy_wings", "When you have %s, enables creative flight.", 1, ChatFormatting.GRAY),

	CONSTANT_EFFECT("constant_effect", "Grants constant %s when applicable.", 1, ChatFormatting.GRAY),
	DANMAKU_SUPPORT_1("no_consume_1", "Allows using %s danmaku without consumption.", 1, ChatFormatting.GRAY),
	DANMAKU_SUPPORT_2("no_consume_2", "Allows using %s and %s danmaku without consumption.", 2, ChatFormatting.GRAY),

	REIMU_FLESH("reimu_flesh", "Reimu: You shall not eat it. Last warning.", 0, ChatFormatting.RED),
	REIMU_WARN("reimu_warn", "Reimu: Drink some tea and keep your sanity. Last warning.", 0, ChatFormatting.RED),
	KOISHI_REIMU("koishi_reimu", "Reimu: ???", 0, ChatFormatting.RED),

	EDITOR_RESET("custom_spell.reset", "Reset", 0, null),
	INVALID_TIME("custom_spell.invalid_time", "Max duration of %s allowed. Current duration: %s", 2, ChatFormatting.RED);

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
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.deer_ambient", "Deer baahs");
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.deer_hurt", "Deer hurts");
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.deer_death", "Deer dies");
		pvd.add("biome." + YoukaisHomecoming.MODID + ".sakura_forest", "Sakura Forest");
		for (YHLangData lang : YHLangData.values()) {
			pvd.add(lang.key, lang.def);
		}
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.koishi_ring", "Koishi Phone Call");
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.graze", "Danmaku Graze");
		pvd.add(YoukaisHomecoming.MODID + ".subtitle.miss", "Danmaku Hit Player");
		pvd.add("death.attack.koishi_attack", "Koishi stabbed %s in the back");
		pvd.add("death.attack.koishi_attack.player", "%2$s stabbed %1$s in the back");
		pvd.add("death.attack.rumia_attack", "%s is eaten by Rumia");
		pvd.add("death.attack.rumia_attack.player", "%s is eaten by %s");
		pvd.add("death.attack.danmaku", "%s lost the danmaku battle");
		pvd.add("death.attack.danmaku.player", "%s lost the danmaku battle to %s");
		pvd.add("death.attack.abyssal_danmaku", "%s lost the danmaku battle");
		pvd.add("death.attack.abyssal_danmaku.player", "%s lost the danmaku battle to %s");

		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.bullet.title", "Bullet Type");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.color.title", "Bullet Color");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.branches", "Branch Count");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.branches.desc", "Number of branches of bullets to shoot");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.steps", "Step Count");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.steps.desc", "Number of bullets per branch");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.delay", "Step Delay");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.delay.desc", "Delay in ticks per step");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.range", "Bullet Range");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.range.desc", "Distance for bullet to fly before vanishing");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedRange", "Range Variation");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedRange.desc", "Variation of bullet range in percentage, plus or minus");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.branchAngle", "Branch Angle Offset");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.branchAngle.desc", "Horizontal angle difference between adjacent branches, in degree");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.stepAngle", "Step Angle Offset");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.stepAngle.desc", "Horizontal angle difference between adjacent steps, in degree");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.stepVerticalAngle", "Step Vertical Angle Offset");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.stepVerticalAngle.desc", "Vertical angle difference between adjacent steps, in degree");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedAngle", "Angle Variation");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedAngle.desc", "Variation of bullet direction in degree, both horizontal and vertical, plus or minus");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speed", "Bullet Speed");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speed.desc", "Bullet speed in block per tick");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speedFirst", "First Step Speed");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speedFirst.desc", "Bullet speed in block per tick for first step");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speedLast", "Last Step Speed");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.speedLast.desc", "Bullet speed in block per tick for last step");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedSpeed", "Speed Variation");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.randomizedSpeed.desc", "Variation of bullet speed in percentage, plus or minus");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.turnTime", "Turn Time");
		pvd.add(YoukaisHomecoming.MODID + ".custom_spell.turnTime.desc", "Time in tick after which bullet will redirect toward target");


		for (var e : YHDanmaku.Bullet.values()) {
			var name = e.name().toLowerCase(Locale.ROOT);
			pvd.add(YoukaisHomecoming.MODID + ".custom_spell.bullet." + name,
					RegistrateLangProvider.toEnglishName(name));
		}

		for (var e : DyeColor.values()) {
			var name = e.getName();
			pvd.add(YoukaisHomecoming.MODID + ".custom_spell.color." + name,
					RegistrateLangProvider.toEnglishName(name));
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
