package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.youkaishomecoming.content.spell.game.yukari.DoubleButterfly;
import dev.xkmc.youkaishomecoming.content.spell.game.yukari.LightHole;

public class SpellCards {

	public static SpellCard yukari() {
		ListSpellCard ans = new ListSpellCard();
		ans.list.add(new LightHole());
		ans.list.add(new DoubleButterfly());
		return ans;
	}

}
