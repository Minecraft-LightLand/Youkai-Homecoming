package dev.xkmc.youkaishomecoming.content.spell.custom.forms;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.spell.item.ItemSpell;

@SerialClass
public class CustomItemSpell extends ItemSpell {

	@SerialClass.SerialField
	private ISpellForm<?> form;


	public CustomItemSpell() {
	}

	public CustomItemSpell(ISpellForm<?> form) {
		this.form = form;
		addTicker(form);
	}

}
