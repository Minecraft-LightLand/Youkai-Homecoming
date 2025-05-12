package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy;

import com.github.tartaricacid.touhoulittlemaid.entity.monster.FairyType;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.spell.MediumFairySpell;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.spell.SmallFairySpell;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import net.minecraft.world.item.DyeColor;

public class FairySpellCards {


	public static final DyeColor[] PRIMARY, SECONDARY;

	static {
		PRIMARY = new DyeColor[FairyType.values().length];
		SECONDARY = new DyeColor[FairyType.values().length];
		PRIMARY[FairyType.BLACK.ordinal()] = DyeColor.BLACK;
		PRIMARY[FairyType.ORANGE.ordinal()] = DyeColor.ORANGE;
		PRIMARY[FairyType.WHITE.ordinal()] = DyeColor.WHITE;
		PRIMARY[FairyType.YELLOW.ordinal()] = DyeColor.YELLOW;
		PRIMARY[FairyType.BROWN.ordinal()] = DyeColor.BROWN;
		PRIMARY[FairyType.RED.ordinal()] = DyeColor.RED;
		PRIMARY[FairyType.GREEN.ordinal()] = DyeColor.GREEN;
		PRIMARY[FairyType.BLUE.ordinal()] = DyeColor.BLUE;
		PRIMARY[FairyType.PINK.ordinal()] = DyeColor.PINK;
		PRIMARY[FairyType.LIME.ordinal()] = DyeColor.LIME;
		PRIMARY[FairyType.CYAN.ordinal()] = DyeColor.CYAN;
		PRIMARY[FairyType.PURPLE.ordinal()] = DyeColor.PURPLE;
		PRIMARY[FairyType.GRAY.ordinal()] = DyeColor.GRAY;
		PRIMARY[FairyType.LIGHT_GRAY.ordinal()] = DyeColor.LIGHT_GRAY;
		PRIMARY[FairyType.LIGHT_BLUE.ordinal()] = DyeColor.LIGHT_BLUE;
		PRIMARY[FairyType.MAGENTA.ordinal()] = DyeColor.MAGENTA;

		PRIMARY[FairyType.LIGHT_PURPLE.ordinal()] = DyeColor.MAGENTA;
		SECONDARY[FairyType.LIGHT_PURPLE.ordinal()] = DyeColor.PURPLE;
		PRIMARY[FairyType.GOLD.ordinal()] = DyeColor.ORANGE;
		SECONDARY[FairyType.GOLD.ordinal()] = DyeColor.YELLOW;
	}

	public static void registerSpells() {
		for (int i = 0; i < 16; i++) {
			var col = PRIMARY[i];
			TouhouSpellCards.registerSpell("fairy:" + i, () -> new SmallFairySpell().init(col));
		}
		for (int i = 16; i < 18; i++) {
			var a = PRIMARY[i];
			var b = SECONDARY[i];
			TouhouSpellCards.registerSpell("fairy:" + i, () -> new MediumFairySpell().init(a, b));
		}
	}
}
