package dev.xkmc.youkaishomecoming.content.spell.custom.data;

import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgRange;
import dev.xkmc.youkaishomecoming.content.spell.custom.forms.HomingSpellForm;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.world.item.Item;

public record HomingSpellFormData(
		BaseSpellData base,
		RingFormData form,
		@ArgRange(base = 1, factor = 3, decimal = 1)
		double speed,
		@ArgRange(low = 1, high = 60)
		int turnTime
) implements ISpellFormData

		<HomingSpellFormData> {

	public static final HomingSpellFormData RING = new HomingSpellFormData(
			BaseSpellData.DEF, new RingFormData(4, 4, 5,
			90, 20, 2, 0),
			20, 1);

	public int getDuration() {
		return form.getDuration();
	}

	public int cost() {
		int cost = YHModConfig.COMMON.homingSpellDanmakuPerItemCost.get();
		return form().branches() * form().steps() / cost + 1;
	}

	@Override
	public Item getAmmoCost() {
		return base().getAmmoCost();
	}

	@Override
	public ItemSpell createInstance() {
		var spell = new HomingSpellForm();
		spell.init(this);
		return spell;
	}

}
