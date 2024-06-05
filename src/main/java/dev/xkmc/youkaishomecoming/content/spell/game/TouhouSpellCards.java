package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.koishi.KoishiTest;
import dev.xkmc.youkaishomecoming.content.spell.game.reimu.StagedHoming;
import dev.xkmc.youkaishomecoming.content.spell.game.youmu.YoumuSlash;
import dev.xkmc.youkaishomecoming.content.spell.game.yukari.DoubleButterfly;
import dev.xkmc.youkaishomecoming.content.spell.game.yukari.LightHole;
import dev.xkmc.youkaishomecoming.content.spell.game.yuyuko.YuyukoTest;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ListSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCard;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TouhouSpellCards {

	private static final Map<String, Supplier<SpellCard>> MAP = new ConcurrentHashMap<>();

	public static void registerSpell(String id, Supplier<SpellCard> card) {
		MAP.put(id, card);
	}

	public static void registerSpells() {
		registerSpell("touhou_little_maid:yukari_yakumo", () -> ListSpellCard.of(new LightHole(), new DoubleButterfly()));
		registerSpell("touhou_little_maid:hakurei_reimu", () -> ListSpellCard.of(new StagedHoming()));
		registerSpell("touhou_little_maid:komeiji_koishi", () -> ListSpellCard.of(new KoishiTest()));
		registerSpell("touhou_little_maid:konpaku_youmu", () -> ListSpellCard.of(new YoumuSlash()));
		registerSpell("touhou_little_maid:saigyouji_yuyuko", () -> ListSpellCard.of(new YuyukoTest()));
	}

	public static void setSpell(GeneralYoukaiEntity e, String id) {
		var sup = MAP.get(id);
		if (sup != null) e.spellCard.card = sup.get();
		var rl = new ResourceLocation(id);
		var name = Component.translatable(rl.toLanguageKey("model") + ".name");
		var desc = Component.translatable(rl.toLanguageKey("model") + ".desc");
		e.setCustomName(name.append(" - ").append(desc));
	}

}
