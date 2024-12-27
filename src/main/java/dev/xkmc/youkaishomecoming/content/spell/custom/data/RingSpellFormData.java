package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgRange;
import dev.xkmc.youkaishomecoming.content.spell.custom.forms.RingSpellForm;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.item.Item;

public record RingSpellFormData(
		BaseSpellData base,
		RingFormData form,
		@ArgRange(base = 1, factor = 3, decimal = 1)
		double speedFirst,
		@ArgRange(base = 1, factor = 3, decimal = 1)
		double speedLast,
		@ArgRange
		double randomizedSpeed
) implements ISpellFormData<RingSpellFormData> {

	public static final RingSpellFormData SIMPLE = new RingSpellFormData(
			BaseSpellData.DEF, new RingFormData(6, 6, 0,
			60, 10, 0, 0),
			0.8, 0.8, 0);

	public static final RingSpellFormData FLOWER = new RingSpellFormData(
			BaseSpellData.DEF, new RingFormData(6, 6, 1,
			60, 10, 3, 0),
			1, 0.6, 0);

	public static final RingSpellFormData FAN = new RingSpellFormData(
			BaseSpellData.DEF, new RingFormData(3, 5, 3,
			30, 10, 0, 0),
			0.8, 0.8, 0);

	public static final RingSpellFormData RAND = new RingSpellFormData(
			BaseSpellData.DEF, new RingFormData(7, 8, 10,
			5, 0, 0, 3),
			0.8, 0.8, 0.2);

	public int getDuration() {
		return form.getDuration();
	}

	public int cost() {
		int cost = YHModConfig.COMMON.ringSpellDanmakuPerItemCost.get();
		return form().branches() * form().steps() / cost + 1;
	}

	@Override
	public Item getAmmoCost() {
		return base().getAmmoCost();
	}

	public ItemSpell createInstance() {
		var form = new RingSpellForm();
		form.init(this);
		return form;
	}

}
