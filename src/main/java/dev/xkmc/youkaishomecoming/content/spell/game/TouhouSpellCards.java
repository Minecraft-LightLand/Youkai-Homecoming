package dev.xkmc.youkaishomecoming.content.spell.game;

import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.koishi.Polygraph;
import dev.xkmc.youkaishomecoming.content.spell.game.reimu.StagedHoming;
import dev.xkmc.youkaishomecoming.content.spell.game.yukari.DoubleButterfly;
import dev.xkmc.youkaishomecoming.content.spell.game.yukari.LightHole;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.ListSpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCard;

public class TouhouSpellCards {

	public static void setSpell(GeneralYoukaiEntity e, String id) {
		if (id.equals("touhou_little_maid:yukari_yakumo")) {
			e.spellCard.card = yukari();
		}
		if (id.equals("touhou_little_maid:hakurei_reimu")) {
			e.spellCard.card = reimu();
		}
		if (id.equals("touhou_little_maid:komeiji_koishi")) {
			e.spellCard.card = koishi();
		}
	}

	public static SpellCard yukari() {
		ListSpellCard ans = new ListSpellCard();
		ans.list.add(new LightHole());
		ans.list.add(new DoubleButterfly());
		return ans;
	}

	public static SpellCard reimu() {
		ListSpellCard ans = new ListSpellCard();
		ans.list.add(new StagedHoming());
		return ans;
	}

	public static SpellCard koishi() {
		ListSpellCard ans = new ListSpellCard();
		ans.list.add(new Polygraph());
		return ans;
	}


}
