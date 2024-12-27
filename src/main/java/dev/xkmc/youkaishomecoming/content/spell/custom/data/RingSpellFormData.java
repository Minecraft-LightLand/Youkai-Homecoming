package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgRange;
import dev.xkmc.youkaishomecoming.content.spell.custom.forms.CustomItemSpell;
import dev.xkmc.youkaishomecoming.content.spell.custom.forms.RingSpellForm;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;

public record RingSpellFormData(
		BaseSpellData base,
		@ArgRange(low = 1, high = 16)
		int branches,
		@ArgRange(low = 1, high = 64)
		int steps,
		@ArgRange(high = 40)
		int delay,
		@ArgRange(base = 30, factor = 6)
		double branchAngle,
		@ArgRange(base = 30, factor = 6)
		double stepAngle,
		@ArgRange(base = 15, factor = 6)
		double stepVerticalAngle,
		@ArgRange(base = 10, factor = 6)
		double randomizedAngle,
		@ArgRange(base = 1, factor = 3, decimal = 1)
		double speedFirst,
		@ArgRange(base = 1, factor = 3, decimal = 1)
		double speedLast,
		@ArgRange
		double randomizedSpeed
) implements ISpellFormData<RingSpellFormData> {

	public static final RingSpellFormData SIMPLE = new RingSpellFormData(
			BaseSpellData.DEF, 6, 6, 0,
			60, 10, 0, 0,
			0.8, 0.8, 0);

	public static final RingSpellFormData FLOWER = new RingSpellFormData(
			BaseSpellData.DEF, 6, 6, 1,
			60, 10, 3, 0,
			1, 0.6, 0);

	public static final RingSpellFormData FAN = new RingSpellFormData(
			BaseSpellData.DEF, 3, 5, 3,
			30, 10, 0, 0,
			0.8, 0.8, 0);

	public static final RingSpellFormData RAND = new RingSpellFormData(
			BaseSpellData.DEF, 7, 8, 10,
			5, 0, 0, 3,
			0.8, 0.8, 0.2);

	public int getDuration() {
		return (steps - 1) * delay;
	}

	public int cost() {
		int cost = YHModConfig.COMMON.customSpellDanmakuPerItemCost.get();
		return branches * steps / cost + 1;
	}

	public ItemSpell createInstance() {
		var form = new RingSpellForm();
		form.init(this);
		return new CustomItemSpell(form);
	}

}
